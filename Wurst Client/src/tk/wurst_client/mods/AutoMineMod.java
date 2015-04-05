/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.BLOCKS,
	description = "Automatically mines a block as soon as you look at it.",
	name = "AutoMine")
public class AutoMineMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
		EventManager.update.addListener(this);
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
		EventManager.update.removeListener(this);
		Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
	}
}
