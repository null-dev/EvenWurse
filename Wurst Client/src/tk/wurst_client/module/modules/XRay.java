/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import tk.wurst_client.module.Mod;
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;

@Info(category = Category.RENDER,
	description = "Allows you to see ores through walls.",
	name = "X-Ray")
public class XRay extends Mod
{
	public static ArrayList<Block> xrayBlocks = new ArrayList<Block>();
	
	@Override
	public String getRenderName()
	{
		return "X-Wurst";
	}
	
	@Override
	public void onEnable()
	{
		Block.isXRayEnabled = true;
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
	
	@Override
	public void onDisable()
	{
		Block.isXRayEnabled = false;
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
}
