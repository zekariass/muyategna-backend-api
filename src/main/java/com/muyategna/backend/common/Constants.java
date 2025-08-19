package com.muyategna.backend.common;

import java.util.List;
import java.util.Set;

/**
 * Constants class containing various constants used throughout the application.
 * <p>
 * This class is not meant to be instantiated and contains static final fields for easy access to commonly used values.
 * </p>
 *
 * @author Zekarias
 */
public final class Constants {

    public static final String LANGUAGE_LOCALE_HEADER = "X-Language-Code";
    public static final String COUNTRY_CODE_HEADER = "X-Country-Code";

    public static final List<String> PUBLIC_API_PATHS_FOR_FILTER = List.of(
            "/api/auth",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resource",
            "/webjars",
            "/docs",
            "/api/v1/secured/billing/payment/chapa/webhook",
            "/api/v1/secured/billing/payment/pesapal/IPN"
    );

    public static final List<String> PUBLIC_API_ENDPOINTS_FOR_ACCESS = List.of(
            "/",
            "/api/v1/users",
            "/api/v1/users/**",
            "/api/v1/public/common/languages/**",
            "/api/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resource/**",
            "/webjars/**",
            "/docs",
            "/api/v1/secured/billing/payment/chapa/webhook",
            "/api/v1/secured/billing/payment/pesapal/IPN"
    );


    //===================================================================================

    public static final String DEFAULT_ADMIN_PAGE_SIZE_NAME = "DEFAULT_ADMIN_PAGE_SIZE";
    public static final String DEFAULT_PUBLIC_PAGE_SIZE_NAME = "DEFAULT_PUBLIC_PAGE_SIZE";

    // FILE EXTENSIONS
    public static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "svg", "heic");
    public static final Set<String> VIDEO_EXTENSIONS = Set.of("mp4", "mkv", "mov", "avi", "wmv", "flv", "webm", "mpeg", "3gp");
    public static final Set<String> DOCUMENT_EXTENSIONS = Set.of("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "md", "csv", "rtf", "odt", "ods", "odp");
    public static final Set<String> AUDIO_EXTENSIONS = Set.of("mp3", "wav", "aac", "ogg", "flac", "m4a", "wma", "opus");
    public static final Set<String> ARCHIVE_EXTENSIONS = Set.of("zip", "rar", "7z", "tar", "gz", "bz2", "xz", "tar.gz", "tar.xz");


    //===================================================================================
    // RABBITMQ QUEUE BASE NAME

    // Job Request Queues
    public static final String JOB_REQUEST_FILE_UPLOAD_QUEUE_BASE_NAME = "job.request.file.upload";
    public static final String JOB_REQUEST_NOTIFICATION_QUEUE_BASE_NAME = "job.request.notification";

    // Job Lead Queues
    public static final String JOB_LEAD_GENERATION_QUEUE_BASE_NAME = "job.lead.generation";
    public static final String JOB_LEAD_ASSIGNMENT_QUEUE_BASE_NAME = "job.lead.assignment";
    public static final String JOB_LEAD_REASSIGNMENT_QUEUE_BASE_NAME = "job.lead.reassignment";
    public static final String JOB_LEAD_REMINDER_QUEUE_BASE_NAME = "job.lead.reminder";
    public static final String SERVICE_PROVIDERS_RANKING_QUEUE_BASE_NAME = "service.providers.ranking";

    // Notification Queues
    public static final String NOTIFY_NEAR_BY_SERVICE_PROVIDERS_QUEUE_BASE_NAME = "notify.top.n.providers";
    public static final String NOTIFY_CUSTOMER_ABOUT_NEW_JOB_REQUEST_QUEUE_BASE_NAME = "notify.customer.about.new.job.request";
    public static final String NOTIFY_SP_ABOUT_NEW_CLAIM_QUEUE_BASE_NAME = "notify.sp.about.new.claim";
    public static final String NOTIFY_CUSTOMER_ABOUT_NEW_CLAIM_QUEUE_BASE_NAME = "notify.customer.about.new.claim";
    public static final String NOTIFY_CUSTOMER_ABOUT_JOB_STATUS_CHANGE_QUEUE_BASE_NAME = "notify.customer.about.job.status.change";
    public static final String NOTIFY_SP_ABOUT_JOB_STATUS_CHANGE_QUEUE_BASE_NAME = "notify.sp.about.job.status.change";
    public static final String NOTIFY_CUSTOMER_ABOUT_REQUOTE_REQUEST_QUEUE_BASE_NAME = "notify.customer.about.requote.request";
    public static final String NOTIFY_SP_ABOUT_REQUOTE_REQUEST_QUEUE_BASE_NAME = "notify.sp.about.requote.request";
    public static final String NOTIFY_CUSTOMER_ABOUT_NEW_QUOTE_QUEUE_BASE_NAME = "notify.customer.about.new.quote";
    public static final String NOTIFY_SP_ABOUT_NEW_QUOTE_QUEUE_BASE_NAME = "notify.customer.about.new.quote";
    public static final String NOTIFY_CUSTOMER_ABOUT_QUOTE_STATUS_CHANGE_QUEUE_BASE_NAME = "notify.customer.about.quote.status.change";
    public static final String NOTIFY_SP_ABOUT_QUOTE_STATUS_CHANGE_QUEUE_BASE_NAME = "notify.sp.about.quote.status.change";
    public static final String NOTIFY_CUSTOMER_ABOUT_CLAIM_STATUS_CHANGE_QUEUE_BASE_NAME = "notify.customer.about.claim.status.change";
    public static final String NOTIFY_SP_ABOUT_CLAIM_STATUS_CHANGE_QUEUE_BASE_NAME = "notify.sp.about.claim.status.change";

    // Service Provider Queues
    public static final String SP_RANKING_FOR_NEW_LEAD_ASSIGNMENT_QUEUE_BASE_NAME = "service.provider.ranking.for.new.lead.assignment";
    public static final String SP_RANKING_FOR_LEAD_REASSIGNMENT_QUEUE_BASE_NAME = "service.provider.ranking.for.lead.reassignment";

    // Billing Queues
    public static final String BANK_TRANSFER_PROOF_UPLOAD_QUEUE_BASE_NAME = "bank.transfer.proof.upload";

    public static final long IMAGE_MAX_SIZE = 5 * 1024 * 1024;
    public static final long VIDEO_MAX_SIZE = 20 * 1024 * 1024;
    public static final long DOCUMENT_MAX_SIZE = 2 * 1024 * 1024;
    public static final long ARCHIVE_MAX_SIZE = 50 * 1024 * 1024;

    private Constants() {
        // Prevent instantiation
    }
}
