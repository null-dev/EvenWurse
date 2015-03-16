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
import tk.wurst_client.module.Mod;

public class MileyCyrus extends Mod implements UpdateListener
{
	private boolean shouldSneak = true;
	private float speed = 5;
	
	public MileyCyrus()
	{
		super(
			"Miley Cyrus",
			"Makes you twerk like Miley Cyrus!",
			Category.FUN);
	}
	
	@Override
	public void onEnable()
	{
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		if(hasTimePassedS(speed))
		{
			Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed =
				shouldSneak;
			shouldSneak = !shouldSneak;
			updateLastMS();
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = false;
	}
}
