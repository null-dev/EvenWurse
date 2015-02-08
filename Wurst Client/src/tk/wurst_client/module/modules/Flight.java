/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.lwjgl.input.Keyboard;

import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Flight extends Module
{
	public Flight()
	{
		super(
			"Flight",
			"Makes you fly.\n"
				+ "This is one of the oldest hacks in Minecraft.",
				Keyboard.KEY_G,
				Category.MOVEMENT);
	}

	public static float speed = 1F;

	@Override
	public void initSliders()
	{
		moduleSliders.add(new BasicSlider("Flight speed", speed, 0.05, 5, 0.05, ValueDisplay.DECIMAL));
	}

	@Override
	public void updateSettings()
	{
		speed = (float)moduleSliders.get(0).getValue();
	}

	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class).getToggled())
		{
			noCheatMessage();
			setToggled(false);
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
}
