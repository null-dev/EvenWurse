package xyz.nulldev.ew.mods;

import tk.wurst_client.api.Module;
import tk.wurst_client.WurstClient;
import tk.wurst_client.mods.Mod;

/**
 * Project: EvenWurse
 * Created: 19/12/15
 * Author: nulldev
 */
@Module.ModuleInfo(version = 1.02f,
        minVersion = 120)
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
