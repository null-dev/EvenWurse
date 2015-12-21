package tk.wurst_client;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Project: EvenWurse
 * Created: 20/12/15
 * Author: nulldev
 */
public class Module {
    private float version = 1.00f;
    private int minVersion = 0;
    private int maxVersion = 0;

    public Module() {
        //Setup annotations
        if(getClass().isAnnotationPresent(ModuleInfo.class)) {
            version = getClass().getAnnotation(ModuleInfo.class).version();
            minVersion = getClass().getAnnotation(ModuleInfo.class).minVersion();
            maxVersion = getClass().getAnnotation(ModuleInfo.class).maxVersion();
        }
    }

    public float getVersion() {
        return version;
    }

    public int getMinVersion() {
        return minVersion;
    }

    public int getMaxVersion() {
        return maxVersion;
    }

    //Overridable on unload
    public void onUnload(){}

    //Overridable on load
    public void onLoad(){}

    //Mod load exceptions
    public static class ModuleLoadException extends Exception {
        public ModuleLoadException() {}

        public ModuleLoadException(String message) {super(message);}

        public ModuleLoadException(String message, Throwable cause) {super(message, cause);}

        public ModuleLoadException(Throwable cause) {super(cause);}

        public ModuleLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {super(message, cause, enableSuppression, writableStackTrace);}
    }
    public static class InvalidVersionException extends ModuleLoadException {
        String name;
        int minVersion;
        int maxVersion;

        public InvalidVersionException(String name, int minVersion, int maxVersion) {
            this.name = name;
            this.minVersion = minVersion;
            this.maxVersion = maxVersion;
        }

        public String getName() {
            return name;
        }

        public int getMinVersion() {
            return minVersion;
        }

        public int getMaxVersion() {
            return maxVersion;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ModuleInfo
    {
        float version() default 1.00f;

        int minVersion() default 0;

        int maxVersion() default Integer.MAX_VALUE;
    }
}
