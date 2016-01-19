package com.emmaprojects.creativehub.web.rest;

import com.emmaprojects.creativehub.service.SocialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;

@RestController
@RequestMapping("/social")
public class SocialController {
    private final Logger log = LoggerFactory.getLogger(SocialController.class);

    @Inject
    private SocialService socialService;

    @Inject
    private ProviderSignInUtils providerSignInUtils;

    @Inject
    private UserDetailsService userDetailsService;

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

    @RequestMapping(value = "/socialsign/{userid}", method = RequestMethod.GET)
    public RedirectView socialsign(@PathVariable String userid) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(userid);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            return new RedirectView(URIBuilder.fromUri("/#/home/")
                .queryParam("success", "true")
                .build().toString(), true);
        } catch (Exception e) {
            log.error("Exception logging social user: ", e);
            return new RedirectView(URIBuilder.fromUri("/#/social-register/no-provider")
                .queryParam("success", "false")
                .build().toString(), true);
        }

    }

}
