package com.emmaprojects.creativehub.config.social;

import com.emmaprojects.creativehub.repository.CustomSocialUsersConnectionRepository;
import com.emmaprojects.creativehub.repository.SocialUserConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import javax.inject.Inject;

// jhipster-needle-add-social-connection-factory-import-package

/**
 * Basic Spring Social configuration.
 *
 * <p>Creates the beans necessary to manage Connections to social services and
 * link accounts from those services to internal Users.</p>
 */
@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {
    private final Logger log = LoggerFactory.getLogger(SocialConfiguration.class);

    @Inject
    private SocialUserConnectionRepository socialUserConnectionRepository;

    @Inject
    private UserDetailsService userDetailsService;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        // Google configuration
        String googleClientId = environment.getProperty("spring.social.google.clientId");
        String googleClientSecret = environment.getProperty("spring.social.google.clientSecret");
        if (googleClientId != null && googleClientSecret != null) {
            log.debug("Configuring GoogleConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new GoogleConnectionFactory(
                    googleClientId,
                    googleClientSecret
                )
            );
        } else {
            log.error("Cannot configure GoogleConnectionFactory id or secret null");
        }

        // Facebook configuration
        String facebookClientId = environment.getProperty("spring.social.facebook.clientId");
        String facebookClientSecret = environment.getProperty("spring.social.facebook.clientSecret");
        if (facebookClientId != null && facebookClientSecret != null) {
            log.debug("Configuring FacebookConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new FacebookConnectionFactory(
                    facebookClientId,
                    facebookClientSecret
                )
            );
        } else {
            log.error("Cannot configure FacebookConnectionFactory id or secret null");
        }

        // Twitter configuration
        String twitterClientId = environment.getProperty("spring.social.twitter.clientId");
        String twitterClientSecret = environment.getProperty("spring.social.twitter.clientSecret");
        if (twitterClientId != null && twitterClientSecret != null) {
            log.debug("Configuring TwitterConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                new TwitterConnectionFactory(
                    twitterClientId,
                    twitterClientSecret
                )
            );
        } else {
            log.error("Cannot configure TwitterConnectionFactory id or secret null");
        }

        // jhipster-needle-add-social-connection-factory
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new CustomSocialUsersConnectionRepository(socialUserConnectionRepository, connectionFactoryLocator);
    }

    @Bean
    public SocialUserDetailsService socialUserDetailsService(){
        return new SocialUserDetailsService() {
            @Override
            public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                return new SocialUser(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
            }
        };
    }

}
