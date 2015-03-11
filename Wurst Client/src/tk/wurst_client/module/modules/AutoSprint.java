/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class AutoSprint extends Module
{
	
	public AutoSprint()
	{
		super(
			"AutoSprint",
			"Makes you sprint whenever you walk.",
			Category.MOVEMENT);
	}
	
	@Override
	public void oldOnUpdate()
	{
		if(!getToggled())
			return;
		if(!Minecraft.getMinecraft().thePlayer.isCollidedHorizontally
			&& Minecraft.getMinecraft().thePlayer.moveForward > 0
			&& !Minecraft.getMinecraft().thePlayer.isSneaking())
			Minecraft.getMinecraft().thePlayer.setSprinting(true);
	}
}
