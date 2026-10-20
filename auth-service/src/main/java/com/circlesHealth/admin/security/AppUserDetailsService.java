package com.circlesHealth.admin.security;

import com.circlesHealth.admin.entity.Permission;
import com.circlesHealth.admin.entity.User;
import com.circlesHealth.admin.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Component
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUserDetailsService.class);

    public AppUserDetailsService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        User user = userRepository.findByUserUniqueId(s).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with email %s does not exist", s)));
       return withUsername(user.getUserUniqueId())
                .password(user.getPassword())
                .authorities(getUserPermissions(user))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public List<Permission> getUserPermissions(User user) {
        List<Permission> permissions = new ArrayList<>();
        user.getUserRoles().forEach(userRole -> userRole.getRole().getRolePermissions()
                .forEach(rolePermission -> permissions.add(rolePermission.getPermission())));
        LOGGER.error("permission Size : " + permissions.size());
        return permissions;
    }

    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(
                    withUsername(jwtProvider.getUserSubject(jwtToken))
                            .authorities(jwtProvider.getPermissions(jwtToken))
                            .password("") // token does not have password but field may not be empty
                            .accountExpired(false)
                            .accountLocked(false)
                            .credentialsExpired(false)
                            .disabled(false)
                            .build());
        }
        return Optional.empty();
    }

    public Optional<UserDetails> loadUserByJwtTokenAndDatabase(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(loadUserByUsername(jwtProvider.getUserSubject(jwtToken)));
        } else {
            return Optional.empty();
        }
    }
}