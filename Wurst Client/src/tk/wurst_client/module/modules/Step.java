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
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Step extends Module
{
	public Step()
	{
		super(
			"Step",
			"Allows you to step up full blocks.",
			Category.MOVEMENT);
	}
	
	@Override
	public void onUpdate()
	{
		if(getToggled())
			if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class)
				.getToggled())
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
		Minecraft.getMinecraft().thePlayer.stepHeight = 0.5F;
	}
}
