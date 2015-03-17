/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mod.mods;

import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.LeftClickListener;
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.Mod.Category;
import tk.wurst_client.mod.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Automatically uses the best sword in your hotbar to attack\n"
		+ "entities.",
	name = "AutoSword")
public class AutoSword extends Mod implements LeftClickListener
{
	@Override
	public void onEnable()
	{
		EventManager.addLeftClickListener(this);
	}
	
	@Override
	public void onLeftClick()
	{	
		
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeLeftClickListener(this);
	}
}
