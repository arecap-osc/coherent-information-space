package org.hkrdi.eden.ggm.vaadin.console.security.controller;

import com.auth0.AuthenticationController;
import com.auth0.AuthorizeUrl;
import com.auth0.SessionUtils;
import org.hkrdi.eden.ggm.vaadin.console.security.SecurityConfiguration;
import org.hkrdi.eden.ggm.vaadin.console.security.TokenAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Optional;

@SuppressWarnings("unused")
@RestController
public class LoginController {

    private static final String SESSION_STATE = "com.auth0.state";

    @Autowired
    private AuthenticationController controller;

    @Autowired
    private SecurityConfiguration appConfig;
    
    @Autowired
    private RequestCache requestCache;

    @Autowired
    private CookieJwtHelper cookieJwtHelper;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<Void> login(final HttpServletRequest req, final HttpServletResponse resp) {
        SavedRequest savedRequest = requestCache.getRequest(req, resp);
        if (savedRequest != null) {
        	logger.info("Performing login for url: " + savedRequest.getRedirectUrl().toString());
        	req.getSession().setAttribute("url_prior_login", savedRequest.getRedirectUrl().toString());
        }else {
        	logger.info("Performing login");
        }
        
        Optional<Cookie> cookieToken = cookieJwtHelper.getJwtCookie(req);
        if (cookieToken.isPresent()) {
        	logger.info("Try performing login with present jwt cookie");
        	TokenAuthentication tokenAuthentication = cookieJwtHelper.getTokenAuthenticationFromJwt(cookieToken.get().getValue());
        	SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
        	if (tokenAuthentication != null) {
        		HttpHeaders headers = new HttpHeaders();
        		if (savedRequest != null) {
        			headers.setLocation(URI.create(savedRequest.getRedirectUrl().toString()));
        		}else {
        			headers.setLocation(URI.create("/coherent-space-view"));
        		}
        		return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        	}
        	logger.info("Cookie jwt invalid");
        }
        
        String redirectUri = req.getScheme() + "://" + req.getServerName();
        if ((req.getScheme().equals("http") && req.getServerPort() != 80) || (req.getScheme().equals("https") && req.getServerPort() != 443)) {
            redirectUri += ":" + req.getServerPort();
        }
        redirectUri += "/callback";
        Optional<String> auth0SessionState = Optional.ofNullable((String) SessionUtils.get(req, SESSION_STATE));
        
        logger.info("Redirecting login to auth0 with callback"+(auth0SessionState.isPresent()?" with previous auth0SessionState":""));
        AuthorizeUrl authorizeUrl = controller
                    .buildAuthorizeUrl(req, redirectUri)
                    .withAudience(String.format("https://%s/userinfo", appConfig.getDomain()))
                    .withScope("openid profile email");
        if(auth0SessionState.isPresent()) {
            authorizeUrl.withState(auth0SessionState.get());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authorizeUrl.build()));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
