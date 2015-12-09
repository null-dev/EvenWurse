/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Allows you to freely move through blocks.\n"
		+ "A block (e.g. sand) must fall on your head to activate it.\n"
		+ "Warning: You will take damage while moving through blocks!",
	name = "NoClip")
public class NoClipMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.events.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		Minecraft.getMinecraft().thePlayer.noClip = true;
		Minecraft.getMinecraft().thePlayer.fallDistance = 0;
		Minecraft.getMinecraft().thePlayer.onGround = false;
		
		Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false;
		Minecraft.getMinecraft().thePlayer.motionX = 0;
		Minecraft.getMinecraft().thePlayer.motionY = 0;
		Minecraft.getMinecraft().thePlayer.motionZ = 0;
		
		float speed = 0.2F;
		Minecraft.getMinecraft().thePlayer.jumpMovementFactor = speed;
		if(Minecraft.getMinecraft().gameSettings.keyBindJump.pressed)
			Minecraft.getMinecraft().thePlayer.motionY += speed;
		if(Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed)
			Minecraft.getMinecraft().thePlayer.motionY -= speed;
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.removeUpdateListener(this);
		Minecraft.getMinecraft().thePlayer.noClip = false;
	}
}
