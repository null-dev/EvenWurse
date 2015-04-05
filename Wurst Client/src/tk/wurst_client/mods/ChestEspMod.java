/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.RenderListener;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Allows you to see chests through walls.\n"
		+ "Tip: This works with the piston crates on HiveMC.",
	name = "ChestESP")
public class ChestEspMod extends Mod implements UpdateListener, RenderListener
{
	private int range = 50;
	private int maxChests = 1000;
	public boolean shouldInform = true;
	private ArrayList<BlockPos> matchingBlocks = new ArrayList<BlockPos>();
	
	@Override
	public void onEnable()
	{
		shouldInform = true;
		EventManager.update.addListener(this);
		EventManager.render.addListener(this);
	}
	
	@Override
	public void onRender()
	{
		int i = 0;
		for(Object o : Minecraft.getMinecraft().theWorld.loadedTileEntityList)
		{
			if(i >= maxChests)
				break;
			if(o instanceof TileEntityChest)
			{
				i++;
				RenderUtils.blockESPBox(((TileEntityChest)o).getPos());
			}
			else if(o instanceof TileEntityEnderChest)
			{
				i++;
				RenderUtils.blockESPBox(((TileEntityEnderChest)o).getPos());
			}
		}
		for(Object o : Minecraft.getMinecraft().theWorld.loadedEntityList)
		{
			if(i >= maxChests)
				break;
			if(o instanceof EntityMinecartChest)
			{
				i++;
				RenderUtils.blockESPBox(((EntityMinecartChest)o).getPosition());
			}
		}
		for(BlockPos blockPos : matchingBlocks)
		{
			if(i >= maxChests)
				break;
			i++;
			RenderUtils.blockESPBox(blockPos);
		}
		if(i >= maxChests && shouldInform)
		{
			Client.wurst.chat
				.warning(getName() + " found §lA LOT§r of chests.");
			Client.wurst.chat
				.message("To prevent lag, it will only show the first "
					+ maxChests + " chests.");
			shouldInform = false;
		}else if(i < maxChests)
			shouldInform = true;
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		if(hasTimePassedM(3000))
		{
			matchingBlocks.clear();
			for(int y = range; y >= -range; y--)
				for(int x = range; x >= -range; x--)
					for(int z = range; z >= -range; z--)
					{
						int posX =
							(int)(Minecraft.getMinecraft().thePlayer.posX + x);
						int posY =
							(int)(Minecraft.getMinecraft().thePlayer.posY + y);
						int posZ =
							(int)(Minecraft.getMinecraft().thePlayer.posZ + z);
						BlockPos pos = new BlockPos(posX, posY, posZ);
						IBlockState state =
							Minecraft.getMinecraft().theWorld
								.getBlockState(pos);
						Block block = state.getBlock();
						int metadata = block.getMetaFromState(state);
						if(Block.getIdFromBlock(block) == 33 &&
							(
							metadata == 6
								|| metadata == 7
								|| metadata == 15
							))
							matchingBlocks.add(pos);
					}
			updateLastMS();
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
		EventManager.render.removeListener(this);
	}
}
