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
	
	public PathPoint(BlockPos pos, PathPoint previous, int priority)
	{
		this.pos = pos;
		this.previous = previous;
		this.priority = priority;
	}
	
	public ArrayList<BlockPos> getNeighbors()
	{
		ArrayList<BlockPos> neighbors = new ArrayList<BlockPos>();
		neighbors.add(pos.add(0, 0, 1));
		neighbors.add(pos.add(-1, 0, 0));
		neighbors.add(pos.add(0, 0, -1));
		neighbors.add(pos.add(1, 0, 0));
		return neighbors;
	}

	public int getPriority()
	{
		return priority;
	}

	public void setPriority(int priority)
	{
		this.priority = priority;
	}

	public BlockPos getPos()
	{
		return pos;
	}

	public PathPoint getPrevious()
	{
		return previous;
	}
}
