package com.muyategna.backend.messaging.notification.service;

import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.messaging.notification.UrlBuilder;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationPayloadBuilder {

    private final UrlBuilder urlBuilder;

    @Autowired
    public NotificationPayloadBuilder(UrlBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    public Map<String, Object> buildLeadNotificationPayload(JobRequest jobRequest, Long leadId, ServiceProvider provider) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("address", jobRequest.getAddress().getFullAddress());
        payload.put("serviceName", jobRequest.getService().getName());
        payload.put("jobDescription", jobRequest.getDescription());
        payload.put("serviceProviderName", provider.getBusinessName());
        payload.put("jobRequestId", jobRequest.getId());
        payload.put("leadId", jobRequest.getId()); // or jobLeadId if distinct
        payload.put("leadDetailUrl", urlBuilder.buildLeadDetailsUrl(leadId));

        // Add any other relevant fields for personalization
        return payload;
    }


    public Map<String, Object> buildNewClaimNotificationPayload(JobRequest jobRequest, JobLeadClaim jobLeadClaim, ServiceProvider provider) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("address", jobRequest.getAddress().getFullAddress());
        payload.put("serviceName", jobRequest.getService().getName());
        payload.put("jobDescription", jobRequest.getDescription());
        payload.put("serviceProviderName", provider.getBusinessName());
        payload.put("jobRequestId", jobRequest.getId());
        payload.put("jobLeadClaimId", jobLeadClaim.getId());
        payload.put("status", jobLeadClaim.getStatus());
        payload.put("claimDetailUrl", urlBuilder.buildClaimDetailsUrl(jobLeadClaim.getId()));
        payload.put("jobDetailUrl", urlBuilder.buildJobDetailsUrl(jobRequest.getId()));

        // Add any other relevant fields for personalization
        return payload;
    }

    public Map<String, Object> buildJobStatusChangeNotificationPayload(JobRequest jobRequest, ServiceProvider provider) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("address", jobRequest.getAddress().getFullAddress());
        payload.put("serviceName", jobRequest.getService().getName());
        payload.put("jobDescription", jobRequest.getDescription());
        payload.put("serviceProviderName", provider.getBusinessName());
        payload.put("jobRequestId", jobRequest.getId());
        payload.put("status", jobRequest.getStatus());

        // Add any other relevant fields for personalization
        return payload;
    }
}
