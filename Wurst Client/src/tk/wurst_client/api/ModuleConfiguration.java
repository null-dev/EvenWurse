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

    public void putString(String key, String value) {
        if(config.containsKey(key)) config.remove(key);
        config.put(key, value);
    }

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

    public boolean contains(String key) {
        return config.containsKey(key);
    }

    public HashMap<String, String> getConfig() {
        return config;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ModuleConfiguration forModule(Module module) {
        String className = module.getClass().getName();
        if(!CONFIGURATION.containsKey(className)) CONFIGURATION.put(className, new ModuleConfiguration(ModuleUtils.getModuleName(module)));
        ModuleConfiguration configuration = CONFIGURATION.get(className);
        configuration.setName(ModuleUtils.getModuleName(module));
        return configuration;
    }
}
