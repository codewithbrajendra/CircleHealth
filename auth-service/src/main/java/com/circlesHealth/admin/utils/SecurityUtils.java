package com.circlesHealth.admin.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean isUserNotValid(String userUniqueId) {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return !loggedInUser.getUsername().equalsIgnoreCase(userUniqueId);
    }

    public static String getLoggedInUserUniqueId() {
        UserDetails loggedInUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loggedInUser.getUsername();
    }

    public static Optional<String> getCurrentUserSessionId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return Optional.of((String) authentication.getCredentials());
        }
        return Optional.empty();
    }

}
