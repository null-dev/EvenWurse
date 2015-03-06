/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.ai;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class PathFinder
{
	public void findPath(BlockPos goal)
	{
		findPath(Minecraft.getMinecraft().thePlayer.playerLocation, goal);
	}
	
	public void findPath(BlockPos current, BlockPos goal)
	{
		
	}
}
