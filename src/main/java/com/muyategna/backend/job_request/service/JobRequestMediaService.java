package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.request_media.JobRequestMediaCreateDto;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface JobRequestMediaService {
//    void uploadJobRequestFile(JobRequestMediaCreateDto mediaCreateDto);

    void uploadJobRequestFile(JobRequestMediaCreateDto mediaCreateDto, Message message, Channel channel) throws Exception;
}
