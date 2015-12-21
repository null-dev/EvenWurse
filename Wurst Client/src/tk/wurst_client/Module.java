package tk.wurst_client;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Project: EvenWurse
 * Created: 20/12/15
 * Author: nulldev
 */
public abstract class Module {
    private float version = 1.00f;
    private int minVersion = 0;

    public Module() {
        //Setup annotations
        if(getClass().isAnnotationPresent(ModuleInfo.class)) {
            version = getClass().getAnnotation(ModuleInfo.class).version();
            minVersion = getClass().getAnnotation(ModuleInfo.class).minVersion();
        }
    }

    public float getVersion() {
        return version;
    }

    public int getMinVersion() {
        return minVersion;
    }

    //Overridable on unload
    public void onUnload(){}

    //Mod load exceptions
    public static class ModuleLoadException extends Exception {
        public ModuleLoadException() {}

        public ModuleLoadException(String message) {super(message);}

        public ModuleLoadException(String message, Throwable cause) {super(message, cause);}

        public ModuleLoadException(Throwable cause) {super(cause);}

        public ModuleLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {super(message, cause, enableSuppression, writableStackTrace);}
    }
    public static class OutdatedClientException extends ModuleLoadException {
        String modName;
        int minVersion;
        int curVersion;

        public OutdatedClientException(String modName, int minVersion, int curVersion) {
            this.modName = modName;
            this.minVersion = minVersion;
            this.curVersion = curVersion;
        }

        public String getModName() {
            return modName;
        }

        public int getMinVersion() {
            return minVersion;
        }

        public int getCurVersion() {
            return curVersion;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ModuleInfo
    {
        float version() default 1.00f;

        int minVersion() default 0;
    }
}
