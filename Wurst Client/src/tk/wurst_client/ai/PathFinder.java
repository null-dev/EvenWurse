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
import tk.wurst_client.Client;
import tk.wurst_client.module.Module;
import tk.wurst_client.module.modules.NoFall;

public class PathFinder
{
	private BlockPos goal;
	private PriorityQueue<PathPoint> queue;
	private HashMap<BlockPos, PathPoint> processed =
		new HashMap<BlockPos, PathPoint>();
	private Module noFallMod;
	
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
				return o1.getPriority() - o2.getPriority();
			}
		});
		addPoint(start, null, 0, 0);
		noFallMod = Client.wurst.moduleManager.getModuleFromClass(NoFall.class);
	}
	
	public PathPoint find()
	{
		PathPoint current = null;
		while(!queue.isEmpty())
		{
			current = queue.poll();
			processed.put(current.getPos(), current);
			if(current.getPos().equals(goal))
				break;
			for(BlockPos neighbor : current.getNeighbors())
			{
				if(!isSafe(neighbor))
					continue;
				int newCost = current.getMovementCost() + 1;
				// TODO: Different movement costs based on block type
				if(!processed.containsKey(neighbor)
					|| processed.get(neighbor).getMovementCost() > newCost)
					addPoint(neighbor, current, newCost,
						newCost + getDistance(neighbor, goal));
			}
		}
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
	
	private boolean isSafe(BlockPos pos)
	{
		return !Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock()
			.getMaterial().blocksMovement()
			&& (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
				|| noFallMod.getToggled()
				|| Minecraft.getMinecraft().theWorld
				.getBlockState(pos.add(0, -1, 0)).getBlock().getMaterial()
				.blocksMovement());
	}
}
