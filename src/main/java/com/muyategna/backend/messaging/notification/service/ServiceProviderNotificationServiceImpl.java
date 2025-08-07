package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.common.entity.Language;
import com.muyategna.backend.common.service.LanguageService;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.repository.JobRequestRepository;
import com.muyategna.backend.job_request.service.JobLeadClaimService;
import com.muyategna.backend.messaging.notification.dto.NotificationCategoryDto;
import com.muyategna.backend.messaging.notification.dto.NotificationDto;
import com.muyategna.backend.messaging.notification.dto.queue.*;
import com.muyategna.backend.messaging.notification.entity.Notification;
import com.muyategna.backend.messaging.notification.entity.NotificationChannel;
import com.muyategna.backend.messaging.notification.entity.NotificationTemplate;
import com.muyategna.backend.messaging.notification.entity.NotificationTemplateTranslation;
import com.muyategna.backend.messaging.notification.enums.NotificationRecipientType;
import com.muyategna.backend.messaging.notification.enums.NotificationStatus;
import com.muyategna.backend.messaging.notification.exception.NotificationSendingException;
import com.muyategna.backend.messaging.notification.mapper.NotificationMapper;
import com.muyategna.backend.messaging.notification.repository.NotificationRepository;
import com.muyategna.backend.messaging.notification.service.email.EmailRendererService;
import com.muyategna.backend.rabbitmq.RabbitUtils;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderStatistics;
import com.muyategna.backend.service_provider.repository.ServiceProviderStatisticsRepository;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.service_provider.service.ServiceProviderStatisticsService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServiceProviderNotificationServiceImpl implements ServiceProviderNotificationService {
    private final NotificationRepository notificationRepository;
    private final ServiceProviderServiceInterface serviceProviderService;
    private final NotificationTemplateService notificationTemplateService;
    private final NotificationCategoryService notificationCategoryService;
    private final NotificationChannelService notificationChannelService;
    private final EmailRendererService emailRendererService;
    private final NotificationDispatcherService notificationDispatcher;
    private final JobRequestRepository jobRequestRepository;
    private final NotificationPayloadBuilder notificationPayloadBuilder;
    private final ServiceProviderStatisticsService statisticsService;
    private final ServiceProviderStatisticsRepository statisticsRepository;
    private final ServiceProviderNotificationUtils serviceProviderNotificationUtils;
    private final JobLeadClaimService jobLeadClaimService;
    private final LanguageService languageService;

    public static final String NEW_LEAD_NOTIFICATION = "NEW_LEAD_NOTIFICATION";
    public static final String NEW_JOB_REQUEST_NOTIFICATION = "NEW_JOB_REQUEST_NOTIFICATION";
    public static final String NEW_CLAIM_NOTIFICATION = "NEW_CLAIM_NOTIFICATION";
    public static final String JOB_STATUS_CHANGE_NOTIFICATION = "JOB_STATUS_CHANGE_NOTIFICATION";
    public static final String REQUOTE_REQUEST_NOTIFICATION = "REQUOTE_REQUEST_NOTIFICATION";
    public static final String NEW_QUOTE_NOTIFICATION = "NEW_QUOTE_NOTIFICATION";
    public static final String QUOTE_STATUS_CHANGE_NOTIFICATION = "QUOTE_STATUS_CHANGE_NOTIFICATION";
    public static final String CLAIM_STATUS_CHANGE_NOTIFICATION = "CLAIM_STATUS_CHANGE_NOTIFICATION";

    @Autowired
    public ServiceProviderNotificationServiceImpl(NotificationRepository notificationRepository,
                                                  ServiceProviderServiceInterface serviceProviderService,
                                                  NotificationTemplateService notificationTemplateService,
                                                  NotificationCategoryService notificationCategoryService,
                                                  NotificationChannelService notificationChannelService,
                                                  EmailRendererService emailRendererService,
                                                  NotificationDispatcherService notificationDispatcher,
                                                  JobRequestRepository jobRequestRepository,
                                                  NotificationPayloadBuilder notificationPayloadBuilder, ServiceProviderStatisticsService statisticsService, ServiceProviderStatisticsRepository statisticsRepository, ServiceProviderNotificationUtils serviceProviderNotificationUtils, JobLeadClaimService jobLeadClaimService, LanguageService languageService) {
        this.notificationRepository = notificationRepository;
        this.serviceProviderService = serviceProviderService;
        this.notificationTemplateService = notificationTemplateService;
        this.notificationCategoryService = notificationCategoryService;
        this.notificationChannelService = notificationChannelService;
        this.emailRendererService = emailRendererService;
        this.notificationDispatcher = notificationDispatcher;
        this.jobRequestRepository = jobRequestRepository;
        this.notificationPayloadBuilder = notificationPayloadBuilder;
        this.statisticsService = statisticsService;
        this.statisticsRepository = statisticsRepository;
        this.serviceProviderNotificationUtils = serviceProviderNotificationUtils;
        this.jobLeadClaimService = jobLeadClaimService;
        this.languageService = languageService;
    }

    @RabbitListener(queues = Constants.NOTIFY_NEAR_BY_SERVICE_PROVIDERS_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyNearByServiceProvidersAboutNewLead(NewLeadNotificationQueueSPMessage notificationMessage, Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        List<Long> serviceProviderIds = notificationMessage.getServiceProviderIds();
        Long jobRequestId = notificationMessage.getJobRequestId();
        Long leadId = notificationMessage.getLeadId();

        List<ServiceProviderStatistics> statistics = statisticsService.getServiceProviderStatisticsByServiceProviderIds(serviceProviderIds);
        Map<Long, ServiceProviderStatistics> retrievedStatisticsMap = statistics.stream().collect(Collectors.toMap(stat -> stat.getProvider().getId(), Function.identity(), (a, b) -> a));

        // create new statistics if not found for any of the service providers
        Map<Long, ServiceProviderStatistics> statisticsMap = checkAndCreateMissingStatistics(serviceProviderIds, retrievedStatisticsMap);

        try {
            Optional<JobRequest> jobRequest = jobRequestRepository.findByIdWithRelatedEntities(jobRequestId);

            if (jobRequest.isEmpty()) {
                log.error("Job request with id {} not found. Notification will not be sent", jobRequestId);
                channel.basicReject(deliveryTag, false);
                return;
            }

            NotificationCategoryDto categoryDto = notificationCategoryService.getNotificationCategoryByNameAndRecipientType(NEW_LEAD_NOTIFICATION, NotificationRecipientType.SERVICE_PROVIDER);

            if (categoryDto == null) {
                log.error("[ServiceProviderNotificationService] Notification category with name {} not found. Notification will not be sent", NEW_LEAD_NOTIFICATION);
                channel.basicReject(deliveryTag, false);
                return;
            }

            List<ServiceProvider> serviceProviders = serviceProviderService.findServiceProvidersByIdsWithNotificationPreferences(serviceProviderIds);
            List<NotificationTemplate> templates = notificationTemplateService.getNotificationTemplatesWithTranslationsByNotificationCategory(categoryDto.getId());

            if (templates.isEmpty()) {
                log.error("[ServiceProviderNotificationService] No notification templates found for notification category {}", categoryDto.getName());
                channel.basicReject(deliveryTag, false);
                return;
            }

            Map<Long, NotificationTemplate> templateByChannelId = templates.stream()
                    .collect(Collectors.toMap(t -> t.getNotificationChannel().getId(), Function.identity(), (a, b) -> a));


            Map<Long, Map<Long, NotificationTemplateTranslation>> translationMap = getTemplateToTranslationMap(templates);

            List<NotificationChannel> allChannels = notificationChannelService.getNotificationChannelsByIsActive(true);

            // Build notifications to save. Save all notifications at once
            List<Notification> notificationsToSave = new ArrayList<>();
            List<ServiceProviderStatistics> statisticsToSave = new ArrayList<>();

            for (ServiceProvider provider : serviceProviders) {
                Language language = provider.getDefaultLanguage();

                List<NotificationChannel> enabledChannels = serviceProviderNotificationUtils.getEnabledNotificationChannels(provider);

                if (enabledChannels.isEmpty()) enabledChannels = allChannels;

                // Build payload for notification template
                Map<String, Object> payload = notificationPayloadBuilder.buildLeadNotificationPayload(jobRequest.get(), leadId, provider);

                for (NotificationChannel channelType : enabledChannels) {
                    NotificationTemplate template = templateByChannelId.get(channelType.getId());
                    if (template == null) {
                        log.warn("No template found for channel {} and category {}", channelType.getName(), categoryDto.getName());
                        continue;
                    }

                    NotificationTemplateTranslation translation = Optional.ofNullable(translationMap.get(template.getId()))
                            .map(map -> map.get(language.getId())).orElse(null);

                    if (translation == null) {
                        log.error("[ServiceProviderNotificationService] No translation found for notification template {}", template.getId());
                        continue;
                    }

                    NotificationDto notification = serviceProviderNotificationUtils.buildNotificationObject(provider, channelType, template, categoryDto, payload, translation);

                    try {
                        log.info("[ServiceProviderNotificationService] Sending notification for provider {}", provider.getId());
                        notificationDispatcher.sendNotification(notification);
                        notification.setStatus(NotificationStatus.SENT);
                        // Update statistics for sent notifications
                        ServiceProviderStatistics updatedStatistics = serviceProviderNotificationUtils.updateStatisticsForSentNotification(statisticsMap.get(provider.getId()));
                        statisticsToSave.add(updatedStatistics);
                    } catch (NotificationSendingException e) {
                        log.error("Rendering or dispatch failed for provider {}: {}", provider.getId(), e.getMessage());
                        notification.setStatus(NotificationStatus.FAILED);
                        notification.setErrorMessage(e.getCause().toString());
                    }

                    notificationsToSave.add(NotificationMapper.toEntity(notification));
                }
            }

            notificationRepository.saveAll(notificationsToSave);
            statisticsRepository.saveAll(statisticsToSave);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Unexpected error in notification flow", e);
            if (!RabbitUtils.isRetryable(e)) {
                try {
                    channel.basicReject(deliveryTag, false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * This method takes a list of notification templates and creates a map of the following structure
     * <p>
     * Map<Long, Map<Long, NotificationTemplateTranslation>>
     * <p>
     * Where the first key is the notification template ID and the second key is the language ID
     * and the value is the notification template translation for that language
     * <p>
     * If a notification template has no translations, it is skipped
     * <p>
     * The purpose of this method is to easily look up translations for a notification template
     * given the template ID and the language ID
     */
    @NotNull
    private static Map<Long, Map<Long, NotificationTemplateTranslation>> getTemplateToTranslationMap(List<NotificationTemplate> templates) {
        Map<Long, Map<Long, NotificationTemplateTranslation>> translationMap = new HashMap<>();
        for (NotificationTemplate template : templates) {
            if (template.getTranslations().isEmpty()) {
                log.warn("[ServiceProviderNotificationService] Notification template {} has no translations found. No notification will be sent", template.getId());
                continue;
            }
            Map<Long, NotificationTemplateTranslation> translations = template.getTranslations().stream()
                    .collect(Collectors.toMap(t -> t.getLanguage().getId(), t -> t, (a, b) -> a));
            translationMap.put(template.getId(), translations);
        }
        return translationMap;
    }


    /**
     * This method checks if statistics for a list of service providers exist and creates missing statistics if necessary.
     * <p>
     * The method takes a list of service provider IDs and a map of already retrieved statistics.
     * It returns a map containing the existing or newly created statistics for each service provider.
     * <p>
     * If statistics for a service provider ID are not present in the retrieved statistics map,
     * new statistics are created and added to the resulting map.
     *
     * @param serviceProviderIds     a list of service provider IDs to check
     * @param retrievedStatisticsMap a map of service provider statistics that have already been retrieved
     * @return a map of service provider IDs to their corresponding statistics
     */
    @NotNull
    private Map<Long, ServiceProviderStatistics> checkAndCreateMissingStatistics(List<Long> serviceProviderIds, Map<Long, ServiceProviderStatistics> retrievedStatisticsMap) {
        Map<Long, ServiceProviderStatistics> statisticsMap = new HashMap<>();
        for (Long serviceProviderId : serviceProviderIds) {
            ServiceProviderStatistics ServiceProviderStatistics = retrievedStatisticsMap.get(serviceProviderId);
            if (ServiceProviderStatistics == null) {
                ServiceProviderStatistics = statisticsService.createStatisticsWithoutSave(serviceProviderId);
            }
            statisticsMap.put(serviceProviderId, ServiceProviderStatistics);
        }
        return statisticsMap;
    }

//    @RabbitListener(queues = Constants.NOTIFY_SP_ABOUT_NEW_CLAIM_QUEUE_BASE_NAME + ".queue")
//    @Override
//    public void notifyServiceProviderAboutNewClaim(NewClaimNotificationQueueSPMessage notificationMessage, Message message, Channel channel) {
//        // Send notification to service provider about new claim
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        Long providerId = notificationMessage.getServiceProviderId();
//        Long leadClaimId = notificationMessage.getClaimId();
//
//        try {
//            NotificationCategoryDto categoryDto = notificationCategoryService.getNotificationCategoryByName(NEW_CLAIM_NOTIFICATION);
//
//            if (categoryDto == null) {
//                log.error("[ServiceProviderNotificationService] Notification category with name {} not found. Notification will not be sent: ", NEW_CLAIM_NOTIFICATION);
//                channel.basicReject(deliveryTag, false);
//                return;
//            }
//
//            JobLeadClaim jobLeadClaim = jobLeadClaimService.findByIdWithJobLeadAndJobRequest(leadClaimId);
//
//            ServiceProvider serviceProvider = serviceProviderService.getServiceProviderByIdWithNotificationPreference(providerId).orElse(null);
//            if (serviceProvider == null) {
//                log.error("[ServiceProviderNotificationService] Service provider with id {} not found. Notification will not be sent: ", providerId);
//                channel.basicReject(deliveryTag, false);
//                return;
//            }
//
//            List<NotificationTemplate> templates = notificationTemplateService.getNotificationTemplatesWithTranslationsByNotificationCategory(categoryDto.getId());
//
//            if (templates.isEmpty()) {
//                log.error("[ServiceProviderNotificationService] No notification templates found for notification category: {}", categoryDto.getName());
//                channel.basicReject(deliveryTag, false);
//                return;
//            }
//
//            Map<Long, NotificationTemplate> templateByChannelId = templates.stream()
//                    .collect(Collectors.toMap(t -> t.getNotificationChannel().getId(), Function.identity(), (a, b) -> a));
//
//
//            Map<Long, Map<Long, NotificationTemplateTranslation>> translationMap = getTemplateToTranslationMap(templates);
//
//            List<NotificationChannel> allChannels = notificationChannelService.getNotificationChannelsByIsActive(true);
//
//            Language language = serviceProvider.getDefaultLanguage();
//
//            List<NotificationChannel> enabledChannels = serviceProviderNotificationUtils.getEnabledNotificationChannels(serviceProvider);
//
//            // If no channels are enabled, use all enabled channels from db
//            if (enabledChannels.isEmpty()) enabledChannels = allChannels;
//
//
//            // Build payload for notification template
//            Map<String, Object> payload = notificationPayloadBuilder.buildNewClaimNotificationPayload(jobLeadClaim.getJobLead().getJobRequest(), jobLeadClaim, serviceProvider);
//
//            // Build notifications to save. Save all notifications at once
//            List<Notification> notificationsToSave = new ArrayList<>();
//
//            for (NotificationChannel channelType : enabledChannels) {
//                NotificationTemplate template = templateByChannelId.get(channelType.getId());
//                if (template == null) {
//                    log.warn("No template found for channel {} and category {}", channelType.getName(), categoryDto.getName());
//                    continue;
//                }
//
//                NotificationTemplateTranslation translation = Optional.ofNullable(translationMap.get(template.getId()))
//                        .map(map -> map.get(language.getId())).orElse(null);
//
//                if (translation == null) {
//                    log.error("[ServiceProviderNotificationService] No translation found for notification template {}", template.getId());
//                    continue;
//                }
//
//                NotificationDto notification = serviceProviderNotificationUtils.buildNotificationObject(serviceProvider, channelType, template, categoryDto, payload, translation);
//
//                try {
//                    log.info("[ServiceProviderNotificationService] Sending notification for provider {}", serviceProvider.getId());
//                    notificationDispatcher.sendNotification(notification);
//                    notification.setStatus(NotificationStatus.SENT);
//                } catch (NotificationSendingException e) {
//                    log.error("Rendering or dispatch failed for provider {}: {}", serviceProvider.getId(), e.getMessage());
//                    notification.setStatus(NotificationStatus.FAILED);
//                    notification.setErrorMessage(e.getCause().toString());
//                }
//
//                notificationsToSave.add(NotificationMapper.toEntity(notification));
//            }
//
//            notificationRepository.saveAll(notificationsToSave);
//            channel.basicAck(deliveryTag, false);
//
//        } catch (Exception e) {
//            log.error("Unexpected error in notification flow", e);
//            if (!RabbitUtils.isRetryable(e)) {
//                try {
//                    channel.basicReject(deliveryTag, false);
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        }
//    }


    @RabbitListener(queues = Constants.NOTIFY_SP_ABOUT_NEW_CLAIM_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyServiceProviderAboutNewClaim(NewClaimNotificationQueueSPMessage notificationMessage, Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        Long providerId = notificationMessage.getServiceProviderId();
        Long leadClaimId = notificationMessage.getClaimId();

        try {
            NotificationCategoryDto category = validateCategory(NEW_CLAIM_NOTIFICATION, deliveryTag, channel);
            if (category == null) return;

            JobLeadClaim claim = jobLeadClaimService.findByIdWithJobLeadAndJobRequest(leadClaimId);

            ServiceProvider provider = resolveServiceProvider(providerId, deliveryTag, channel);
            if (provider == null) return;

            List<NotificationTemplate> templates = resolveTemplates(category.getId(), deliveryTag, channel);
            if (templates == null) return;

            Map<Long, NotificationTemplate> templateByChannelId = templates.stream()
                    .collect(Collectors.toMap(
                            t -> t.getNotificationChannel().getId(),
                            Function.identity(),
                            (a, b) -> a
                    ));

            Map<Long, Map<Long, NotificationTemplateTranslation>> translationMap = getTemplateToTranslationMap(templates);

            List<NotificationChannel> enabledChannels = resolveChannelsForProvider(provider);

            Map<String, Object> payload = notificationPayloadBuilder.buildNewClaimNotificationPayload(
                    claim.getJobLead().getJobRequest(),
                    claim,
                    provider
            );

            List<Notification> notifications = buildAndSendNotifications(
                    provider,
                    category,
                    enabledChannels,
                    templateByChannelId,
                    translationMap,
                    payload
            );

            notificationRepository.saveAll(notifications);
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Unexpected error in notification flow", e);
            if (!RabbitUtils.isRetryable(e)) {
                try {
                    channel.basicReject(deliveryTag, false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }


    @RabbitListener(queues = Constants.NOTIFY_SP_ABOUT_JOB_STATUS_CHANGE_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyServiceProviderAboutJobStatusChange(JobStatusChangeNotificationQueueSPMessage notificationMessage, Message message, Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        Long jobRequestId = notificationMessage.getJobId();
        try {
            NotificationCategoryDto category = validateCategory(JOB_STATUS_CHANGE_NOTIFICATION, deliveryTag, channel);
            if (category == null) return;

            JobRequest jobRequest = jobRequestRepository.findByIdWithRelatedEntitiesAndClaims(jobRequestId).orElse(null);
            if (jobRequest == null) return;

            List<JobLead> JobLeads = jobRequest.getJobLeads();

            if (JobLeads.isEmpty()) return;

            List<Long> jobLeadIds = JobLeads.stream().map(JobLead::getId).toList();

            List<JobLeadClaim> jobLeadClaims = jobLeadClaimService.findAllByJobLeadIdIn(jobLeadIds);

            // Notify all service providers that have open job lead claims
            Set<Long> serviceProviderIds = extractServiceProviderIdsFromJobLeadClaims(jobLeadClaims);
            if (serviceProviderIds.isEmpty()) return;

            List<NotificationTemplate> templates = resolveTemplates(category.getId(), deliveryTag, channel);
            if (templates == null) return;

            List<Notification> allNotificationsToSave = new ArrayList<>();
            System.out.println("======================================================>>>: serviceProviderIds: " + serviceProviderIds.size());

            for (Long serviceProviderId : serviceProviderIds) {
                ServiceProvider provider = resolveServiceProvider(serviceProviderId, deliveryTag, channel);
                if (provider == null) return;

                Map<Long, NotificationTemplate> templateByChannelId = templates.stream()
                        .collect(Collectors.toMap(
                                t -> t.getNotificationChannel().getId(),
                                Function.identity(),
                                (a, b) -> a
                        ));

                Map<Long, Map<Long, NotificationTemplateTranslation>> translationMap = getTemplateToTranslationMap(templates);

                List<NotificationChannel> enabledChannels = resolveChannelsForProvider(provider);

                Map<String, Object> payload = notificationPayloadBuilder.buildJobStatusChangeNotificationPayload(
                        jobRequest,
                        provider
                );


                List<Notification> notifications = buildAndSendNotifications(
                        provider,
                        category,
                        enabledChannels,
                        templateByChannelId,
                        translationMap,
                        payload
                );

                allNotificationsToSave.addAll(notifications);
            }

//            System.out.println("=========================================================> NOTIF SIZE: " + allNotificationsToSave.size());
            notificationRepository.saveAll(allNotificationsToSave);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Unexpected error in notification flow", e);
            if (!RabbitUtils.isRetryable(e)) {
                try {
                    channel.basicReject(deliveryTag, false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @NotNull
    private static Set<Long> extractServiceProviderIdsFromJobLeadClaims(List<JobLeadClaim> jobLeadClaims) {
        return jobLeadClaims
                .stream()
                .filter(jc -> jc.isAccepted() || jc.isClaimed() || jc.isQuoted())
                .map(jc -> jc.getProvider().getId())
                .collect(Collectors.toSet());
    }

    @RabbitListener(queues = Constants.NOTIFY_SP_ABOUT_REQUOTE_REQUEST_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyServiceProviderAboutRequoteRequest(RequoteRequestNotificationQueueSPMessage notificationMessage, Message message, Channel channel) {
        // TODO:
    }

    @RabbitListener(queues = Constants.NOTIFY_SP_ABOUT_NEW_QUOTE_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyServiceProviderAboutNewQuote(NewQuoteNotificationQueueSPMessage notificationMessage, Message message, Channel channel) {
        // TODO:
    }

    @RabbitListener(queues = Constants.NOTIFY_SP_ABOUT_QUOTE_STATUS_CHANGE_QUEUE_BASE_NAME + ".queue")
    @Override
    public void notifyServiceProviderAboutQuoteStatusChange(QuoteStatusChangeNotificationQueueSPMessage notificationMessage, Message message, Channel channel) {
        // TODO:
    }

    @Override
    public void notifyServiceProviderAboutClaimStatusChange(ClaimStatusChangeNotificationQueueSPMessage notificationMessage, Message message, Channel channel) {
        // TODO:
    }


    private NotificationCategoryDto validateCategory(String categoryName, long deliveryTag, Channel channel) throws IOException {
        NotificationCategoryDto category = notificationCategoryService.getNotificationCategoryByNameAndRecipientType(categoryName, NotificationRecipientType.SERVICE_PROVIDER);
        if (category == null) {
            log.error("Notification category '{}' not found. Notification will not be sent.", categoryName);
            channel.basicReject(deliveryTag, false);
        }
        return category;
    }

    private ServiceProvider resolveServiceProvider(Long providerId, long deliveryTag, Channel channel) throws IOException {
        return serviceProviderService.getServiceProviderByIdWithNotificationPreference(providerId).orElseGet(() -> {
            log.error("Service provider with id {} not found. Notification will not be sent.", providerId);
            try {
                channel.basicReject(deliveryTag, false);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }


    private List<NotificationTemplate> resolveTemplates(Long categoryId, long deliveryTag, Channel channel) throws IOException {
        List<NotificationTemplate> templates = notificationTemplateService.getNotificationTemplatesWithTranslationsByNotificationCategory(categoryId);
        if (templates.isEmpty()) {
            log.error("No notification templates found for category {}", categoryId);
            channel.basicReject(deliveryTag, false);
            return null;
        }
        return templates;
    }

    private List<NotificationChannel> resolveChannelsForProvider(ServiceProvider provider) {
        List<NotificationChannel> allChannels = notificationChannelService.getNotificationChannelsByIsActive(true);
        List<NotificationChannel> enabledChannels = serviceProviderNotificationUtils.getEnabledNotificationChannels(provider);
        return enabledChannels.isEmpty() ? allChannels : enabledChannels;
    }

    private List<Notification> buildAndSendNotifications(
            ServiceProvider provider,
            NotificationCategoryDto category,
            List<NotificationChannel> channels,
            Map<Long, NotificationTemplate> templateByChannelId,
            Map<Long, Map<Long, NotificationTemplateTranslation>> translationMap,
            Map<String, Object> payload
    ) {
        List<Notification> notificationsToSave = new ArrayList<>();
        Language language = provider.getDefaultLanguage();

//        System.out.println("======================================================>>>: CHANNEL" + channels.size());

        for (NotificationChannel channelType : channels) {
//            System.out.println("======================================================>>>: CHANNEL");
            NotificationTemplate template = templateByChannelId.get(channelType.getId());
            if (template == null) {
                log.warn("No template found for channel {} and category {}", channelType.getName(), category.getName());
                continue;
            }

            NotificationTemplateTranslation translation = Optional.ofNullable(translationMap.get(template.getId()))
                    .map(map -> map.get(language.getId()))
                    .or(() -> Optional.ofNullable(translationMap.get(template.getId()))
                            .map(map -> map.get(languageService.getGlobalLanguage().getId())))
                    .orElse(null);

            if (translation == null) {
                log.error("No translation found for template ID {}", template.getId());
                continue;
            }

            NotificationDto notificationDto = serviceProviderNotificationUtils.buildNotificationObject(
                    provider, channelType, template, category, payload, translation
            );

            try {
                log.info("Sending notification for provider {}", provider.getId());
                notificationDispatcher.sendNotification(notificationDto);
                notificationDto.setStatus(NotificationStatus.SENT);
            } catch (NotificationSendingException e) {
                log.error("Failed to send notification to provider {}: {}", provider.getId(), e.getMessage());
                notificationDto.setStatus(NotificationStatus.FAILED);
                notificationDto.setErrorMessage(e.getCause() != null ? e.getCause().toString() : e.getMessage());
            }

            notificationsToSave.add(NotificationMapper.toEntity(notificationDto));
        }

        return notificationsToSave;
    }


}
