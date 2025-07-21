package com.muyategna.backend.job_request.enums;

public enum JobLeadClaimStatus {
    CLAIMED,    // provider claimed
    QUOTED,     // provider submitted quote
    WITHDRAWN,  // provider cancelled
    ACCEPTED,   // customer accepted quote
    REJECTED,   // customer rejected quote
    EXPIRED     // auto-expired
}
