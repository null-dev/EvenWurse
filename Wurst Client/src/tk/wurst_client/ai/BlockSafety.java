/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.ai;

import tk.wurst_client.Client;
import tk.wurst_client.module.Module;
import tk.wurst_client.module.modules.NoFall;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class BlockSafety
{
	private static Module noFallMod;
	
	public static boolean isSafe(BlockPos pos)
	{
		if(noFallMod == null)
			noFallMod = Client.wurst.moduleManager.getModuleFromClass(NoFall.class);
		return !Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock()
			.getMaterial().blocksMovement()
			&& (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
				|| noFallMod.getToggled()
				|| Minecraft.getMinecraft().theWorld
				.getBlockState(pos.add(0, -1, 0)).getBlock().getMaterial()
				.blocksMovement());
	}
}
