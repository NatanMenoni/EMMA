package com.emmaprojects.creativehub.security.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

/**
 * An implementation of ConnectionSignUp that resolves the User login for a social
 * Connection by searching for a UserConnection record that matches the Connection.
 * @see com.mycompany.myapp.domain.User#getLogin()
 */
@Component("socialConnectionSignUp")
public class SocialConnectionSignUp implements ConnectionSignUp {
    private final Logger log = LoggerFactory.getLogger(SocialConnectionSignUp.class);

    @Inject
    private UsersConnectionRepository usersConnectionRepository;

    /**
     * Map a Connection to an existing User by searching for a UserConnection record that matches
     * the Connection's {@link ConnectionKey ConnectionKey}.  For example,
     * given a ConnectionKey with a providerId of "google" and a providerUserId of "12345691011",
     * search for a UserConnection record that matches and return the {@link com.mycompany.myapp.domain.User#getLogin() login}
     * associated with the account.
     * @param connection a non-null Connection
     * @return a User login if the Connection matched an existing User, null otherwise
     */
    @Transactional(readOnly = true)
    @Override
    public String execute(Connection<?> connection) {
        ConnectionKey key = connection.getKey();
        String providerName = key.getProviderId();
        ExternalAccountProvider externalProvider = ExternalAccountProvider.caseInsensitiveValueOf(providerName);
        String externalId = key.getProviderUserId();

        List<String> userIds = usersConnectionRepository.findUserIdsWithConnection(connection);
		if (userIds.isEmpty()) {
			log.debug("No internal User for external login '{}' from {}", externalId, externalProvider);
			return null;
        } else {
        	String internalLogin = userIds.iterator().next();
        	log.debug("Returning existing internal User '{}' for external login '{}' from {}", internalLogin, externalId, externalProvider);
        	return internalLogin;
        }
    }


}
