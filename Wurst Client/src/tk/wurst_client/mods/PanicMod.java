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

@Info(category = Category.MISC,
	description = "Instantly turns off all enabled mods.\n"
		+ "Be careful with this!",
	name = "Panic")
public class PanicMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		for(Mod mod : Client.wurst.modManager.getAllMods())
			if(mod.getCategory() != Category.HIDDEN && mod.isEnabled())
				mod.setEnabled(false);
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
}
