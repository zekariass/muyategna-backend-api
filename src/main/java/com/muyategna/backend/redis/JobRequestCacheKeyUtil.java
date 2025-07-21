package com.muyategna.backend.redis;

public class JobRequestCacheKeyUtil {

    public static String getJobLeadByIdKey(Long jobLeadId) {
        return String.format("job_lead_%d", jobLeadId);
    }

    public static String getJobRequestByIdKey(Long jobRequestId) {
        return String.format("job_request_%d", jobRequestId);
    }
}
