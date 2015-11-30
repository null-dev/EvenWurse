/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
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

@Mod.Info(category = Mod.Category.MOVEMENT,
	description = "Allows you to run roughly 2.5x faster than you would by\n"
		+ "sprinting and jumping. Bypasses NoCheat+.\n"
		+ "Warning: AntiCheat systems other than NoCheat+ (e.g. Hypixel\n"
		+ "AntiCheat) might still be able to block it.",
	name = "SpeedHack")
public class SpeedHackMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		// return if sneaking or not walking
		if(Minecraft.getMinecraft().thePlayer.isSneaking()
			|| (Minecraft.getMinecraft().thePlayer.moveForward == 0 && Minecraft
				.getMinecraft().thePlayer.moveStrafing == 0))
			return;
		
		// activate sprint if walking forward
		if(Minecraft.getMinecraft().thePlayer.moveForward > 0
			&& !Minecraft.getMinecraft().thePlayer.isCollidedHorizontally)
			Minecraft.getMinecraft().thePlayer.setSprinting(true);
		
		// activate mini jump if on ground
		if(Minecraft.getMinecraft().thePlayer.onGround)
		{
			Minecraft.getMinecraft().thePlayer.motionY += 0.1;
			Minecraft.getMinecraft().thePlayer.motionX *= 1.8;
			Minecraft.getMinecraft().thePlayer.motionZ *= 1.8;
			double currentSpeed =
				Math.sqrt(Math.pow(Minecraft.getMinecraft().thePlayer.motionX,
					2)
					+ Math.pow(Minecraft.getMinecraft().thePlayer.motionZ, 2));
			
			// limit speed to highest value that works on NoCheat+ version
			// 3.13.0-BETA-sMD5NET-b878
			double maxSpeed = 0.66F;
			if(currentSpeed > maxSpeed)
			{
				Minecraft.getMinecraft().thePlayer.motionX =
					Minecraft.getMinecraft().thePlayer.motionX / currentSpeed
						* maxSpeed;
				Minecraft.getMinecraft().thePlayer.motionZ =
					Minecraft.getMinecraft().thePlayer.motionZ / currentSpeed
						* maxSpeed;
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
	}
}
