/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class FastPlace extends Module
{
	
	public FastPlace()
	{
		super(
			"FastPlace",
			"Allows you to place blocks 5 times faster.\n"
				+ "Tip: This can speed up AutoBuild in YesCheat+ mode.",
				Keyboard.KEY_F,
				Category.BLOCKS);
	}

	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		Minecraft.getMinecraft().rightClickDelayTimer = 0;
	}
}
