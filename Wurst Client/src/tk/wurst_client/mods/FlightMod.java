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
import net.minecraft.network.play.client.C03PacketPlayer;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT, description = "Allows you to you fly.\n"
	+ "Bypasses NoCheat+ if YesCheat+ is enabled.\n"
	+ "Bypasses MAC if AntiMAC is enabled.", name = "Flight")
public class FlightMod extends Mod implements UpdateListener
{
	public float speed = 1F;
	private double startY;
	
	@Override
	public void initSliders()
	{
		sliders.add(new BasicSlider("Flight speed", speed, 0.05, 5, 0.05,
			ValueDisplay.DECIMAL));
	}
	
	@Override
	public void updateSettings()
	{
		speed = (float)sliders.get(0).getValue();
	}
	
	@Override
	public void onEnable()
	{
		if(WurstClient.INSTANCE.mods.getModByClass(JetpackMod.class)
			.isEnabled())
			WurstClient.INSTANCE.mods.getModByClass(JetpackMod.class)
				.setEnabled(false);
		
		if(WurstClient.INSTANCE.mods.getModByClass(YesCheatMod.class)
			.isActive()
			|| WurstClient.INSTANCE.mods.getModByClass(AntiMacMod.class)
				.isActive())
		{
			double startX = Minecraft.getMinecraft().thePlayer.posX;
			startY = Minecraft.getMinecraft().thePlayer.posY;
			double startZ = Minecraft.getMinecraft().thePlayer.posZ;
			for(int i = 0; i < 4; i++)
			{
				Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						startX, startY + 1.01, startZ, false));
				Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						startX, startY, startZ, false));
			}
			Minecraft.getMinecraft().thePlayer.jump();
		}
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(WurstClient.INSTANCE.mods.getModByClass(YesCheatMod.class)
			.isActive())
		{
			if(!Minecraft.getMinecraft().thePlayer.onGround)
				if(Minecraft.getMinecraft().gameSettings.keyBindJump.pressed
					&& Minecraft.getMinecraft().thePlayer.posY < startY - 1)
					Minecraft.getMinecraft().thePlayer.motionY = 0.2;
				else
					Minecraft.getMinecraft().thePlayer.motionY = -0.02;
		}else if(WurstClient.INSTANCE.mods
			.getModByClass(AntiMacMod.class).isActive())
		{
			updateMS();
			if(!Minecraft.getMinecraft().thePlayer.onGround)
				if(Minecraft.getMinecraft().gameSettings.keyBindJump.pressed
					&& hasTimePassedS(2))
				{
					Minecraft.getMinecraft().thePlayer.setPosition(
						Minecraft.getMinecraft().thePlayer.posX,
						Minecraft.getMinecraft().thePlayer.posY + 8,
						Minecraft.getMinecraft().thePlayer.posZ);
					updateLastMS();
				}else if(Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed)
					Minecraft.getMinecraft().thePlayer.motionY = -0.4;
				else
					Minecraft.getMinecraft().thePlayer.motionY = -0.02;
			Minecraft.getMinecraft().thePlayer.jumpMovementFactor = 0.04F;
		}else
		{
			Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false;
			Minecraft.getMinecraft().thePlayer.motionX = 0;
			Minecraft.getMinecraft().thePlayer.motionY = 0;
			Minecraft.getMinecraft().thePlayer.motionZ = 0;
			Minecraft.getMinecraft().thePlayer.jumpMovementFactor = speed;
			if(Minecraft.getMinecraft().gameSettings.keyBindJump.pressed)
				Minecraft.getMinecraft().thePlayer.motionY += speed;
			if(Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed)
				Minecraft.getMinecraft().thePlayer.motionY -= speed;
		}
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
	}
}
