package com.muyategna.backend.job_request.service;

public interface InviteForQuoteService {
    void inviteForQuote(Long jobRequestId, Long serviceProviderId);

    void acceptTheInviteForQuote(Long serviceProviderId, Long inviteForQuoteId);

    void viewTheInviteForQuote(Long serviceProviderId, Long inviteForQuoteId);
}
