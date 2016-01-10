package tk.wurst_client.mods;

import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Chat;
import tk.wurst_client.api.Module;
import tk.wurst_client.api.ModuleConfiguration;

/**
 * Project: EvenWurse
 * Created: 28/12/15
 * Author: nulldev
 */
@Module.ModuleInfo(version = 1.00f, minVersion = 136, usesConfig = true)
@Mod.Info(name = "RecordingMode",
        description = "Hides Wurst-related GUI elements for recording purposes.",
        category = Mod.Category.RENDER)
public class RecordingModeMod extends Mod {
    public boolean hideInGameGUI() {
        return ModuleConfiguration.forModule(this).getBoolean("Hide Mod List and Logo", true)
                && isActive();
    }

    public boolean hideOptionsButton() {
        return ModuleConfiguration.forModule(this).getBoolean("Hide Options Button", true)
                && isActive();
    }

    public boolean clearChat() {
        return ModuleConfiguration.forModule(this).getBoolean("Clear Chat", true)
                && isActive();
    }

    public boolean disableWurstMessages() {
        return ModuleConfiguration.forModule(this).getBoolean("Disable Wurst Messages", true)
                && isActive();
    }

    public boolean hideStealStoreButtons() {
        return ModuleConfiguration.forModule(this).getBoolean("Hide Steal/Store Buttons", true)
                && isActive();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if(clearChat()) {
//            for(int i = 0; i < 100; i++) {
//                Chat.sendClientOnlyMessage("");
//            }
            Chat.clearChat();
        }
    }

    @Override
    public void onToggle() {
        super.onToggle();
        WurstClient.INSTANCE.chat.setEnabled(!disableWurstMessages());
    }
}
