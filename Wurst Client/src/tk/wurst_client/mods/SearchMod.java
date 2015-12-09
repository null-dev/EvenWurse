/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Helps you to find specific blocks.\n"
		+ "Use .search id <block id> or .search name <block name>\n"
		+ "to specify it.",
	name = "Search")
public class SearchMod extends Mod implements UpdateListener, RenderListener
{
	private ArrayList<BlockPos> matchingBlocks = new ArrayList<BlockPos>();
	private int range = 50;
	private int maxBlocks = 1000;
	public boolean notify = true;
	
	@Override
	public String getRenderName()
	{
		return getName() + " [" + WurstClient.INSTANCE.options.searchID + "]";
	}
	
	@Override
	public void onEnable()
	{
		notify = true;
		WurstClient.INSTANCE.events.addUpdateListener(this);
		WurstClient.INSTANCE.events.addRenderListener(this);
	}
	
	@Override
	public void onRender()
	{
		for(BlockPos blockPos : matchingBlocks)
			RenderUtils.searchBox(blockPos);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		if(hasTimePassedM(3000))
		{
			matchingBlocks.clear();
			for(int y = range; y >= -range; y--)
			{
				for(int x = range; x >= -range; x--)
				{
					for(int z = range; z >= -range; z--)
					{
						int posX =
							(int)(Minecraft.getMinecraft().thePlayer.posX + x);
						int posY =
							(int)(Minecraft.getMinecraft().thePlayer.posY + y);
						int posZ =
							(int)(Minecraft.getMinecraft().thePlayer.posZ + z);
						BlockPos pos = new BlockPos(posX, posY, posZ);
						if(Block
							.getIdFromBlock(Minecraft.getMinecraft().theWorld
								.getBlockState(pos).getBlock()) == WurstClient.INSTANCE.options.searchID)
							matchingBlocks.add(pos);
						if(matchingBlocks.size() >= maxBlocks)
							break;
					}
					if(matchingBlocks.size() >= maxBlocks)
						break;
				}
				if(matchingBlocks.size() >= maxBlocks)
					break;
			}
			if(matchingBlocks.size() >= maxBlocks && notify)
			{
				WurstClient.INSTANCE.chat.warning(getName()
					+ " found §lA LOT§r of blocks.");
				WurstClient.INSTANCE.chat
					.message("To prevent lag, it will only show the first "
						+ maxBlocks + " blocks.");
				notify = false;
			}else if(matchingBlocks.size() < maxBlocks)
				notify = true;
			updateLastMS();
		}
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
		WurstClient.INSTANCE.events.remove(RenderListener.class, this);
	}
}
