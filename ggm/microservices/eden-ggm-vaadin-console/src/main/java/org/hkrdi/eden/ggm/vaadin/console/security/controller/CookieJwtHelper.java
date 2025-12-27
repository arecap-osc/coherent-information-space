package org.hkrdi.eden.ggm.vaadin.console.security.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.hkrdi.eden.ggm.vaadin.console.security.TokenAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class CookieJwtHelper {

	@Value(value = "${cookie.jwt.name:jwttoken}")
	public String COOKIE_TOKEN;
	
	@Value(value = "${cookie.jwt.domain:/}")
	private String COOKIE_DOMAIN;
	
	@Value(value = "${cookie.jwt.max.age:3600}")
	private int COOKIE_MAX_AGE;
	
	@Value(value = "${cookie.jwt.secure:false}")
	private boolean COOKIE_SECURE;
	
	@Value(value = "${cookie.jwt.httponly:false}")
	private boolean COOKIE_HTTPONLY;
	
    /**
     * This is the client secret of your auth0 application (see Settings page on auth0 dashboard)
     */
    @Value(value = "${com.auth0.clientSecret}")
    private String clientSecret;
    
    @Value("${book.permissions.default}")
    private String permissionsDefault;
	
	public HttpCookie buildJwtCookie(String idToken) {
		HttpCookie idTokenCookie = ResponseCookie.from(COOKIE_TOKEN, idToken)
//        		.domain(COOKIE_DOMAIN)
        		.maxAge(COOKIE_MAX_AGE)
//        		.secure(COOKIE_SECURE)
//        		.httpOnly(COOKIE_HTTPONLY)
        		.build();
		return idTokenCookie;
	}
	
	public HttpCookie buildExpiredJwtCookie() {
		HttpCookie idTokenCookie = ResponseCookie.from(COOKIE_TOKEN, "expired")
//        		.domain(COOKIE_DOMAIN)
        		.maxAge(0)
//        		.secure(COOKIE_SECURE)
//        		.httpOnly(COOKIE_HTTPONLY)
        		.build();
		return idTokenCookie;
	}
	
	public Optional<Cookie> getJwtCookie(HttpServletRequest req) {
		if (req == null || req.getCookies() == null) {
			return Optional.empty();
		}
		return Stream.of(req.getCookies()).filter(c->c.getName().equals(COOKIE_TOKEN)).findFirst();
	}
	
	public TokenAuthentication getTokenAuthenticationFromJwt(String token) {
		if (token != null) {
			
			// parse the token.
			String tokenVerify = JWT.require(Algorithm.HMAC256(clientSecret.getBytes())).build()
					.verify(token).getToken();

			if (tokenVerify != null) {
				TokenAuthentication tokenAuth = new TokenAuthentication(tokenVerify, JWT.decode(tokenVerify), permissionsDefault);
				return tokenAuth;
			}
			return null;
		}
		return null;
	}

	public String getCOOKIE_TOKEN() {
		return COOKIE_TOKEN;
	}

}
