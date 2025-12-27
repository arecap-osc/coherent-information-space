package org.arecap.eden.ia.console.boot;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.StringTokenizer;

@SpringComponent
public class LoggingServletFilter implements Filter {
    @Value("${env}")
    private String environment;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication() != null ?
                    (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal() : "";
            if (username != null && username.trim().length() > 0) {
                MDC.put("username", username);
            }
            MDC.put("environment", environment);

            HttpSession session = ((HttpServletRequest) request).getSession(false);
            if (session != null) {
                MDC.put("sessionId", session.getId());
            }

            //TODO always null ??
            String ipAddress = ((HttpServletRequest) request).getHeader("HTTP-X-FORWARDED-FOR");
            if (ipAddress == null) {
            	ipAddress = ((HttpServletRequest) request).getHeader("http_x_forwarded_for");
            } else if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            } else {
                ipAddress = new StringTokenizer(ipAddress, ",").nextToken().trim();
            }

            MDC.put("clientIp", ipAddress);
        } finally {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}