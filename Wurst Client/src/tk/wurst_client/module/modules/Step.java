/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Mod;
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Allows you to step up full blocks.",
	name = "Step")
public class Step extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getModByClass(YesCheat.class)
			.isEnabled())
		{
			Minecraft.getMinecraft().thePlayer.stepHeight = 0.5F;
			if(Minecraft.getMinecraft().thePlayer.isCollidedHorizontally
				&& Minecraft.getMinecraft().thePlayer.onGround)
				Minecraft.getMinecraft().thePlayer.jump();
		}else
			Minecraft.getMinecraft().thePlayer.stepHeight = 1.0F;
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		Minecraft.getMinecraft().thePlayer.stepHeight = 0.5F;
	}
}
