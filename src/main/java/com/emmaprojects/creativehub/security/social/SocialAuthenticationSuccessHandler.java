package com.emmaprojects.creativehub.security.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by ignacio on 23/01/16.
 */
public class SocialAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    public static final String REDIRECT_PATH_BASE = "/#/login";
    public static final String FIELD_TOKEN = "access_token";
    public static final String FIELD_EXPIRATION_SECS = "expires_in";

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AuthorizationServerTokenServices authTokenServices;
    private final String localClientId;
    private static final String ENCODING_UTF8 = "UTF-8";

    public SocialAuthenticationSuccessHandler(AuthorizationServerTokenServices authTokenServices, String localClientId){
        this.authTokenServices = authTokenServices;
        this.localClientId = localClientId;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        log.debug("Social user authenticated: " + authentication.getPrincipal() + ", generating and sending local auth");
        OAuth2AccessToken oauth2Token = authTokenServices.createAccessToken(convertAuthentication(authentication)); //Automatically checks validity
        String redirectUrl = new StringBuilder(REDIRECT_PATH_BASE)
            .append("?").append(FIELD_TOKEN).append("=")
            .append(encode(oauth2Token.getValue()))
            .append("&").append(FIELD_EXPIRATION_SECS).append("=")
            .append(oauth2Token.getExpiresIn())
            .toString();
        log.debug("Sending redirection to " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    private OAuth2Authentication convertAuthentication(Authentication authentication) {
        OAuth2Request request = new OAuth2Request(null, localClientId, null, true, null,
            null, null, null, null);
        return new OAuth2Authentication(request,
            //Other option: new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), "N/A", authorities)
            new PreAuthenticatedAuthenticationToken(authentication.getPrincipal(), "N/A")
        );
    }

    private String encode(String in){
        String res = in;
        try{
            res = UriUtils.encode(in, ENCODING_UTF8);
        }catch(UnsupportedEncodingException e){
            log.error("ERROR: unsupported encoding: " + ENCODING_UTF8, e);
        }
        return res;
    }
}
