/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.block.Block;
import tk.wurst_client.WurstClient;
import tk.wurst_client.mods.GhostHandMod;
import tk.wurst_client.utils.MiscUtils;

@Cmd.Info(help = "Changes the settings of GhostHand or toggles it.",
        name = "ghosthand",
        syntax = {"id <block_id>", "name <block_name>"})
public class GhostHandCmd extends Cmd {
    @Override
    public void execute(String[] args) throws Error {
        if (args.length == 0) {
            WurstClient.INSTANCE.mods.getModByClass(GhostHandMod.class).toggle();
            WurstClient.INSTANCE.chat.message("GhostHand turned " +
                    (WurstClient.INSTANCE.mods.getModByClass(GhostHandMod.class).isEnabled() ? "on" : "off") + ".");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("id") && MiscUtils.isInteger(args[1])) {
                WurstClient.INSTANCE.options.ghostHandID = Integer.valueOf(args[1]);
                WurstClient.INSTANCE.files.saveOptions();
                WurstClient.INSTANCE.chat.message("GhostHand ID set to " + args[1] + ".");
            } else if (args[0].equalsIgnoreCase("name")) {
                int newID = Block.getIdFromBlock(Block.getBlockFromName(args[1]));
                if (newID == -1) {
                    WurstClient.INSTANCE.chat.message("The block \"" + args[1] + "\" could not be found.");
                    return;
                }
                WurstClient.INSTANCE.options.ghostHandID = newID;
                WurstClient.INSTANCE.files.saveOptions();
                WurstClient.INSTANCE.chat.message("GhostHand ID set to " + newID + " (" + args[1] + ").");
            } else {
                syntaxError();
            }
        } else {
            syntaxError();
        }
    }
}
