package xyz.nulldev.ew.mods;

import net.minecraft.client.Minecraft;
import tk.wurst_client.api.GUI;
import tk.wurst_client.api.Module;
import tk.wurst_client.mods.Mod;
import xyz.nulldev.ew.gui.FastToggleGUI;

/**
 * Project: EvenWurse
 * Created: 01/01/16
 * Author: nulldev
 */
@Module.ModuleInfo(version = 1.02f, minVersion = 140)
@Mod.Info(category = Mod.Category.HIDDEN,
        description = "Toggle mods at the speed of light!",
        name = "FastToggle")
public class FastToggleMod extends Mod {
    @Override
    public void onToggle() {
        if(!(Minecraft.getMinecraft().currentScreen instanceof FastToggleGUI))
            GUI.displayGuiScreen(new FastToggleGUI());
    }
}
