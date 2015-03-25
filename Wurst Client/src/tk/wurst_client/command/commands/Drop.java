/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mod.mods.YesCheat;

public class Drop extends Command implements UpdateListener
{
	private int timer;
	private int counter;
	
	public Drop()
	{
		super("drop",
			"Drops all your items on the ground.",
			"§o.drop§r");
	}
	
	@Override
	public void execute(String input, String[] args)
	{
		if(args.length != 0)
		{
			commandError();
			return;
		}
		timer = 0;
		counter = 9;
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getModByClass(YesCheat.class)
			.isEnabled())
		{
			timer++;
			if(timer >= 5)
			{
				Minecraft.getMinecraft().playerController.windowClick(0,
					counter, 1, 4, Minecraft.getMinecraft().thePlayer);
				counter++;
				timer = 0;
				if(counter >= 45)
					EventManager.update.removeListener(this);
			}
		}else
		{
			for(int i = 9; i < 45; i++)
				Minecraft.getMinecraft().playerController.windowClick(0, i, 1,
					4, Minecraft.getMinecraft().thePlayer);
			EventManager.update.removeListener(this);
		}
	}
}
