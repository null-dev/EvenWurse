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
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.FUN,
	description = "Makes you twerk like Miley Cyrus!",
	name = "Miley Cyrus")
public class MileyCyrusMod extends Mod implements UpdateListener
{
	private int timer;
	
	@Override
	public void onEnable()
	{
		timer = 0;
		WurstClient.INSTANCE.events.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		timer++;
		if(timer >= 6)
		{
			Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed =
				!Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed;
			timer = 0;
		}
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
		Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = false;
	}
}
