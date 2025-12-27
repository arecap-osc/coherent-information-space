package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.security.permissions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Permission examples:
 *      name:resource:operations 
 *      cca:DE:c
 *      cca:DE,IT,RO:c
 *      cca:countryid=DE&storeid=7:c,r,d
 *      cca:countryid=DE,IT,RO&storeid=7:c
 * 
 * @author george.boboc
 *
 */
public class Permission {
    
    public final static String ALL_RESOURCES = "*";
    
    private String name;
    
    private String resource;
    
    private Set<Operation> operations;
    
    private List<Evaluator> evaluators;
    
    public Map<Class, Set<String>> classFieldsLocalCach = new ConcurrentHashMap<Class, Set<String>>();
    
    enum Operation{
        CREATE, READ, WRITE, DELETE, ADMIN, EXECUTE
    }
    
    public Permission(String name, String resource, String operation) {
        this.name = name;
        this.resource = resource;
        this.operations = createOperations(operation);
        evaluators = createEvaluators();
    }
    
    public Permission(String permission) {
        String parts[] = permission.split(":");
        this.name = parts[0];
        this.resource = parts[1];
        this.operations = createOperations(parts[2]);    
        evaluators = createEvaluators();
    }
    
    private Set<Operation> createOperations(String operationL){
        Set<Operation> operation = new HashSet<Operation>();
        for (int i=0; i< operationL.length(); i++){
            switch (operationL.charAt(i)){
                case 'c': operation.add(Operation.CREATE);break;
                case 'r': operation.add(Operation.READ);break;
                case 'w': operation.add(Operation.WRITE);break;
                case 'd': operation.add(Operation.DELETE);break;
                case 'a': operation.add(Operation.ADMIN);break;
                case 'e': operation.add(Operation.EXECUTE);break;
            }
        }
        return operation;
    }
    
    private List<Evaluator> createEvaluators(){
        List<Evaluator> evaluators= new ArrayList<Evaluator>();
        evaluators.add(new OperationsEvaluator());
        
        if (!resource.contains("&") && !resource.contains("=")){
                //check if resource is all
                if (ALL_RESOURCES.equals(resource)){
                    return evaluators;
                }
                if (!resource.contains(",")){
                    evaluators.add(new ForStringSingleResourceEvaluator());
                }else{
                    //for multiple values in resources
                    evaluators.add(new ForStringMultiResourceEvaluator());
                }
//            }
        } else{            
            evaluators.add(new ForComplexResourceEvaluator());
        }
        
        return evaluators;
    }
    
    private class ForComplexResourceEvaluator implements Evaluator{
        
        Map<String, Set<String>> resourcesMap;
        
        public ForComplexResourceEvaluator() {
            resourcesMap = new HashMap<String, Set<String>>();
            for(String resourcePart : resource.split("&")){
                String resourceName = resourcePart.split("=")[0];
                String resourceContent = resourcePart.split("=")[1];
                Set<String> values;
                if (!resourceContent.contains(",")){
                     values = new HashSet<String>();
                     values.add(resourceContent);
                }else{
                    values = new HashSet<String>(Arrays.asList(resourceContent.split(",")));
                }
                resourcesMap.put(resourceName, values);
            }
        }

        private Set<String> getGetters(Object o){
            Set<String> result = classFieldsLocalCach.get(o);//classFieldsLocalCach.clear();
            if (result != null){
                return result;
            }
            Set<String> fields = new HashSet<String>();
            for (Method m: o.getClass().getMethods()){
                if (m.getName().startsWith("get") && m.getParameterCount() == 0){
                    fields.add(m.getName());
                }
            }
            classFieldsLocalCach.put(o.getClass(), fields);
            return fields;
        }
        
        private Map<String, String> getFieldsValues(Object o){
            Map<String, String> entries = new HashMap<String, String>();
            for (String fieldName: getGetters(o)){
                try {
                    Method field = o.getClass().getMethod(fieldName);
                    field.setAccessible(true);
                    Object fieldValue = field.invoke(o);
                    //keep also null as "null" string
                    
                    entries.put(fieldName.substring("get".length()).toLowerCase(), ""+fieldValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return entries;
        }
        
        @Override
        public boolean isAuthorized(Object targetDomainObject, String expectedPermissionName, String expectedOperation) {
            Map<String, String> entries;
            if (!(targetDomainObject instanceof Map)){
                entries = getFieldsValues(targetDomainObject);
            }else{
                entries = ((Map<String, String>)targetDomainObject);
            }
            Set<String> verifiedResources = new HashSet<String>();
            for (Map.Entry<String, String> entry : entries.entrySet()){
                Set<String> resourceContent = resourcesMap.get(entry.getKey().toLowerCase());
                if (resourceContent == null){
                    continue;
                }else if (resourceContent.size() == 1){
                    if (resourceContent.contains(ALL_RESOURCES)){
                        verifiedResources.add(entry.getKey().toLowerCase());
                    }else{
                        if (!resourceContent.contains(entry.getValue())){
                            return false;
                        }
                        verifiedResources.add(entry.getKey().toLowerCase());
                    }
                }else{
                    if (!resourceContent.contains(entry.getValue())){
                        return false;
                    }
                    verifiedResources.add(entry.getKey().toLowerCase());
                }
            }
            if (verifiedResources.size() == resourcesMap.size()){
                return true;
            }
            return false;
        }
    }
    
    private class OperationsEvaluator implements Evaluator{
        @Override
        public boolean isAuthorized(Object targetDomainObject, String expectedPermissionName, String expectedOperation) {
            Operation operation = Operation.valueOf(expectedOperation.toUpperCase());
            if (operation == null){
                throw new RuntimeException("Unknown operation for permission:" +expectedOperation);
            }
            return operations.contains(operation);
        }
    }
    
    private class ForStringSingleResourceEvaluator implements Evaluator{
        @Override
        public boolean isAuthorized(Object targetDomainObject, String expectedPermissionName, String expectedOperation) {
            return resource.equals(targetDomainObject != null? targetDomainObject.toString() : null);
        }
    }
    
    private class ForStringMultiResourceEvaluator implements Evaluator{
        private Set<String> resourceValues;
        
        public ForStringMultiResourceEvaluator() {
            resourceValues = new HashSet<>(Arrays.asList(resource.split(",")));
        }
        
        @Override
        public boolean isAuthorized(Object targetDomainObject, String expectedPermissionName, String expectedOperation) {
            return resourceValues.contains(targetDomainObject);
        }
    }
    
    private interface Evaluator{
        boolean isAuthorized(Object targetDomainObject, String expectedPermissionName, String expectedOperation);
    }
    
    public boolean isAuthorized(Object targetDomainObject, String expectedPermissionName, String expectedOperation) {
        if (!expectedPermissionName.equals(name)){
            return false;
        }
        
        for (Evaluator evaluator: evaluators){
            if (evaluator.isAuthorized(targetDomainObject, expectedPermissionName, expectedOperation) == false){
                return false;
            }
        }
        return true;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
