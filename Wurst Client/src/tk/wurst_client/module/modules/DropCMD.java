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

public class DropCMD extends Module
{
	public DropCMD()
	{
		super(
			"Drop",
			"",
			0,
			Category.HIDDEN);
	}

	private int timer;
	private int counter;

	@Override
	public void onEnable()
	{
		timer = 0;
		counter = 9;
	}

	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class).getToggled())
		{
			timer++;
			if(timer >= 5)
			{
				Minecraft.getMinecraft().playerController.windowClick(0, counter, 1, 4, Minecraft.getMinecraft().thePlayer);
				counter++;
				timer = 0;
				if(counter >= 45)
					setToggled(false);
			}
		}else
		{
			for(int i = 9; i < 45; i++)
				Minecraft.getMinecraft().playerController.windowClick(0, i, 1, 4, Minecraft.getMinecraft().thePlayer);
			setToggled(false);
		}
	}
}
