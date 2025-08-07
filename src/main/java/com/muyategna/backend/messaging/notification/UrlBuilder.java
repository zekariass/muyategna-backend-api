package com.muyategna.backend.messaging.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlBuilder {
    @Value("${frontend.base.url}")
    private String frontendUrl;

    public String buildLeadDetailsUrl(Long leadId) {
        return frontendUrl + "/lead/" + leadId;
    }

    public String buildJobDetailsUrl(Long jobId) {
        return frontendUrl + "/job/" + jobId;
    }

    public String buildClaimDetailsUrl(Long claimId) {
        return frontendUrl + "/claim/" + claimId;
    }
}
