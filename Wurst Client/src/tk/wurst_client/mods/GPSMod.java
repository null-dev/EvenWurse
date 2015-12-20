package tk.wurst_client.mods;

import tk.wurst_client.WurstClient;

/**
 * Project: EvenWurse
 * Created: 19/12/15
 * Author: nulldev
 */
@Mod.Info(category = Mod.Category.RENDER,
        description = "Faster, enhanced version of .path and .goto!",
        name = "GPS")
public class GPSMod extends Mod {

    @Override
    public void onEnable() {
        WurstClient.INSTANCE.chat.info("Initializing GPS...");
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void initSliders() {
        super.initSliders();
    }

    @Override
    public void updateSettings() {
        super.updateSettings();
    }
}
