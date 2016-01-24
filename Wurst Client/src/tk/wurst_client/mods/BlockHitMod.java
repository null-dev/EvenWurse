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
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.LeftClickListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.COMBAT,
        description = "Automatically blocks whenever you hit something with a\n" +
                "sword. Some say that you will receive less damage in PVP when doing\n" + "this.",
        name = "BlockHit")
public class BlockHitMod extends Mod implements LeftClickListener {
    @Override
    public void onEnable() {
        WurstClient.INSTANCE.events.add(LeftClickListener.class, this);
    }

    @Override
    public void onDisable() {
        WurstClient.INSTANCE.events.remove(LeftClickListener.class, this);
    }

    @Override
    public void onLeftClick() {
        ItemStack stack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();

        if (stack != null && stack.getItem() instanceof ItemSword) doBlock();
    }

    public void doBlock() {
        if (!isActive()) return;
        new Thread("BlockHit") {
            @Override
            public void run() {
                KeyBinding keybindUseItem = Minecraft.getMinecraft().gameSettings.keyBindUseItem;
                keybindUseItem.pressed = false;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                keybindUseItem.pressed = true;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                keybindUseItem.pressed = false;
            }
        }.start();
    }
}
