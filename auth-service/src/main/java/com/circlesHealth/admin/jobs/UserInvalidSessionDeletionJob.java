package com.circlesHealth.admin.jobs;

import com.circlesHealth.admin.repo.UserSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserInvalidSessionDeletionJob {

    private final Logger logger = LoggerFactory.getLogger(UserInvalidSessionDeletionJob.class);
    private final UserSessionRepository userSessionRepository;

    public UserInvalidSessionDeletionJob(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    @Scheduled(fixedRate = 900000)
    public void deleteUserInvalidSessions() {
        logger.info("Deleting users invalid session data");
        userSessionRepository.deleteByActiveOrExpiresAtLessThan(false, LocalDateTime.now());
    }
}
