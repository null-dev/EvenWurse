/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.ai;

import net.minecraft.util.BlockPos;

public class PathPoint
{
	private BlockPos pos;
	private PathPoint previous;
	private int passedTicks;
	
	public PathPoint(BlockPos pos, PathPoint previous, int passedTicks)
	{
		this.pos = pos;
		this.previous = previous;
		this.passedTicks = passedTicks;
	}

	public int getPassedTicks()
	{
		return passedTicks;
	}

	public void setPassedTicks(int passedTicks)
	{
		this.passedTicks = passedTicks;
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
