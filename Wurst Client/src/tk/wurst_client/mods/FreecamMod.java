/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.RENDER,
	description = "Allows you to fly out of your body.\n"
		+ "Looks similar to spectator mode.",
	name = "Freecam")
public class FreecamMod extends Mod implements UpdateListener
{
	private EntityOtherPlayerMP fakePlayer = null;
	private double oldX;
	private double oldY;
	private double oldZ;
	
	@Override
	public void onEnable()
	{
		oldX = Minecraft.getMinecraft().thePlayer.posX;
		oldY = Minecraft.getMinecraft().thePlayer.posY;
		oldZ = Minecraft.getMinecraft().thePlayer.posZ;
		fakePlayer =
			new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld,
				Minecraft.getMinecraft().thePlayer.getGameProfile());
		fakePlayer.clonePlayer(Minecraft.getMinecraft().thePlayer, true);
		fakePlayer
			.copyLocationAndAnglesFrom(Minecraft.getMinecraft().thePlayer);
		fakePlayer.rotationYawHead =
			Minecraft.getMinecraft().thePlayer.rotationYawHead;
		Minecraft.getMinecraft().theWorld.addEntityToWorld(-69, fakePlayer);
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		Minecraft.getMinecraft().thePlayer.motionX = 0;
		Minecraft.getMinecraft().thePlayer.motionY = 0;
		Minecraft.getMinecraft().thePlayer.motionZ = 0;
		Minecraft.getMinecraft().thePlayer.jumpMovementFactor =
			FlightMod.speed / 10;
		if(Minecraft.getMinecraft().gameSettings.keyBindJump.pressed)
			Minecraft.getMinecraft().thePlayer.motionY += FlightMod.speed;
		if(Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed)
			Minecraft.getMinecraft().thePlayer.motionY -= FlightMod.speed;
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
		Minecraft.getMinecraft().thePlayer.setPositionAndRotation(oldX, oldY,
			oldZ, Minecraft.getMinecraft().thePlayer.rotationYaw,
			Minecraft.getMinecraft().thePlayer.rotationPitch);
		Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-69);
		fakePlayer = null;
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
}
