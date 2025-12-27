package org.hkrdi.eden.ggm.vaadin.console.security;

import org.hkrdi.eden.ggm.vaadin.console.security.controller.CookieJwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cop.support.BeanUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class CookieJwtAuthorizationFilter extends BasicAuthenticationFilter {

//	public static final String COOKIE_TOKEN = "token";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public CookieJwtAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		CookieJwtHelper cookieJwtHelper = BeanUtil.getBean(CookieJwtHelper.class);
		
		Optional<Cookie> cookieToken = cookieJwtHelper.getJwtCookie(req);
		
		if (!cookieToken.isPresent()) {
			if (SecurityContextHolder.getContext().getAuthentication() != null) {
				res.addHeader(HttpHeaders.SET_COOKIE, cookieJwtHelper.buildJwtCookie(
						((TokenAuthentication)SecurityContextHolder.getContext().getAuthentication()).getEncodedJwt()).toString());
				if (logger.isDebugEnabled()) {
	            	logger.info("Cookie {} set for {}",  cookieJwtHelper.getCOOKIE_TOKEN(),
	            			((TokenAuthentication)SecurityContextHolder.getContext().getAuthentication()).getName());
	            }
			}
			chain.doFilter(req, res);
			return;
		}
		
		if (SecurityContextHolder.getContext().getAuthentication() == null || 
				!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			TokenAuthentication authentication = cookieJwtHelper.getTokenAuthenticationFromJwt(cookieToken.get().getValue());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			logger.info("Authorized using jwt cookie for user {} on {}", authentication.getName(), req.getRequestURI());
		}
		chain.doFilter(req, res);
	}

//	private TokenAuthentication getAuthentication(String token) {
//		if (token != null) {
//			
//			// parse the token.
//			String tokenVerify = JWT.require(Algorithm.HMAC256(clientSecret.getBytes())).build()
//					.verify(token).getToken();
//
//			if (tokenVerify != null) {
//				TokenAuthentication tokenAuth = new TokenAuthentication(JWT.decode(tokenVerify), permissionsDefault);
//	            SecurityContextHolder.getContext().setAuthentication(tokenAuth);
//				return tokenAuth;
//			}
//			return null;
//		}
//		return null;
//	}
}
