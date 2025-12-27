package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.controller;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.SecurityConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.AuthenticationController;

@SuppressWarnings("unused")
@RestController
public class LoginController {

    @Autowired
    private AuthenticationController controller;

    @Autowired
    private SecurityConfiguration appConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<Void> login(final HttpServletRequest req) {
        logger.debug("Performing login");
        String redirectUri = req.getScheme() + "://" + req.getServerName();
        if ((req.getScheme().equals("http") && req.getServerPort() != 80) || (req.getScheme().equals("https") && req.getServerPort() != 443)) {
            redirectUri += ":" + req.getServerPort();
        }
        redirectUri += "/callback";
        String authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri)
                .withAudience(String.format("https://%s/userinfo", appConfig.getDomain()))
                .withScope("openid profile email")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(authorizeUrl));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

}
