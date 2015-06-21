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
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Allows you to jump in mid-air.\n"
		+ "Looks as if you had a jetpack.",
	name = "Jetpack")
public class JetpackMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		if(WurstClient.INSTANCE.modManager
			.getModByClass(FlightMod.class).isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(
				FlightMod.class).setEnabled(false);
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(WurstClient.INSTANCE.modManager.getModByClass(YesCheatMod.class)
			.isEnabled())
		{
			noCheatMessage();
			setEnabled(false);
		}else if(Minecraft.getMinecraft().gameSettings.keyBindJump.pressed)
			Minecraft.getMinecraft().thePlayer.jump();
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
	}
}
