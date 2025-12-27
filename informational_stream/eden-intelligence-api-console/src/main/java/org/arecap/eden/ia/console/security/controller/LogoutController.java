package org.arecap.eden.ia.console.security.controller;

import org.arecap.eden.ia.console.security.SecurityConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@SuppressWarnings("unused")
@Component
public class LogoutController implements LogoutSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutController.class);

    @Autowired
    private SecurityConfiguration appConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CookieJwtHelper cookieJwtHelper;
    
    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) {
        logger.debug("Performing logout");
        String username = req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "";
        invalidateSession(req);
        String returnTo = req.getScheme() + "://" + req.getServerName();
        if ((req.getScheme().equals("http") && req.getServerPort() != 80) || (req.getScheme().equals("https") && req.getServerPort() != 443)) {
            returnTo += ":" + req.getServerPort();
        }
        returnTo += "/";
        String logoutUrl = String.format(
                "https://%s/v2/logout?client_id=%s&returnTo=%s",
                appConfig.getDomain(),
                appConfig.getClientId(),
                returnTo);
        Optional<Cookie> cookieToken = cookieJwtHelper.getJwtCookie(req);
        if (cookieToken.isPresent()) {
        	res.setHeader(HttpHeaders.SET_COOKIE, cookieJwtHelper.buildExpiredJwtCookie().toString());
        }
        try {
            res.sendRedirect(logoutUrl);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("User {} has logged out", username);
    }

    private void invalidateSession(HttpServletRequest request) {
        if (request.getSession() != null) {
            request.getSession().invalidate();
        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
