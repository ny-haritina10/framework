package session;

import java.lang.reflect.Method;

import annotation.AnnotationGetMapping;
import modelview.ModelView;

public class FormSession {

    private Method lastFormMethod;
    private Object lastControllerInstance;
    private boolean hasDisplayedErrors = false;

    public ModelView invokeLastFormMethod() 
        throws Exception 
    {
        try {
            if (lastFormMethod != null && lastControllerInstance != null) {
                if (hasDisplayedErrors) {
                    lastFormMethod.setAccessible(true);
                    ModelView mv = (ModelView) lastFormMethod.invoke(lastControllerInstance);
    
                    // Clear validation errors after rendering
                    hasDisplayedErrors = false;
                    resetStoredMethod();
    
                    return mv;
                }
            }

            throw new IllegalStateException("No stored form method found");
        } 
        
        catch (Exception e) {
            hasDisplayedErrors = true;
            throw e;
        }
    }    

    public void storeFormMethod(Method method, Object controllerInstance) {
        // Reset error flag when storing a new form method
        hasDisplayedErrors = false;
        
        if (method != null && method.isAnnotationPresent(AnnotationGetMapping.class)) {
            this.lastFormMethod = method;
            this.lastControllerInstance = controllerInstance;
        }
    }

    private void resetStoredMethod() {
        this.lastFormMethod = null;
        this.lastControllerInstance = null;
        this.hasDisplayedErrors = false;
    }
}