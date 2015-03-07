/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.ai;

import java.util.ArrayList;

import net.minecraft.util.BlockPos;

public class PathPoint
{
	private BlockPos pos;
	private PathPoint previous;
	private int priority;
	private int movementCost;
	
	public PathPoint(BlockPos pos, PathPoint previous, int movementCost,
		int priority)
	{
		this.pos = pos;
		this.previous = previous;
		this.movementCost = movementCost;
		this.priority = priority;
	}
	
	public ArrayList<BlockPos> getNeighbors()
	{
		ArrayList<BlockPos> neighbors = new ArrayList<BlockPos>();
		if(PathSafety.isSolid(pos.add(0, -1, 0)) || PathSafety.isFlying())
		{
			neighbors.add(pos.add(0, 0, -1));// north
			neighbors.add(pos.add(0, 0, 1));// south
			neighbors.add(pos.add(1, 0, 0));// east
			neighbors.add(pos.add(-1, 0, 0));// west
		}
		neighbors.add(pos.add(0, -1, 0));// down
		if(PathSafety.isFlying() || PathSafety.isClimbable(pos))
			neighbors.add(pos.add(0, 1, 0));// up
		for(int i = neighbors.size() - 1; i > -1; i--)
			if(!PathSafety.isSafe(neighbors.get(i)))
				neighbors.remove(i);
		return neighbors;
	}
	
	public BlockPos getPos()
	{
		return pos;
	}
	
	public PathPoint getPrevious()
	{
		return previous;
	}
	
	public int getPriority()
	{
		return priority;
	}
	
	public int getMovementCost()
	{
		return movementCost;
	}
}
