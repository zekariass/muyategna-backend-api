package com.muyategna.backend.user.repository;

import com.muyategna.backend.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByEmail(String email);

    Optional<UserProfile> findByKeycloakUserId(UUID id);
}

