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

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT, description = "Allows you to you fly.\n"
	+ "Bypasses NoCheat+ if YesCheat+ is enabled.", name = "Flight")
public class FlightMod extends Mod implements UpdateListener
{
	public float speed = 1F;
	
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
		if(WurstClient.INSTANCE.modManager.getModByClass(JetpackMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(JetpackMod.class)
				.setEnabled(false);
		
		if(WurstClient.INSTANCE.modManager.getModByClass(YesCheatMod.class)
			.isEnabled())
		{
			double posX = Minecraft.getMinecraft().thePlayer.posX;
			double posY = Minecraft.getMinecraft().thePlayer.posY;
			double posZ = Minecraft.getMinecraft().thePlayer.posZ;
			for(int i = 0; i < 4; i++)
			{
				Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						posX, posY + 1.01, posZ, false));
				Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						posX, posY, posZ, false));
			}
			Minecraft.getMinecraft().thePlayer.jump();
		}
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(WurstClient.INSTANCE.modManager.getModByClass(YesCheatMod.class)
			.isEnabled())
		{
			if(!Minecraft.getMinecraft().thePlayer.onGround)
				Minecraft.getMinecraft().thePlayer.motionY = -0.02;
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
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
	}
}
