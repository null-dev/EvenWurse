/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import tk.wurst_client.Client;
import tk.wurst_client.ai.PathFinder;
import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Walks or flies you to a specific location.",
	name = "goto",
	syntax = {"<x> <y> <z>"})
public class GoToCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length != 3)
			syntaxError();
		int[] pos = argsToPos(args);
		if(Math.abs(pos[0] - Minecraft.getMinecraft().thePlayer.posX) > 256
			|| Math.abs(pos[2] - Minecraft.getMinecraft().thePlayer.posZ) > 256)
		{
			Client.wurst.chat.error("Goal is out of range!");
			Client.wurst.chat.message("Maximum range is 256 blocks.");
			return;
		}
		tk.wurst_client.mod.mods.GoToCmd.setGoal(new BlockPos(pos[0], pos[1], pos[2]));
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("Finding path");
				long startTime = System.nanoTime();
				PathFinder pathFinder = new PathFinder(tk.wurst_client.mod.mods.GoToCmd.getGoal());
				if(pathFinder.find())
				{
					tk.wurst_client.mod.mods.GoToCmd.setPath(pathFinder.formatPath());
					Client.wurst.modManager
						.getModByClass(tk.wurst_client.mod.mods.GoToCmd.class).setEnabled(true);
				}else
					Client.wurst.chat.error("Could not find a path.");
				System.out.println("Done after "
					+ (System.nanoTime() - startTime) / 1e6 + "ms");
			}
		});
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
}
