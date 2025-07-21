package com.muyategna.backend.quartz.service;

import com.muyategna.backend.quartz.entity.SchedulerConfig;
import com.muyategna.backend.quartz.enums.TriggerType;
import com.muyategna.backend.quartz.repository.SchedulerConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class JobSchedulerService {

    private final SchedulerConfigRepository schedulerConfigRepository;

    private final Scheduler quartzScheduler;

    @Autowired
    public JobSchedulerService(SchedulerConfigRepository schedulerConfigRepository, Scheduler quartzScheduler) {
        this.schedulerConfigRepository = schedulerConfigRepository;
        this.quartzScheduler = quartzScheduler;
    }


    @PostConstruct
    public void init() {
        log.info("Initializing scheduler");
        scheduleJobs();
    }

    private void scheduleJobs() {
        try {
            List<SchedulerConfig> schedulerConfigs = schedulerConfigRepository.findByIsActive(true); // Fetch only enabled jobs
            for (SchedulerConfig config : schedulerConfigs) {
                scheduleJob(config);
            }
        }catch (Exception e) {
            log.error("Error scheduling jobs", e);
        }
    }

    private void scheduleJob(SchedulerConfig config) {
        try {
            System.out.println("==========================================================>>>: "+ config.getCreatedAt().toString());

            validateSchedulerConfig(config);
            String cronExpression = config.getCronExpression();

            // Load the job class dynamically
            Class<?> rawClass = Class.forName(config.getJobClass());

            if (!Job.class.isAssignableFrom(rawClass)) {
                throw new IllegalArgumentException("Provided job class does not implement org.quartz.Job: " + config.getJobClass());
            }

            Class<? extends Job> jobClass = (Class<? extends Job>) rawClass;

            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(getJobKey(config))
                    .withDescription(config.getDescription())
                    .usingJobData(toJobDataMap(config.getJobData())) // Convert Map to JobDataMap
                    .build();

            Trigger trigger;

            if (config.getTriggerType() == TriggerType.CRON) {
                // For CRON triggers, use the cron expression directly
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(getTriggerKey(config))
                        .withSchedule(scheduleBuilder)
                        .build();
            } else if (config.getTriggerType() == TriggerType.CALENDAR) {
                // For CALENDAR triggers
                CalendarIntervalScheduleBuilder calendarScheduleBuilder = getCalendarIntervalScheduleBuilder(config);

                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(getTriggerKey(config))
                        .withSchedule(calendarScheduleBuilder)
                        .build();

            } else {
                throw new IllegalArgumentException("Unsupported trigger type: " + config.getTriggerType());
            }


            if (quartzScheduler.checkExists(jobDetail.getKey())) {
                log.warn("Job with name {} and group {} already exists in the scheduler. Skipping scheduling.", config.getJobName(), config.getJobGroup());
            }else {
                quartzScheduler.scheduleJob(jobDetail, trigger);
                log.info("Job scheduled successfully: {}", config.getJobName());
            }
        }catch (Exception e) {
            log.error("Error scheduling job: {}", config.getJobName(), e);
        }

    }

    @Scheduled(fixedRate = 30 * 60 * 1000) // Every 30 minutes
    private void updateJobs() throws Exception {
        List<SchedulerConfig> schedulerConfigs = schedulerConfigRepository.findByIsActive(true); // Fetch only enabled jobs

        for (SchedulerConfig config : schedulerConfigs) {
            validateSchedulerConfig(config);
//            TriggerKey triggerKey = TriggerKey.triggerKey(getTriggerKey(config));

            if (quartzScheduler.checkExists(getJobKey(config))) {
                if (config.getTriggerType() == TriggerType.CRON) {

                    CronTrigger oldTrigger = (CronTrigger) quartzScheduler.getTrigger(getTriggerKey(config));
                    String oldCronExpression = oldTrigger.getCronExpression();
                    if (!Objects.equals(oldCronExpression, config.getCronExpression())) {
                        log.warn("Cron expression has changed for job: {}. Updating the job.", config.getJobName());
                        updateCronJob(config);
                    }

                }else if (config.getTriggerType() == TriggerType.CALENDAR) {
                    if (config.getIntervalUnit() == null || config.getIntervalValue() == null) {
                        throw new IllegalArgumentException("Interval unit and value must be provided for calendar triggers");
                    }else if(hasCalendarIntervalChanged(config, quartzScheduler)) {
                        log.warn("Calendar interval has changed for job: {}. Updating the job.", config.getJobName());
                        updateCalendarJob(config);
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported trigger type: " + config.getTriggerType());
                }



            }else {
                log.warn("Trigger with key {} does not exist. Scheduling it as a new job.", getTriggerKey(config));
                scheduleJob(config);
            }
        }

    }


    private void updateCronJob(SchedulerConfig config) {

        TriggerKey triggerKey = getTriggerKey(config);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(config.getCronExpression()))
                .build();

        try {
            quartzScheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to reschedule job: " + config.getJobName(), e);
        }
    }

    private void updateCalendarJob(SchedulerConfig config) {
        TriggerKey triggerKey = getTriggerKey(config);
        CalendarIntervalScheduleBuilder scheduleBuilder = getCalendarIntervalScheduleBuilder(config);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder)
                .build();

        try {
            quartzScheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException("Failed to reschedule job: " + config.getJobName(), e);
        }
    }



    private JobKey getJobKey(SchedulerConfig config) {
        return JobKey.jobKey(config.getId() + "_" + sanitize(config.getJobName()), sanitize(config.getJobGroup()));
    }

    private TriggerKey getTriggerKey(SchedulerConfig config) {
        return TriggerKey.triggerKey(config.getId() + "_" + sanitize(config.getJobName()) + "_trigger", sanitize(config.getJobGroup()));
    }

    private String sanitize(String input) {
        return (input == null) ? "" : input.replaceAll("\\s+", "_");
    }

    private void validateSchedulerConfig(SchedulerConfig config) {
        if (config.getTriggerType() == TriggerType.CRON) {
            if (config.getCronExpression() == null || config.getCronExpression().isBlank()) {
                throw new IllegalArgumentException("Cron expression must be provided for CRON trigger type");
            }
        } else if (config.getTriggerType() == TriggerType.CALENDAR) {
            if (config.getIntervalUnit() == null || config.getIntervalValue() == null) {
                throw new IllegalArgumentException("Interval unit and value must be provided for CALENDAR trigger type");
            }
        } else {
            throw new IllegalArgumentException("Unsupported trigger type: " + config.getTriggerType());
        }
    }

    @NotNull
    private static CalendarIntervalScheduleBuilder getCalendarIntervalScheduleBuilder(SchedulerConfig config) {
        CalendarIntervalScheduleBuilder scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                .preserveHourOfDayAcrossDaylightSavings(true)
                .skipDayIfHourDoesNotExist(true);

        switch (config.getIntervalUnit()) {
            case SECONDS -> scheduleBuilder.withIntervalInSeconds(config.getIntervalValue());
            case MINUTES -> scheduleBuilder.withIntervalInMinutes(config.getIntervalValue());
            case HOURS   -> scheduleBuilder.withIntervalInHours(config.getIntervalValue());
            case DAYS    -> scheduleBuilder.withIntervalInDays(config.getIntervalValue());
            case WEEKS   -> scheduleBuilder.withIntervalInWeeks(config.getIntervalValue());
            case MONTHS  -> scheduleBuilder.withIntervalInMonths(config.getIntervalValue());
            case YEARS   -> scheduleBuilder.withIntervalInYears(config.getIntervalValue());
            default      -> throw new IllegalArgumentException("Unsupported interval unit");
        }
        return scheduleBuilder;
    }

    private JobDataMap toJobDataMap(Map<String, Object> jobData) {
        JobDataMap jobDataMap = new JobDataMap();
        Optional.ofNullable(jobData).ifPresent(jobDataMap::putAll);
        return jobDataMap;
    }


    public boolean hasCalendarIntervalChanged(SchedulerConfig config, Scheduler scheduler) throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey(config);
        Trigger trigger = scheduler.getTrigger(triggerKey);

        if (trigger instanceof CalendarIntervalTrigger calendarTrigger) {
            int interval = calendarTrigger.getRepeatInterval();
            String unit = calendarTrigger.getRepeatIntervalUnit().name();

            return !Objects.equals(interval, config.getIntervalValue()) ||
                    !Objects.equals(unit, config.getIntervalUnit().name());
        }

        return true; // different type → changed
    }
}

