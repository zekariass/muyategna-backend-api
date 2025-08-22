package com.muyategna.backend.messaging.message.repository;

import com.muyategna.backend.messaging.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByThread_Id(Long threadId, Pageable pageable);
}
