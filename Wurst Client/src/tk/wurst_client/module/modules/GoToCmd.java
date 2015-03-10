/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import java.util.ArrayList;

import net.minecraft.util.BlockPos;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class GoToCmd extends Module
{
	private static ArrayList<BlockPos> path;
	private static BlockPos goal;
	private int index;
	
	public GoToCmd()
	{
		super("GoTo", "", Category.HIDDEN);
	}
	
	@Override
	public String getRenderName()
	{
		if(goal != null)
			return "Go to " + goal.getX() + " " + goal.getY() + " " + goal.getZ();
		else
			return "GoTo";
	}
	
	@Override
	public void onEnable()
	{
		index = 0;
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		setToggled(false);
	}
	
	@Override
	public void onDisable()
	{
		path = null;
		goal = null;
	}

	public static void setPath(ArrayList<BlockPos> path)
	{
		GoToCmd.path = path;
	}

	public static BlockPos getGoal()
	{
		return goal;
	}

	public static void setGoal(BlockPos goal)
	{
		GoToCmd.goal = goal;
	}
}
