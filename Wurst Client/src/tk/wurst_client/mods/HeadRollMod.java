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
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.FUN,
        description = "While this is active, other people will think you are\n" + "rolling your head around!\n" +
                "Looks a bit like nodding.",
        name = "HeadRoll")
public class HeadRollMod extends Mod implements UpdateListener {
    @Override
    public void onEnable() {
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(
                new C03PacketPlayer.C05PacketPlayerLook(Minecraft.getMinecraft().thePlayer.rotationYaw,
                        (float) Math.sin(Minecraft.getMinecraft().thePlayer.ticksExisted % 20 / 10d * Math.PI) * 90,
                        Minecraft.getMinecraft().thePlayer.onGround));
    }

    @Override
    public void onDisable() {
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
    }
}
