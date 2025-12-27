package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@SuppressWarnings("unused")
@RestController
public class ErrorController implements //org.springframework.boot.autoconfigure.web.ErrorController {
										org.springframework.boot.web.servlet.error.ErrorController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PATH = "/error";

    @RequestMapping("/error")
    public ResponseEntity<Void> error() throws IOException {
        logger.error("Handling error");
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/login"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}