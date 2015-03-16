/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Mod;

public class AutoSwitch extends Mod implements UpdateListener
{
	public AutoSwitch()
	{
		super(
			"AutoSwitch",
			"Switches the item in your hand all the time.\n"
				+ "Tip: Use this in combination with BuildRandom while\n"
				+ "having a lot of different colored wool blocks in your\n"
				+ "hotbar.",
			Category.MISC);
	}
	
	@Override
	public void onEnable()
	{
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().thePlayer.inventory.currentItem == 8)
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = 0;
		else
			Minecraft.getMinecraft().thePlayer.inventory.currentItem++;
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
	}
}
