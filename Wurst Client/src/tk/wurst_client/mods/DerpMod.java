/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.FUN,
	description = "While this is active, other people will think you are\n"
		+ "derping around.",
	name = "Derp")
public class DerpMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		float yaw =
			Minecraft.getMinecraft().thePlayer.rotationYaw
				+ (float)(Math.random() * 360 - 180);
		float pitch = (float)(Math.random() * 180 - 90);
		Minecraft.getMinecraft().thePlayer.sendQueue
			.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook
			(
				yaw,
				pitch,
				Minecraft.getMinecraft().thePlayer.onGround
			));
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
}
