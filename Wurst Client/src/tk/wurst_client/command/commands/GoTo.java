/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.util.BlockPos;
import tk.wurst_client.Client;
import tk.wurst_client.ai.PathFinder;
import tk.wurst_client.command.Command;
import tk.wurst_client.module.modules.GoToCmd;
import tk.wurst_client.utils.MiscUtils;

public class GoTo extends Command
{
	public GoTo()
	{
		super("goto",
			"Walks or flies you to a specific location.",
			"§o.goto§r <x> <y> <z>");
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		if(args == null || args.length != 3)
		{
			commandError();
			return;
		}
		for(String arg : args)
			if(!MiscUtils.isInteger(arg))
			{
				commandError();
				return;
			}
		GoToCmd.setGoal(new BlockPos(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])));
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
					Client.wurst.moduleManager.getModuleFromClass(GoToCmd.class).setToggled(true);
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
