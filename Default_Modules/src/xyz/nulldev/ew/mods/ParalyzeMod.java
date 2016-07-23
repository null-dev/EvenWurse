/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package xyz.nulldev.ew.mods;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Module;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.utils.EntityUtils;
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
        EntityLivingBase en = EntityUtils.getClosestEntity(true, 360, false);
        }

        @Override
        public void onDisable() {
        super.onDisable();
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
        }

        @Override
        public void onUpdate() {
        en = EntityUtils.getClosestEntity(true, 360, false);
        double xDist = Math.abs(mc.thePlayer.posX - entity.posX);
        double yDist = Math.abs(mc.thePlayer.posY - entity.posY);
	double zDist = Math.abs(mc.thePlayer.posZ - entity.posZ);
	if(Math.sqrt((Math.pow(xDist, 2) + Math.pow(zDist, 2))) < 1D && zDist < 2D)
                for (int i = 0; i < 30000; i++) {
                        Minecraft.getMinecraft().getNetHandler()
                        .addToSendQueue(new C03PacketPlayer(Minecraft.getMinecraft().thePlayer.onGround));
        }
    }
}
