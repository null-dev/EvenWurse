/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.ai;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class PathFinder
{
	private BlockPos goal;
	private PriorityQueue<PathPoint> queue;
	private HashMap<BlockPos, PathPoint> processed =
		new HashMap<BlockPos, PathPoint>();
	
	public PathFinder(BlockPos goal)
	{
		this(new BlockPos(Minecraft.getMinecraft().thePlayer), goal);
	}
	
	public PathFinder(BlockPos start, BlockPos goal)
	{
		this.goal = goal;
		queue = new PriorityQueue<PathPoint>(new Comparator<PathPoint>()
		{
			@Override
			public int compare(PathPoint o1, PathPoint o2)
			{
				if(o1.getPriority() < o2.getPriority())
					return -1;
				else if(o1.getPriority() > o2.getPriority())
					return 1;
				else if(getDistance(o1.getPos(), PathFinder.this.goal) < getDistance(o2.getPos(), PathFinder.this.goal))
					return -1;
				else if(getDistance(o1.getPos(), PathFinder.this.goal) > getDistance(o2.getPos(), PathFinder.this.goal))
					return 1;
				else
					return 0;
			}
		});
		addPoint(start, null, 0, 0);
	}
	
	public PathPoint find()
	{
		PathPoint current = null;
		long startTime = System.currentTimeMillis();
		while(!queue.isEmpty())
		{
			current = queue.poll();
			processed.put(current.getPos(), current);
			if(current.getPos().equals(goal))
				break;
			if(System.currentTimeMillis() - startTime > 10e3)
			{
				System.err.println("Path finding took more than 10s. Aborting!");
				break;
			}
			for(BlockPos next : current.getNeighbors())
			{
				if(!PathUtils.isSafe(next))
					continue;
				int nextCost = PathUtils.getCost(current.getPos(), next);
				int newCost = current.getMovementCost() + nextCost;
				if(!processed.containsKey(next)
					|| processed.get(next).getMovementCost() > newCost)
					addPoint(next, current, newCost,
						newCost + getDistance(next, goal) * nextCost);
			}
		}
		System.out.println("Processed " + processed.size() + " nodes");
		return current;
	}
	
	private void addPoint(BlockPos pos, PathPoint previous, int movementCost,
		int priority)
	{
		queue.add(new PathPoint(pos, previous, movementCost, priority));
	}
	
	private int getDistance(BlockPos a, BlockPos b)
	{
		return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY())
			+ Math.abs(a.getZ() - b.getZ());
	}
}
