package org.arecap.eden.ia.console.security.permissions;

import org.arecap.eden.ia.console.security.TokenAuthentication;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class RbacPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        
        throw new RuntimeException("Id and Class permissions are not supperted by this application");
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        boolean hasPermission = false;

        if (authentication != null && permission instanceof String && authentication instanceof TokenAuthentication) {
            List<String> permissionsAsString = ((TokenAuthentication) authentication).readAuth0Permissions();
            Permissions permissions = new Permissions();
            for (String permissionLocal : permissionsAsString) {
                permissions.addPermission(new Permission(permissionLocal));
            }

            return permissions.isAuthorized(targetId, targetType, permission);
            
        } else {
            hasPermission = false;
        }
        return hasPermission;
    }

}
