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
import net.minecraft.util.BlockPos;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.RenderListener;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.RenderUtils;

public class Search extends Module implements UpdateListener, RenderListener
{
	private ArrayList<BlockPos> matchingBlocks = new ArrayList<BlockPos>();
	private int range = 50;
	private int maxBlocks = 1000;
	public static boolean shouldInform = true;
	
	public Search()
	{
		super(
			"Search",
			"Helps you to find specific blocks.\n"
				+ "Use .search id <block id> or .search name <block name>\n"
				+ "to specify it.",
			Category.RENDER);
	}

	@Override
	public String getRenderName()
	{
		return getName() + " [" + Client.wurst.options.searchID + "]";
	}
	
	@Override
	public void onEnable()
	{
		shouldInform = true;
		EventManager.addUpdateListener(this);
		EventManager.addRenderListener(this);
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
								.getBlockState(pos).getBlock()) == Client.wurst.options.searchID)
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
			if(matchingBlocks.size() >= maxBlocks && shouldInform)
			{
				Client.wurst.chat.warning(getName()
					+ " found §lA LOT§r of blocks.");
				Client.wurst.chat
					.message("To prevent lag, it will only show the first "
						+ maxBlocks + " blocks.");
				shouldInform = false;
			}else if(matchingBlocks.size() < maxBlocks)
				shouldInform = true;
			updateLastMS();
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		EventManager.removeRenderListener(this);
	}
}
