/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Mod;

public class AutoMine extends Mod implements UpdateListener
{
	public AutoMine()
	{
		super(
			"AutoMine",
			"Automatically mines a block as soon as you look at it.",
			Category.BLOCKS);
	}
	
	@Override
	public void onEnable()
	{
		Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().objectMouseOver == null
			|| Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null)
			return;
		if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld
			.getBlockState(
				Minecraft.getMinecraft().objectMouseOver.getBlockPos())
			.getBlock()) != 0)
			Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = true;
		else
			Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
		System.out
			.println(Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed);
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
	}
}
