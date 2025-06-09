package com.muyategna.backend.request_and_lead.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/job-requests")
public class JobRequestController {

    @PostMapping
    public String createJobRequest() {
        // Logic to create a job request
        return "Job request created successfully!";
    }
}

