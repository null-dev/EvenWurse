/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package xyz.nulldev.ew.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Module;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.EXPLOITS,
        description = "Paralyze your enemies by walking to them.",
        name = "Paralyze")

@Module.ModuleInfo(version = 1.00f,
        minVersion = 151)
public class ParalyzeMod extends Mod implements UpdateListener {
    @Override
    public void onEnable() {
        super.onEnable();
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        for (int i = 0; i < 40001; i++) {
            Minecraft.getMinecraft().getNetHandler()
                    .addToSendQueue(new C03PacketPlayer(Minecraft.getMinecraft().thePlayer.onGround));
        }
    }
}
