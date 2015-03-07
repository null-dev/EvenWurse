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
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.util.BlockPos;

public class PathSafety
{
	private static Module noFallMod;
	private static PlayerCapabilities playerCaps;
	
	public static boolean isSafe(BlockPos pos)
	{
		BlockPos playerPos = new BlockPos(Minecraft.getMinecraft().thePlayer);
		return !isSolid(pos)
			&& !isSolid(pos.add(0, 1, 0))
			&& isFallable(pos.add(0, -1, 0))
			&& Math.abs(playerPos.getX() - pos.getX()) < 256
			&& Math.abs(playerPos.getZ() - pos.getZ()) < 256;
	}
	
	public static boolean isSolid(BlockPos pos)
	{
		return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock()
			.getMaterial().blocksMovement();
	}
	
	private static boolean isFallable(BlockPos pos)
	{
		for(int i = -2; i >= (isNoFall() ? -256 : -3); i--)
			if(isSolid(pos.add(0, i, 0)))
				return true;
		return false;
	}
	
	public static boolean isNoFall()
	{
		if(noFallMod == null)
			noFallMod =
				Client.wurst.moduleManager.getModuleFromClass(NoFall.class);
		return noFallMod.getToggled() || isCreative();
	}
	
	public static boolean isCreative()
	{
		if(playerCaps == null)
			playerCaps = Minecraft.getMinecraft().thePlayer.capabilities;
		return playerCaps.isCreativeMode;
	}
}
