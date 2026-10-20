package com.circlesHealth.admin.security;

import com.circlesHealth.admin.utils.constants.EndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Entry points
        http.authorizeRequests().
        antMatchers(EndPoints.BASE_PATH + "/user" + "/getpatientdetailpdf/{userId}",
        		EndPoints.BASE_PATH + "/user" + "/getPatientDetailCSV/{userId}",
        		EndPoints.BASE_PATH + "/analytics" + "/getanalyticscsvforsingleuser/{userId}/{stringStartDate}/{stringEndDate}",
        		EndPoints.BASE_PATH + "/analytics" + "/getanalyticscsvforallusers/{stringStartDate}/{stringEndDate}/{ageStart}/{ageEnd}/{gender}/{ethnicity}").permitAll().
                antMatchers(EndPoints.AUTH_PATH + "/admin-login",
                EndPoints.AUTH_PATH + "/patient-login",
                EndPoints.BASE_PATH + "user/userDetail",
                EndPoints.BASE_PATH + "/user/adminForgottenAccessCode")
                .permitAll().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**").permitAll()
                // Disallow everything else..
                .anyRequest().authenticated();

        // Disable CSRF (cross site request forgery)
        http.csrf().disable();
        http.cors();
        // avoid click jacking for web
        http.headers().contentSecurityPolicy(
                "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
                .and().referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN).and()
                .featurePolicy(
                        "geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
                .and().frameOptions().deny();

        // Add custom spring security error handling
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs").antMatchers("/swagger-resources/**").antMatchers("/swagger-ui.html")
                .antMatchers("/swagger-ui/**").antMatchers("/configuration/**").antMatchers("/webjars/**")
                .antMatchers("/public").antMatchers("/v3/api-docs/**");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // enable cors support
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedHeaders("*").
                        allowedOrigins("*").allowedMethods("*");
            }
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}