/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.EventManager;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.RENDER,
	description = "Allows you to see in the dark.",
	name = "Fullbright")
public class FullbrightMod extends Mod implements UpdateListener
{
	public FullbrightMod()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(isEnabled()
			|| WurstClient.INSTANCE.modManager.getModByClass(XRayMod.class).isEnabled())
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
