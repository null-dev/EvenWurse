/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Freecam extends Module
{
	public Freecam()
	{
		super(
			"Freecam",
			"Allows you to fly out of your body.\n"
				+ "Looks similar to spectator mode.",
			Category.RENDER);
	}
	
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
	}
	
	@Override
	public void oldOnUpdate()
	{
		if(!getToggled())
			return;
		Minecraft.getMinecraft().thePlayer.motionX = 0;
		Minecraft.getMinecraft().thePlayer.motionY = 0;
		Minecraft.getMinecraft().thePlayer.motionZ = 0;
		Minecraft.getMinecraft().thePlayer.jumpMovementFactor =
			Flight.speed / 10;
		if(Minecraft.getMinecraft().gameSettings.keyBindJump.pressed)
			Minecraft.getMinecraft().thePlayer.motionY += Flight.speed;
		if(Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed)
			Minecraft.getMinecraft().thePlayer.motionY -= Flight.speed;
	}
	
	@Override
	public void onDisable()
	{
		Minecraft.getMinecraft().thePlayer.setPositionAndRotation(oldX, oldY,
			oldZ, Minecraft.getMinecraft().thePlayer.rotationYaw,
			Minecraft.getMinecraft().thePlayer.rotationPitch);
		Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-69);
		fakePlayer = null;
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
}
