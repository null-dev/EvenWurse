/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Glide extends Module implements UpdateListener
{
	public Glide()
	{
		super(
			"Glide",
			"Makes you fall like if you had a hang glider.",
			Category.MOVEMENT);
	}
	
	@Override
	public void onEnable()
	{
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.moduleManager.getMod(YesCheat.class)
			.getToggled())
		{
			noCheatMessage();
			setToggled(false);
		}else if(Minecraft.getMinecraft().thePlayer.motionY < 0
			&& Minecraft.getMinecraft().thePlayer.isAirBorne
			&& !Minecraft.getMinecraft().thePlayer.isInWater()
			&& !Minecraft.getMinecraft().thePlayer.isOnLadder()
			&& !Minecraft.getMinecraft().thePlayer
				.isInsideOfMaterial(Material.lava))
		{
			Minecraft.getMinecraft().thePlayer.motionY = -0.125f;
			Minecraft.getMinecraft().thePlayer.jumpMovementFactor *= 1.21337f;
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
	}
}
