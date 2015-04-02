/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import tk.wurst_client.Client;
import tk.wurst_client.ai.PathFinder;
import tk.wurst_client.command.Command;
import tk.wurst_client.command.Command.Info;
import tk.wurst_client.mod.mods.GoToCmd;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Walks or flies you to a specific location.",
	name = "goto",
	syntax = {"<x> <y> <z>"})
public class GoTo extends Command
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length != 3)
			syntaxError();
		int[] pos = new int[3];
		int[] playerPos =
			new int[]{(int)Minecraft.getMinecraft().thePlayer.posX,
				(int)Minecraft.getMinecraft().thePlayer.posY,
				(int)Minecraft.getMinecraft().thePlayer.posZ};
		for(int i = 0; i < args.length; i++)
		{
			if(MiscUtils.isInteger(args[i]))
				pos[i] = Integer.parseInt(args[i]);
			else if(args[i].startsWith("~"))
				if(args[i].equals("~"))
					pos[i] = playerPos[i];
				else if(MiscUtils.isInteger(args[i].substring(1)))
					pos[i] =
						playerPos[i] + Integer.parseInt(args[i].substring(1));
				else
					syntaxError("Invalid coordinates.");
			else
				syntaxError("Invalid coordinates.");
		}
		if(Math.abs(pos[0] - Minecraft.getMinecraft().thePlayer.posX) > 256
			|| Math.abs(pos[2] - Minecraft.getMinecraft().thePlayer.posZ) > 256)
		{
			Client.wurst.chat.error("Goal is out of range!");
			Client.wurst.chat.message("Maximum range is 256 blocks.");
			return;
		}
		GoToCmd.setGoal(new BlockPos(pos[0], pos[1], pos[2]));
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("Finding path");
				long startTime = System.nanoTime();
				PathFinder pathFinder = new PathFinder(GoToCmd.getGoal());
				if(pathFinder.find())
				{
					GoToCmd.setPath(pathFinder.formatPath());
					Client.wurst.modManager
						.getModByClass(GoToCmd.class).setEnabled(true);
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
