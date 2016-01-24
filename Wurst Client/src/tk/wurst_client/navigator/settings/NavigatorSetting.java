package tk.wurst_client.navigator.settings;

import com.google.gson.JsonObject;
import tk.wurst_client.navigator.gui.NavigatorFeatureScreen;

/**
 * Project: EvenWurse
 * Created: 10/01/16
 * Author: nulldev
 */
public interface NavigatorSetting {
    void addToFeatureScreen(NavigatorFeatureScreen featureScreen);
    void save(JsonObject json);
    void load(JsonObject json);
}