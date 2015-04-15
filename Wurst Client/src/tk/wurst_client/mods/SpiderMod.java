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

@Info(category = Category.MOVEMENT,
	description = "Allows you to climb up walls like a spider.",
	name = "Spider")
public class SpiderMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(WurstClient.INSTANCE.modManager.getModByClass(YesCheatMod.class).isEnabled())
		{
			noCheatMessage();
			setEnabled(false);
		}else if(Minecraft.getMinecraft().thePlayer.isCollidedHorizontally)
			Minecraft.getMinecraft().thePlayer.motionY = 0.2;
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
}
