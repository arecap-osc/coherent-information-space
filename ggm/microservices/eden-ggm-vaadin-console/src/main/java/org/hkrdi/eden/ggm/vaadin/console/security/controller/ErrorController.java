package org.hkrdi.eden.ggm.vaadin.console.security.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;

@SuppressWarnings("unused")
@RestController
public class ErrorController implements //org.springframework.boot.autoconfigure.web.ErrorController {
										org.springframework.boot.web.servlet.error.ErrorController{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PATH = "/error";

    @RequestMapping("/error")
    public ResponseEntity<?> error(HttpServletRequest request) throws IOException {
//        Enumeration<String> attrs = request.getAttributeNames();
//        String reqS = "";
//        while(attrs.hasMoreElements()) {
//        	String attrName;
//        	Object attr = request.getAttribute(attrName = attrs.nextElement());
//        	reqS += attrName+"="+attr+"; ";
//        }
                
        
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        String requestUri = (String)request.getAttribute("javax.servlet.forward.request_uri");
        String requestUri2 = (String)request.getAttribute("javax.servlet.include.request_uri");
        if (requestUri == null) {
        	requestUri = requestUri2;
        }
        logger.error("Handling error for:{uri={" + request.getRequestURL().toString()+"},stauts_code={"+statusCode+"}}");
//        System.out.println("Handling error for:{uri={" + request.getRequestURL().toString()+"},stauts_code={"+statusCode+"}}");
        
        if (statusCode != null && 
        		(statusCode.toString().startsWith("403") ||
        				statusCode.toString().startsWith("401")
        		)) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setLocation(URI.create("/login"));
	        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }else {
        	return new ResponseEntity<>("Error on " + request.getRequestURL().toString(), null, HttpStatus.valueOf(statusCode));
        }
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}