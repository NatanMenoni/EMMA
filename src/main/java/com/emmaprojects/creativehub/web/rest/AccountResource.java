package com.emmaprojects.creativehub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.emmaprojects.creativehub.domain.Authority;
import com.emmaprojects.creativehub.domain.PersistentToken;
import com.emmaprojects.creativehub.domain.User;
import com.emmaprojects.creativehub.repository.PersistentTokenRepository;
import com.emmaprojects.creativehub.repository.UserRepository;
import com.emmaprojects.creativehub.security.SecurityUtils;
import com.emmaprojects.creativehub.security.social.ExternalAccountProvider;
import com.emmaprojects.creativehub.service.MailService;
import com.emmaprojects.creativehub.service.UserService;
import com.emmaprojects.creativehub.web.rest.dto.ExternalAccountDTO;
import com.emmaprojects.creativehub.web.rest.dto.UserDTO;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.ApiException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final static String EXTERNAL_AUTH_AS_USERDTO_KEY = "AccountResource.signInAsUserDTO";

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private MailService mailService;

    @Inject
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Inject
    private UsersConnectionRepository usersConnectionRepository;

    /**
     * GET /rest/register -> get the details of an ongoing registration
     * @param request
     * @return 200 OK or 404 if there is no ongoing registration
     */
    @RequestMapping(value = "/register",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getRegisterAccount(HttpServletRequest request) {
        UserDTO dto = externalAuthAsUserDTO(request);

        if (dto != null) {
            log.debug("Returning ongoing registration request");
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * POST  /register -> register the user.
     */
    @RequestMapping(value = "/register",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request) {
        if (StringUtils.equals(request.getRemoteUser(), userDTO.getLogin()) && isSocialRegistration(request)) {
            return registerExternalAccount(userDTO, request);
        }

        return userRepository.findOneByLogin(userDTO.getLogin())
            .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
            .orElseGet(() -> {
                    if (isSocialRegistration(request)) {
                        return registerExternalAccount(userDTO, request);
                    } else {
                        return userRepository.findOneByEmail(userDTO.getEmail())
                            .map(user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
                            .orElseGet(() -> {
                                User user = userService.createUserInformation(userDTO.getLogin(), userDTO.getPassword(),
                                    userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
                                    userDTO.getLangKey());
                                String baseUrl = request.getScheme() + // "http"
                                    "://" +                                // "://"
                                    request.getServerName() +              // "myhost"
                                    ":" +                                  // ":"
                                    request.getServerPort();               // "80"

                                mailService.sendActivationEmail(user, baseUrl);
                                return new ResponseEntity<>(HttpStatus.CREATED);
                            });
                    }
                }
            );
    }

    /**
     * GET  /activate -> activate the registered user.
     */
    @RequestMapping(value = "/activate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return Optional.ofNullable(userService.activateRegistration(key))
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .map(user -> {
                return new ResponseEntity<>(
                    new UserDTO(
                        user.getLogin(),
                        null,
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getLangKey(),
                        user.getAuthorities().stream().map(Authority::getName)
                            .collect(Collectors.toList())),
                    HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account -> update the current user information.
     */
    @RequestMapping(value = "/account",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> saveAccount(@RequestBody UserDTO userDTO) {
        return userRepository
            .findOneByLogin(userDTO.getLogin())
            .filter(u -> u.getLogin().equals(SecurityUtils.getCurrentLogin()))
            .map(u -> {
                userService.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                    userDTO.getLangKey());
                return new ResponseEntity<String>(HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /change_password -> changes the current user's password
     */
    @RequestMapping(value = "/account/change_password",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /account/sessions -> get the current open sessions.
     */
    @RequestMapping(value = "/account/sessions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
            .map(user -> new ResponseEntity<>(
                persistentTokenRepository.findByUser(user),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * DELETE  /account/sessions?series={series} -> invalidate an existing session.
     *
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     *   still be able to use that session, until you quit your browser: it does not work in real time (there is
     *   no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     *   your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     *   anymore.
     *   There is an API to invalidate the current session, but there is no API to check which session uses which
     *   cookie.
     */
    @RequestMapping(value = "/account/sessions/{series}",
        method = RequestMethod.DELETE)
    @Timed
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            persistentTokenRepository.findByUser(u).stream()
                .filter(persistentToken -> StringUtils.equals(persistentToken.getSeries(), decodedSeries))
                .findAny().ifPresent(t -> persistentTokenRepository.delete(decodedSeries));
        });
    }

    @RequestMapping(value = "/account/reset_password/init",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {

        return userService.requestPasswordReset(mail)
            .map(user -> {
                String baseUrl = request.getScheme() +
                    "://" +
                    request.getServerName() +
                    ":" +
                    request.getServerPort();
                mailService.sendPasswordResetMail(user, baseUrl);
                return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
            }).orElse(new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/account/reset_password/finish",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestParam(value = "key") String key, @RequestParam(value = "newPassword") String newPassword) {
        if (!checkPasswordLength(newPassword)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(newPassword, key)
            .map(user -> new ResponseEntity<String>(HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /account/connections -> get the user's social connections.
     */
    @RequestMapping(value = "/account/connections",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<List<ExternalAccountDTO>> getUserConnections(HttpServletRequest request) {
        String login = request.getRemoteUser();
        log.debug("REST request to get User's social connections : {}", login);

        List<ExternalAccountDTO> connections = new ArrayList<>();

        MultiValueMap<String, Connection<?>> allConnections = usersConnectionRepository.createConnectionRepository(login).findAllConnections();
        for (Map.Entry<String, List<Connection<?>>> entry : allConnections.entrySet()) {
            ExternalAccountProvider externalAccountProvider = ExternalAccountProvider.caseInsensitiveValueOf(entry.getKey());
            boolean connected = !entry.getValue().isEmpty();
            DateTime expireTime = null;
            for (Connection<?> connect : entry.getValue()) {
                connected = true;
                expireTime = new DateTime(connect.createData().getExpireTime());
            }
            ExternalAccountDTO externalAccount = new ExternalAccountDTO(externalAccountProvider, connected, expireTime);
            connections.add(externalAccount);
        }

        return new ResponseEntity<>(connections,
            HttpStatus.OK);
    }

    /**
     * DELETE  /account/connections/{providerId} -> delete the user's social connection.
     */
    @RequestMapping(value = "/account/connections/{providerId}",
        method = RequestMethod.DELETE)
    @Timed
    void deleteUserConnections(HttpServletRequest request, @PathVariable String providerId) {
        String login = request.getRemoteUser();
        log.debug("REST request to delete User ({})'s social connection : {}", login, providerId);

        usersConnectionRepository.createConnectionRepository(login).removeConnections(providerId.toLowerCase());
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) && password.length() >= UserDTO.PASSWORD_MIN_LENGTH && password.length() <= UserDTO.PASSWORD_MAX_LENGTH);
    }

    /**
     * Build a new Connection to a social provider using the information from a previous
     * sign in.
     * @param attempt a possibly null ProviderSignInAttempt
     * @return null a valid Connection or null if attempt was null or a Connection could not be established
     */
    private Connection<?> buildConnection(ProviderSignInAttempt attempt) {
        if (attempt == null)
            return null;

        Connection<?> connection = attempt.getConnection(connectionFactoryLocator);

        if (!connection.test()) {
            log.warn("Social connection to {} for user '{}' failed", connection.getKey().getProviderId(), connection.getKey().getProviderUserId());
            return null;
        }
        return connection;
    }

    /**
     * Retrieve a previous external social authentication attempt as a UserDTO.
     * @param request a non-null request
     * @return a UserDTO with the firstName, lastName, email, and externalAccount details set or null if
     *  there was no previous social authentication attempt or the details of the social authentication
     *  could not be retrieved.
     *  @throws org.springframework.social.ApiException when the social API does not return the required attributes (name, email)
     *  @see org.springframework.social.connect.web.ProviderSignInAttempt
     *  @see org.springframework.social.security.SocialAuthenticationFilter#addSignInAttempt(javax.servlet.http.HttpSession, org.springframework.social.connect.Connection) SocialAuthenticationFilter#addSignInAttempt
     */
    private UserDTO externalAuthAsUserDTO(HttpServletRequest request)
    {
        UserDTO userDTO = (UserDTO) WebUtils.getSessionAttribute(request, EXTERNAL_AUTH_AS_USERDTO_KEY);
        if (userDTO == null) {
            // check if the user was successfully authenticated against an external service
            // but failed to authenticate against this application.
            ProviderSignInAttempt attempt = (ProviderSignInAttempt) WebUtils.getSessionAttribute(request, ProviderSignInAttempt.SESSION_ATTRIBUTE);
            Connection<?> connection = buildConnection(attempt);
            if (connection == null)
                return null;

            // build a new UserDTO from the external provider's version of the User
            UserProfile profile = connection.fetchUserProfile();
            String firstName = profile.getFirstName();
            String lastName = profile.getLastName();
            String email = profile.getEmail();

            // build the ExternalAccount from the ConnectionKey
            String externalAccountProviderName = connection.getKey().getProviderId();
            ExternalAccountProvider externalAccountProvider = ExternalAccountProvider.caseInsensitiveValueOf(externalAccountProviderName);
            String externalUserId = connection.getKey().getProviderUserId();
            ExternalAccountDTO externalAccount = new ExternalAccountDTO(externalAccountProvider, externalUserId);

            // check that we got the information we needed
            if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName) || StringUtils.isBlank(email))
                throw new ApiException(externalAccountProviderName, "provider failed to return required attributes");

            userDTO = new UserDTO(request.getRemoteUser(), firstName, lastName, email, externalAccount);

            // save the new UserDTO for later and clean up the HttpSession
//            request.getSession().removeAttribute(ProviderSignInAttempt.SESSION_ATTRIBUTE);
            request.getSession().setAttribute(EXTERNAL_AUTH_AS_USERDTO_KEY, userDTO);

            log.debug("Retrieved details from {} for user '{}'", externalAccountProviderName, externalUserId);
        }
        return userDTO;
    }

    /**
     * Check if the current request is associated with a social registration.
     * @param request a non-null request
     * @return true if the request is associated with a social registration
     */
    private boolean isSocialRegistration(HttpServletRequest request) {
        return
            WebUtils.getSessionAttribute(request, EXTERNAL_AUTH_AS_USERDTO_KEY) != null ||
                WebUtils.getSessionAttribute(request, ProviderSignInAttempt.SESSION_ATTRIBUTE) != null;
    }

    private ResponseEntity<String> registerExternalAccount(UserDTO currentRequestDTO, final HttpServletRequest request) {
        log.debug("Creating user from previous external authentication");

        // get the information from the social provider as a UserDTO
        UserDTO externalAuthDTO = externalAuthAsUserDTO(request);

        ProviderSignInAttempt attempt = (ProviderSignInAttempt) WebUtils.getSessionAttribute(request, ProviderSignInAttempt.SESSION_ATTRIBUTE);
        Connection<?> connection = attempt.getConnection(connectionFactoryLocator);

        // cleanup the social stuff that we've been keeping in the session
        request.getSession().removeAttribute(EXTERNAL_AUTH_AS_USERDTO_KEY);

        return userService.getUserBySocialConnection(connection)
            .map(existingUser -> {
                return new ResponseEntity<>("The external login is already linked to another User", HttpStatus.BAD_REQUEST);
            })
            .orElseGet(() -> {
                if (StringUtils.isEmpty(request.getRemoteUser())) {
                    // use the email supplied by the user, falling back on the email retrieved from the social login
                    String email;
                    if (StringUtils.isNotBlank(currentRequestDTO.getEmail())) {
                        email = currentRequestDTO.getEmail();
                    } else {
                        email = externalAuthDTO.getEmail();
                    }

                    userService.createUserInformation(currentRequestDTO.getLogin(), currentRequestDTO.getPassword(), externalAuthDTO.getFirstName(),
                        externalAuthDTO.getLastName(), email.toLowerCase(), currentRequestDTO.getLangKey(), connection);

                    return new ResponseEntity<String>(HttpStatus.CREATED);
                } else {
                    return userRepository.findOneByLogin(request.getRemoteUser()).map(existingUser -> {
                        userService.addConnection(request.getRemoteUser(), connection);
                        return new ResponseEntity<String>(HttpStatus.CREATED);
                    })
                        .orElseGet(() -> {
                            return new ResponseEntity<>("Failed to load user account", HttpStatus.INTERNAL_SERVER_ERROR);
                        });
                }
            });


    }


}

