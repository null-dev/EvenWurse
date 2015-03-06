/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class PathFinder
{
	private BlockPos goal;
	private PriorityQueue<PathPoint> queue;
	private ArrayList<PathPoint> processed = new ArrayList<PathPoint>();
	
	public PathFinder(BlockPos goal)
	{
		this(Minecraft.getMinecraft().thePlayer.playerLocation, goal);
	}
	
	public PathFinder(BlockPos start, BlockPos goal)
	{
		this.goal = goal;
		queue = new PriorityQueue<PathPoint>(new Comparator<PathPoint>()
		{
			@Override
			public int compare(PathPoint o1, PathPoint o2)
			{
				return o1.getPriority() - o2.getPriority();
			}
		});
		addPoint(new PathPoint(start, null, 0));
	}
	
	public PathPoint find()
	{	
		PathPoint current = null;
		while(!queue.isEmpty())
		{
			current = queue.poll();
			if(current.getPos() == goal)
				break;
		}
		return current;
	}
	
	private void addPoint(PathPoint point)
	{
		processed.add(point);
		queue.add(point);
	}
}
