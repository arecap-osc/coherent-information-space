package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.controller;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;

@SuppressWarnings("unused")
@RestController
public class CallbackController {

    @Autowired
    private AuthenticationController controller;
    private final String redirectOnFail;
    private final String redirectOnSuccess;

    public CallbackController() {
        this.redirectOnFail = "/login";
        this.redirectOnSuccess = "/ggm-application";
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public ResponseEntity<Void> getCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        boolean hand = handle(req, res);
        HttpHeaders headers = new HttpHeaders();
        if (hand){
            headers.setLocation(URI.create(this.redirectOnSuccess));
        }else{
            headers.setLocation(URI.create(this.redirectOnFail));
        }
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> postCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        boolean hand = handle(req, res);
        HttpHeaders headers = new HttpHeaders();
        if (hand){
            headers.setLocation(URI.create(this.redirectOnSuccess));
        }else{
            headers.setLocation(URI.create(this.redirectOnFail));
        }
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    private boolean handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Tokens tokens = controller.handle(req);
            TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokens.getIdToken()));
            SecurityContextHolder.getContext().setAuthentication(tokenAuth);
            return true;
        } catch (AuthenticationException | IdentityVerificationException e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            return false;
        }
    }

}
