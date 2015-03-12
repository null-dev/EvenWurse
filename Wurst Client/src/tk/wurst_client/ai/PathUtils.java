/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.ai;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.util.BlockPos;
import tk.wurst_client.Client;
import tk.wurst_client.module.Module;
import tk.wurst_client.module.modules.AntiKnockback;
import tk.wurst_client.module.modules.Flight;
import tk.wurst_client.module.modules.Jesus;
import tk.wurst_client.module.modules.NoFall;
import tk.wurst_client.module.modules.NoSlowdown;
import tk.wurst_client.module.modules.Spider;

public class PathUtils
{
	private static PlayerCapabilities playerCaps;
	private static Module antiKnockbackMod;
	private static Module flightMod;
	private static Module jesusMod;
	private static Module noFallMod;
	private static Module noSlowdownMod;
	private static Module spiderMod;
	
	public static boolean isSafe(BlockPos pos)
	{
		Material material = getMaterial(pos);
		int id = getID(pos);
		boolean alwaysSafe = !material.blocksMovement()
			&& id != 132;// tripwire
		if(isCreative())
			return alwaysSafe;
		Material materialBelow = getMaterial(pos.add(0, -1, 0));
		return alwaysSafe
			&& material != Material.lava
			&& materialBelow != Material.cactus
			&& material != Material.fire;
	}
	
	public static boolean isSolid(BlockPos pos)
	{
		if(jesusMod == null)
			jesusMod =
				Client.wurst.moduleManager.getModuleFromClass(Jesus.class);
		return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock()
			.getMaterial().blocksMovement()
			|| getMaterial(pos) == Material.water && jesusMod.getToggled();
	}
	
	public static boolean isFallable(BlockPos pos)
	{
		for(int i = -1; i >= (isNoFall() ? -256 : -3); i--)
			if(isSolid(pos.add(0, i, 0)))
				return true;
		return false;
	}
	
	public static boolean isClimbable(BlockPos pos)
	{
		if(spiderMod == null)
			spiderMod =
				Client.wurst.moduleManager.getModuleFromClass(Spider.class);
		if(isSolid(pos.add(0, -1, 0))
			|| spiderMod.getToggled()
			|| getID(pos) == 65
			|| isFlyable(pos))
			if(isSolid(pos.add(0, 0, -1))
				|| isSolid(pos.add(0, 0, 1))
				|| isSolid(pos.add(1, 0, 0))
				|| isSolid(pos.add(-1, 0, 0)))
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
	
	public static boolean isFlyable(BlockPos pos)
	{
		if(flightMod == null)
			flightMod =
				Client.wurst.moduleManager.getModuleFromClass(Flight.class);
		if(noSlowdownMod == null)
			noSlowdownMod =
				Client.wurst.moduleManager.getModuleFromClass(NoSlowdown.class);
		if(playerCaps == null)
			playerCaps = Minecraft.getMinecraft().thePlayer.capabilities;
		return flightMod.getToggled()
			|| playerCaps.isFlying
			|| !noSlowdownMod.getToggled()
			&& getMaterial(pos) == Material.water;
	}
	
	public static int getCost(BlockPos current, BlockPos next)
	{
		if(noSlowdownMod == null)
			noSlowdownMod =
				Client.wurst.moduleManager.getModuleFromClass(NoSlowdown.class);
		if(antiKnockbackMod == null)
			antiKnockbackMod =
				Client.wurst.moduleManager
					.getModuleFromClass(AntiKnockback.class);
		Material nextMaterial = getMaterial(next);
		if(nextMaterial == Material.water)
			if(noSlowdownMod.getToggled())
				return 1;
			else if(antiKnockbackMod.getToggled())
				return 2;
			else
				return 3;
		else if(nextMaterial == Material.lava)
			return 5;
		return 1;
	}
	
	private static Material getMaterial(BlockPos pos)
	{
		return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock()
			.getMaterial();
	}
	
	private static int getID(BlockPos pos)
	{
		return Block.getIdFromBlock(Minecraft.getMinecraft().theWorld
			.getBlockState(pos).getBlock());
	}
}
