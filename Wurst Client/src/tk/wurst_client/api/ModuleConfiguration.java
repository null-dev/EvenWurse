package tk.wurst_client.api;

import com.google.gson.reflect.TypeToken;
import tk.wurst_client.WurstClient;
import tk.wurst_client.utils.F;
import tk.wurst_client.utils.ModuleUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: EvenWurse
 * Created: 21/12/15
 * Author: nulldev
 */
public class ModuleConfiguration {

    public static final Type TYPE = new TypeToken<Map<String, ModuleConfiguration>>(){}.getType();

    public static Map<String, ModuleConfiguration> CONFIGURATION = new HashMap<>();

    //May change so transient
    transient String name;

    public ModuleConfiguration(String name) {
        this.name = name;
    }

    HashMap<String, String> config = new HashMap<>();

    /**
     * Add a string entry into the config, if a string with this key already exists, it will be removed.
     * @param key The key of the entry to add.
     * @param value The value of the entry.
     */
    public void putString(String key, String value) {
        if(config.containsKey(key)) config.remove(key);
        config.put(key, value);
    }

    /**
     * Get a string entry from the config, the default value will be returned if it does not exist.
     * @param key The key of the entry
     * @param defaultValue The default value if the entry does not exist.
     * @return The value of the entry.
     */
    public String getString(String key, String defaultValue) {
        if(!config.containsKey(key)){
            config.put(key, defaultValue);
            return defaultValue;
        }
        return config.get(key);
    }

    void warnType(String key, String type) {
        WurstClient.INSTANCE.chat.warning(F.f("Module '<ITALIC>" + name + "</ITALIC>' requested configuration entry '<ITALIC>" + key
                + "</ITALIC>' of type <UNDERLINE>" + type + "</UNDERLINE> but value was of different type! Please confirm the value is an <UNDERLINE>" + type + "</UNDERLINE>!"));
    }

    /**
     * Similar to {@link ModuleConfiguration#getString(String, String)} except parses the value of the entry to an int.
     * It also warns the user if value is the wrong type.
     */
    public int getInt(String key, int defaultValue) {
        String s = String.valueOf(defaultValue);
        String ret = getString(key, s);
        if(!ret.equals(s)) {
            try {
                return Integer.parseInt(ret);
            } catch(NumberFormatException e) {
                warnType(key, "int");
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Similar to {@link ModuleConfiguration#getString(String, String)} except parses the value of the entry to a float.
     * It also warns the user if value is the wrong type.
     */
    public float getFloat(String key, float defaultValue) {
        String s = String.valueOf(defaultValue);
        String ret = getString(key, s);
        if(!ret.equals(s)) {
            try {
                return Float.parseFloat(ret);
            } catch(NumberFormatException e) {
                warnType(key, "float");
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Similar to {@link ModuleConfiguration#getString(String, String)} except parses the value of the entry to a boolean.
     * It also warns the user if value is the wrong type.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String s = String.valueOf(defaultValue);
        String ret = getString(key, s);
        if(!ret.equals(s)) {
            try {
                return Boolean.parseBoolean(ret);
            } catch(NumberFormatException e) {
                warnType(key, "boolean");
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Check if the module configuration contains an entry with a specified key
     * @param key The key of the entry
     * @return Whether or not an entry with this key exists.
     */
    public boolean contains(String key) {
        return config.containsKey(key);
    }

    /**
     * Get the config HashMap
     * @return The internal config HashMap
     */
    public HashMap<String, String> getConfig() {
        return config;
    }

    /**
     * Get the name of the module owning the configuration
     * @return The name of the module owning the configuration
     */
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    /**
     * Get the configuration for a module. It will be created if it does not exist.
     * @param module The module of the configuration to get
     * @return The configuration for the specified module
     */
    public static ModuleConfiguration forModule(Module module) {
        String className = module.getClass().getName();
        if(!CONFIGURATION.containsKey(className)) CONFIGURATION.put(className, new ModuleConfiguration(ModuleUtils.getModuleName(module)));
        ModuleConfiguration configuration = CONFIGURATION.get(className);
        configuration.setName(ModuleUtils.getModuleName(module));
        return configuration;
    }

    /**
     * Get the configuration for a module. It will be created if it does not exist.
     *
     * Since modules cannot share classes, this allows one module to get another's config
     *
     * @param className The classname of the module of the configuration to get
     * @return The configuration for the specified module
     */
    public static ModuleConfiguration forClassName(String className) {
        if(!CONFIGURATION.containsKey(className)) CONFIGURATION.put(className, new ModuleConfiguration(className));
        ModuleConfiguration configuration = CONFIGURATION.get(className);
        configuration.setName(className);
        return configuration;
    }
}
