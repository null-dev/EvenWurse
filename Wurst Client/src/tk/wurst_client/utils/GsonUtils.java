package tk.wurst_client.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Project: EvenWurse
 * Created: 21/12/15
 * Author: nulldev
 */
public class GsonUtils {
    static Gson GSON = null;

    public static Gson getGson() {
        if (GSON == null) {
            GSON = new GsonBuilder().setPrettyPrinting().create();
        }
        return GSON;
    }
}
