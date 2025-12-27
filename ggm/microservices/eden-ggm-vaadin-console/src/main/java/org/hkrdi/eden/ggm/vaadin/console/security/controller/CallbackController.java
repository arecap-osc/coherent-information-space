package org.hkrdi.eden.ggm.vaadin.console.security.controller;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import org.hkrdi.eden.ggm.vaadin.console.security.TokenAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@SuppressWarnings("unused")
@RestController
public class CallbackController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackController.class);
    
    @Autowired
    private CookieJwtHelper cookieJwtHelper;
    
    @Autowired
    private AuthenticationController controller;
    
    private final String redirectOnFail;
    private final String redirectOnSuccess;

    @Value("${book.permissions.default}")
    private String permissionsDefault;

    public CallbackController() {
        this.redirectOnFail = "/login";
        this.redirectOnSuccess = "/coherent-space-view";
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public ResponseEntity<Void> getCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
    	return callbackInternal(req, res);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> postCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        return callbackInternal(req, res);
    }

    private ResponseEntity<Void> callbackInternal(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
    	Tokens tokens = handle(req, res);
        HttpHeaders headers = new HttpHeaders();
        String username = req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "";
        if (tokens != null) {
            LOGGER.info("User {} logged in successfully", username);
            if (req.getSession().getAttribute("url_prior_login") != null) {
                headers.setLocation(URI.create(req.getSession().getAttribute("url_prior_login").toString()));
            } else {
                headers.setLocation(URI.create(this.redirectOnSuccess));
            }
            String idToken = tokens.getIdToken();
            headers.add(HttpHeaders.SET_COOKIE, cookieJwtHelper.buildJwtCookie(idToken).toString());
            if (LOGGER.isDebugEnabled()) {
            	LOGGER.info("Cookie {} set for {} on {}",  cookieJwtHelper.getCOOKIE_TOKEN(), username, req.getRequestURI());
            }
        } else {
            LOGGER.info("User {} failed to log in", username);
            headers.setLocation(URI.create(this.redirectOnFail));
        }
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    private Tokens handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Tokens tokens = controller.handle(req);
            TokenAuthentication tokenAuth = new TokenAuthentication(tokens.getIdToken(), JWT.decode(tokens.getIdToken()), permissionsDefault);
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);
            return tokens;
        } catch (AuthenticationException | IdentityVerificationException e) {
            LOGGER.error(e.getMessage(), e);
            SecurityContextHolder.clearContext();
            return null;
        }
    }
}
