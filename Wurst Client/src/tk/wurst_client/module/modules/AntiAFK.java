/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.BlockUtils;

public class AntiAFK extends Module
{
	private BlockPos block;
	private Random random;
	private BlockPos nextBlock;
	
	public AntiAFK()
	{
		super("AntiAFK",
			"Walks around randomly to hide you from AFK detectors.\n"
			+ "Needs 3x3 blocks of free space.",
			0,
			Category.MISC);
	}
	
	@Override
	public void onEnable()
	{
		try
		{
			block = new BlockPos(Minecraft.getMinecraft().thePlayer);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		random  = new Random();
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		updateMS();
		if(hasTimePassedM(3000) || nextBlock == null)
		{
			if(block == null)
				onEnable();
			nextBlock = block.add(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
			updateLastMS();
		}
		BlockUtils.faceBlockClientHorizontally(nextBlock);
		if(BlockUtils.getHorizontalPlayerBlockDistance(nextBlock) > 0.75)
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = true;
		else
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
	}
	
	@Override
	public void onDisable()
	{
		Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
	}
}
