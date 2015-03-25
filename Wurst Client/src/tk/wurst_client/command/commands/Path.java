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
import tk.wurst_client.ai.PathPoint;
import tk.wurst_client.command.Command;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.RenderListener;
import tk.wurst_client.utils.MiscUtils;
import tk.wurst_client.utils.RenderUtils;

public class Path extends Command implements RenderListener
{
	private PathPoint path;
	private boolean enabled;
	
	public Path()
	{
		super("path",
			"Shows the shortest path to a specific point. Useful for",
			"labyrinths and caves.",
			"§o.path§r [<x> <y> <z>]");
	}
	
	@Override
	public void execute(String input, String[] args)
	{
		path = null;
		if(enabled)
		{
			EventManager.render.removeListener(this);
			enabled = false;
			return;
		}
		if(args.length != 3)
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
		final BlockPos pos =
			new BlockPos(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
				Integer.parseInt(args[2]));
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("Finding path");
				long startTime = System.nanoTime();
				PathFinder pathFinder = new PathFinder(pos);
				if(pathFinder.find())
				{
					path = pathFinder.getRawPath();
					enabled = true;
					EventManager.render.addListener(Path.this);
				}else
					Client.wurst.chat.error("Could not find a path.");
				System.out.println("Done after "
					+ (System.nanoTime() - startTime) / 1e6 + "ms");
			}
		});
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}
	
	@Override
	public void onRender()
	{
		PathPoint path2 = path;
		while(path2 != null)
		{
			RenderUtils.blockESPBox(path2.getPos());
			path2 = path2.getPrevious();
		}
	}
}
