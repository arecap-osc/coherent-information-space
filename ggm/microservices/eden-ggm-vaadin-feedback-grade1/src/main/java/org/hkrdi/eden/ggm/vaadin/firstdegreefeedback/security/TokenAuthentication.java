package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenAuthentication extends AbstractAuthenticationToken {

    public final static String audianceURL = "http://edenggm";
    private final DecodedJWT jwt;
    private boolean invalidated;

    public TokenAuthentication(DecodedJWT jwt) {
        super(readAllAuthorities(jwt));
        this.jwt = jwt;
    }

    private boolean hasExpired() {
        return jwt.getExpiresAt().before(new Date());
    }

    public String getProfileName(){
       return getClaims().get("name")!=null?getClaims().get("name").asString():null;
    }

    public String getProfileEmail(){
        return getClaims().get("email")!=null?getClaims().get("email").asString():null;
    }

    public String getProfilePictureUrl(){
        return getClaims().get("picture")!=null?getClaims().get("picture").asString():null;
    }

    private static List<GrantedAuthority> readAllAuthorities(DecodedJWT jwt){
        List<GrantedAuthority> cumulatedAuthorities = readAuthorities(jwt);
        cumulatedAuthorities.addAll(readAuth0Authorities(jwt));
        return cumulatedAuthorities;
    }

    private static List<GrantedAuthority> readAuthorities(DecodedJWT jwt) {
        Claim rolesClaim = jwt.getClaim("https://access.control/roles");
        if (rolesClaim.isNull()) {
            return new ArrayList<>();
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] scopes = rolesClaim.asArray(String.class);
        for (String s : scopes) {
            SimpleGrantedAuthority a = new SimpleGrantedAuthority(s);
            if (!authorities.contains(a)) {
                authorities.add(a);
            }
        }
        return authorities;
    }

    public static List<GrantedAuthority> readAuth0Authorities(DecodedJWT jwt) {
        Claim rolesClaim = jwt.getClaims().get(audianceURL+"/claims/roles");
        if (rolesClaim.isNull()) {
            return Collections.emptyList();
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] scopes = rolesClaim.asArray(String.class);
        for (String s : scopes){
            SimpleGrantedAuthority a = new SimpleGrantedAuthority(s);
            if (!authorities.contains(a)) {
                authorities.add(a);
            }
        }
        return authorities;
    }

    public List<String> readAuth0Permissions() {
        Claim rolesClaim = jwt.getClaims().get(audianceURL+"/claims/permissions");
        if (rolesClaim.isNull()) {
            return Collections.emptyList();
        }
        List<String> authorities = new ArrayList<>();
        String[] scopes = rolesClaim.asArray(String.class);
        for (String s : scopes){
            if (!authorities.contains(s)) {
                authorities.add(s);
            }
        }
        return authorities;
    }

    @Override
    public String getCredentials() {
        return jwt.getToken();
    }

    @Override
    public Object getPrincipal() {
        return jwt.getSubject();
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Create a new Authentication object to authenticate");
        }
        invalidated = true;
    }

    @Override
    public boolean isAuthenticated() {
        return !invalidated && !hasExpired();
    }

    /**
     * Gets the claims for this JWT token.
     * <br>
     * For an ID token, claims represent user profile information such as the user's name, profile, picture, etc.
     * <br>
     * @see <a href="https://auth0.com/docs/tokens/id-token">ID Token Documentation</a>
     * @return a Map containing the claims of the token.
     */
    public Map<String, Claim> getClaims() {
        return jwt.getClaims();
    }

}
