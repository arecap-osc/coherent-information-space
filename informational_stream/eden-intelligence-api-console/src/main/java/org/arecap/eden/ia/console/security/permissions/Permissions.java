package org.arecap.eden.ia.console.security.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Permissions {

    private Map<String, List<Permission>> existingPermissions = new ConcurrentHashMap<String, List<Permission>>();
    
    public void addPermission(Permission permission){
        List<Permission> permissionList;
        if (existingPermissions.get(permission.getName()) == null){
            existingPermissions.put(permission.getName(), new ArrayList<Permission>());
        }
        permissionList = existingPermissions.get(permission.getName());
        permissionList.add(permission);
    }
    
    public List<Permission> getPermissions(String permissionName){
        return existingPermissions.get(permissionName);
    }

    public boolean isAuthorized(Object targetDomainObject, String permissionName, Object operation) {
        if (existingPermissions.get(permissionName) == null){
            return false;
        }
        
        for (Permission permission:existingPermissions.get(permissionName)){
            if (permission.isAuthorized(targetDomainObject, permissionName, operation.toString())){
                return true;
            };
        }
        return false;
    }
}
