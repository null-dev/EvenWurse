/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.client.Minecraft;
import tk.wurst_client.command.Command;
import tk.wurst_client.utils.MiscUtils;

public class VClip extends Command
{
	private static String[] commandHelp =
	{
		"Teleports you up/down. Can glitch you through",
		"floors & ceilings.",
		"The maximum distance is 100 blocks on vanilla servers",
		"and 10 blocks on Bukkit servers.",
		".vclip <height>"
	};
	
	public VClip()
	{
		super("vclip", commandHelp);
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		if(MiscUtils.isInteger(args[0]))
			Minecraft.getMinecraft().thePlayer.setPosition
				(
					Minecraft.getMinecraft().thePlayer.posX,
					Minecraft.getMinecraft().thePlayer.posY + Integer.valueOf(args[0]),
					Minecraft.getMinecraft().thePlayer.posZ
				);
		else
			commandError();
	}
}
