/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mod.mods;

import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.Mod.Category;
import tk.wurst_client.mod.Mod.Info;

@Info(category = Category.RENDER,
	description = "Allows you to see in the dark.",
	name = "Fullbright")
public class Fullbright extends Mod implements UpdateListener
{
	public Fullbright()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(isEnabled()
			|| Client.wurst.modManager.getModByClass(XRay.class)
				.isEnabled())
		{
			if(Minecraft.getMinecraft().gameSettings.gammaSetting < 16F)
				Minecraft.getMinecraft().gameSettings.gammaSetting += 0.5F;
		}else if(Minecraft.getMinecraft().gameSettings.gammaSetting > 0.5F)
			if(Minecraft.getMinecraft().gameSettings.gammaSetting < 1F)
				Minecraft.getMinecraft().gameSettings.gammaSetting = 0.5F;
			else
				Minecraft.getMinecraft().gameSettings.gammaSetting -= 0.5F;
	}
}
