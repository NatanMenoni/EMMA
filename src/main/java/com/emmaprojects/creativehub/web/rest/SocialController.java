package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.service.SocialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.*;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/social")
public class SocialController {
    private final Logger log = LoggerFactory.getLogger(SocialController.class);

    @Inject
    private SocialService socialService;

    @Inject
    private ProviderSignInUtils providerSignInUtils;

    @Inject
    private ConnectionFactoryLocator connectionFactoryLocator;

    private ConnectSupport connectSupport = new ConnectSupport(new HttpSessionSessionStrategy());

    @Inject
    private UsersConnectionRepository usersConnectionRepository;

    @Inject
    private SignInAdapter signInAdapter;

    private final MultiValueMap<Class<?>, ProviderSignInInterceptor<?>> signInInterceptors = new LinkedMultiValueMap<Class<?>, ProviderSignInInterceptor<?>>();


    private String signInUrl = "/signin";

    private String signUpUrl = "/signup";

    private String postSignInUrl = "/";


    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public RedirectView signUp(WebRequest webRequest, @CookieValue(name = "NG_TRANSLATE_LANG_KEY", required = false, defaultValue = "\"en\"") String langKey) {
        try {
            Connection<?> connection = providerSignInUtils.getConnectionFromSession(webRequest);
            socialService.createSocialUser(connection, langKey.replace("\"", ""));
            return new RedirectView(URIBuilder.fromUri("/#/social-register/" + connection.getKey().getProviderId())
                .queryParam("success", "true")
                .build().toString(), true);
        } catch (Exception e) {
            log.error("Exception creating social user: ", e);
            return new RedirectView(URIBuilder.fromUri("/#/social-register/no-provider")
                .queryParam("success", "false")
                .build().toString(), true);
        }
    }

    @RequestMapping(value = "/signin/{providerId}", method = RequestMethod.GET)
    public RedirectView signIn(@PathVariable String providerId, NativeWebRequest request) {
        System.out.printf("PASS");
        OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
        Connection<?> connection = connectSupport.completeConnection(connectionFactory, request);
        return handleSignIn(connection, connectionFactory, request);
    }


    private RedirectView handleSignIn(Connection<?> connection, ConnectionFactory<?> connectionFactory, NativeWebRequest request) {
        List<String> userIds = usersConnectionRepository.findUserIdsWithConnection(connection);
        if (userIds.size() == 0) {
            ProviderSignInAttempt signInAttempt = new ProviderSignInAttempt(connection);
            sessionStrategy.setAttribute(request, ProviderSignInAttempt.SESSION_ATTRIBUTE, signInAttempt);
            return redirect(signUpUrl);
        } else if (userIds.size() == 1) {
            usersConnectionRepository.createConnectionRepository(userIds.get(0)).updateConnection(connection);
            String originalUrl = signInAdapter.signIn(userIds.get(0), connection, request);
            postSignIn(connectionFactory, connection, (WebRequest) request);
            return originalUrl != null ? redirect(originalUrl) : redirect(postSignInUrl);
        } else {
            return redirect(URIBuilder.fromUri(signInUrl).queryParam("error", "multiple_users").build().toString());
        }
    }


    private RedirectView redirect(String url) {
        return new RedirectView(url, true);
    }


    private void postSignIn(ConnectionFactory<?> connectionFactory, Connection<?> connection, WebRequest request) {
        for (ProviderSignInInterceptor interceptor : interceptingSignInTo(connectionFactory)) {
            interceptor.postSignIn(connection, request);
        }
    }

    private List<ProviderSignInInterceptor<?>> interceptingSignInTo(ConnectionFactory<?> connectionFactory) {
        Class<?> serviceType = GenericTypeResolver.resolveTypeArgument(connectionFactory.getClass(), ConnectionFactory.class);
        List<ProviderSignInInterceptor<?>> typedInterceptors = signInInterceptors.get(serviceType);
        if (typedInterceptors == null) {
            typedInterceptors = Collections.emptyList();
        }
        return typedInterceptors;
    }

}
