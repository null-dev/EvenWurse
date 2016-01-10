/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Chat;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.Mod.Category;

@Info(help = "Counts the features in this release of Wurst.",
        name = "features",
        syntax = {})
public class FeaturesCmd extends Cmd {
    @Override
    public void execute(String[] args) throws Error {
        if(args.length != 0)
            syntaxError();
        WurstClient.INSTANCE.chat.message("Features in this release of Wurst:");
        int mods = WurstClient.INSTANCE.mods.countMods();
        int hiddenMods = 0;
        for(Mod mod : WurstClient.INSTANCE.mods.getAllMods())
            if(mod.getCategory() == Category.HIDDEN)
                hiddenMods++;
        WurstClient.INSTANCE.chat.message(">" + (mods - hiddenMods)
                + " mods (+" + hiddenMods + " hidden mods)");
        int commands = WurstClient.INSTANCE.commands.countCommands();
        WurstClient.INSTANCE.chat.message(">" + commands + " commands");
        WurstClient.INSTANCE.chat.message(">"
                + WurstClient.INSTANCE.keybinds.size()
                + " keybinds in your current configuration");
        int settings = 0;
        for(Mod mod : WurstClient.INSTANCE.mods.getAllMods())
            settings += mod.getSettings().size();
        Chat.sendClientWurstMessage(">" + settings + " settings");
    }
}
