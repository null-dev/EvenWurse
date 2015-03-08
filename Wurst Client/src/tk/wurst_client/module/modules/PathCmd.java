/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.util.BlockPos;
import tk.wurst_client.ai.PathFinder;
import tk.wurst_client.ai.PathPoint;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.MiscUtils;
import tk.wurst_client.utils.RenderUtils;

public class PathCmd extends Module
{
	private PathPoint path;
	private BlockPos pos;
	
	public PathCmd()
	{
		super("Path",
			"",
			Category.HIDDEN);
	}
	
	@Override
	public String getRenderName()
	{
		if(pos != null)
			return "Path to " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
		else
			return "Path";
	}
	
	public boolean onToggledByCmd(String[] args)
	{
		path = null;
		if(getToggled())
		{
			setToggled(false);
			return true;
		}
		if(args == null || args.length != 3)
			return false;
		for(String arg : args)
			if(!MiscUtils.isInteger(arg))
				return false;
		pos = new BlockPos(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("Finding path");
				long startTime = System.nanoTime();
				path = new PathFinder(pos).find();
				System.out.println("Done after "
					+ (System.nanoTime() - startTime) / 1e6 + "ms");
			}
		}).start();
		setToggled(true);
		return true;
	}
	
	@Override
	public void onRender()
	{
		if(!getToggled())
			return;
		PathPoint path2 = path;
		while(path2 != null)
		{
			RenderUtils.blockESPBox(path2.getPos());
			path2 = path2.getPrevious();
		}
	}
}
