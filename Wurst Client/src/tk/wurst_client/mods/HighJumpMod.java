/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Makes you jump six times higher.",
	name = "HighJump")
public class HighJumpMod extends Mod implements UpdateListener
{
	public static double jumpHeight = 0.41999998688697815D * 6;
	
	@Override
	public void onEnable()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getModByClass(YesCheatMod.class)
			.isEnabled())
		{
			noCheatMessage();
			setEnabled(false);
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
}
