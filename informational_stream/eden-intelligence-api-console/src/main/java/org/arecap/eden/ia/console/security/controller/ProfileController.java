package org.arecap.eden.ia.console.security.controller;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.arecap.eden.ia.console.security.TokenAuthentication;
import org.arecap.eden.ia.console.security.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unused")
@RestController
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    protected String profile(final Authentication authentication) {

        // Since we've configured Spring Security to only allow authenticated requests to
        // reach this endpoint, and we control the Authentication implementation, we can safely cast.
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        if (tokenAuthentication == null) {
            return "redirect:/login";
        }

        String profileJson = TokenUtils.claimsAsJson(tokenAuthentication.getClaims());
        
        ObjectMapper objectMapper = new ObjectMapper();
        Claim claim = tokenAuthentication.getClaims().get("http://edenggm/claims/permissions");
        if (claim.asList(String.class) != null) {
        	String rez = "\nrezult = ";
        	for (String s:claim.asList(String.class)){
        		rez+=s+", ";
        	}
        	profileJson += rez;
        }

//        model.addAttribute("profile", tokenAuthentication.getClaims());
//        model.addAttribute("profileJson", profileJson);
        return profileJson;
    }

}
