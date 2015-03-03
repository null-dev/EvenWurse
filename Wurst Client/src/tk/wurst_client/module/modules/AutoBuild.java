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
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.BuildUtils;
import tk.wurst_client.utils.RenderUtils;

public class AutoBuild extends Module
{
	public AutoBuild()
	{
		super(
			"AutoBuild",
			"Automatically builds the selected template whenever\n"
				+ "you place a block. Use the combo box below to select\n"
				+ "a template.\n"
				+ "This mod can bypass NoCheat+ while YesCheat+ is\n"
				+ "enabled.",
			Category.BLOCKS);
	}
	
	public static ArrayList<String> names = new ArrayList<String>();
	public static ArrayList<int[][]> buildings = new ArrayList<int[][]>();
	private float speed = 5;
	private int blockIndex;
	private boolean shouldBuild;
	private float playerYaw;
	private MovingObjectPosition mouseOver;
	
	@Override
	public String getRenderName()
	{
		return getName() + " [" + names.get(Client.wurst.options.autobuildMode) + "]";
	}
	
	@Override
	public void onRender()
	{
		if(!getToggled())
			return;
		if(buildings.get(Client.wurst.options.autobuildMode)[0].length == 4)
			renderAdvanced();
		else
			renderSimple();
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(buildings.get(Client.wurst.options.autobuildMode)[0].length == 4)
			buildAdvanced();
		else
			buildSimple();
	}
	
	@Override
	public void onDisable()
	{
		shouldBuild = false;
	}
	
	private void renderAdvanced()
	{
		if(shouldBuild && blockIndex < buildings.get(Client.wurst.options.autobuildMode).length && blockIndex >= 0)
			if(playerYaw > -45 && playerYaw <= 45)
			{// F: 0 South
				double renderX = BuildUtils.convertPosNext(1, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				double renderZ = BuildUtils.convertPosNext(3, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}else if(playerYaw > 45 && playerYaw <= 135)
			{// F: 1 West
				double renderX = BuildUtils.convertPosNext(1, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				double renderZ = BuildUtils.convertPosNext(3, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}else if(playerYaw > 135 || playerYaw <= -135)
			{// F: 2 North
				double renderX = BuildUtils.convertPosNext(1, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				double renderZ = BuildUtils.convertPosNext(3, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}else if(playerYaw > -135 && playerYaw <= -45)
			{// F: 3 East
				double renderX = BuildUtils.convertPosNext(1, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				double renderZ = BuildUtils.convertPosNext(3, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode));
				RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}
		if(shouldBuild && mouseOver != null)
		{
			double renderX = BuildUtils.convertPosNext(1, mouseOver);
			double renderY = BuildUtils.convertPosNext(2, mouseOver) + 1;
			double renderZ = BuildUtils.convertPosNext(3, mouseOver);
			RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
		}
		for(int i = 0; i < buildings.get(Client.wurst.options.autobuildMode).length; i++)
			if(shouldBuild && mouseOver != null)
				if(playerYaw > -45 && playerYaw <= 45)
				{// F: 0 South
					double renderX = BuildUtils.convertPosNext(1, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(1, i, buildings.get(Client.wurst.options.autobuildMode));
					double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, i, buildings.get(Client.wurst.options.autobuildMode));
					double renderZ = BuildUtils.convertPosNext(3, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(3, i, buildings.get(Client.wurst.options.autobuildMode));
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
				}else if(playerYaw > 45 && playerYaw <= 135)
				{// F: 1 West
					double renderX = BuildUtils.convertPosNext(1, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(3, i, buildings.get(Client.wurst.options.autobuildMode));
					double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, i, buildings.get(Client.wurst.options.autobuildMode));
					double renderZ = BuildUtils.convertPosNext(3, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(1, i, buildings.get(Client.wurst.options.autobuildMode));
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
				}else if(playerYaw > 135 || playerYaw <= -135)
				{// F: 2 North
					double renderX = BuildUtils.convertPosNext(1, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(1, i, buildings.get(Client.wurst.options.autobuildMode));
					double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, i, buildings.get(Client.wurst.options.autobuildMode));
					double renderZ = BuildUtils.convertPosNext(3, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(3, i, buildings.get(Client.wurst.options.autobuildMode));
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
				}else if(playerYaw > -135 && playerYaw <= -45)
				{// F: 3 East
					double renderX = BuildUtils.convertPosNext(1, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(3, i, buildings.get(Client.wurst.options.autobuildMode));
					double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, i, buildings.get(Client.wurst.options.autobuildMode));
					double renderZ = BuildUtils.convertPosNext(3, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(1, i, buildings.get(Client.wurst.options.autobuildMode));
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
				}
	}
	
	private void renderSimple()
	{
		if(shouldBuild && blockIndex < buildings.get(Client.wurst.options.autobuildMode).length && blockIndex >= 0)
			if(playerYaw > -45 && playerYaw <= 45)
			{// F: 0 South
				double renderX = mouseOver.getBlockPos().getX() + BuildUtils.convertPosInBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				double renderY = mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				double renderZ = mouseOver.getBlockPos().getZ() + BuildUtils.convertPosInBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}else if(playerYaw > 45 && playerYaw <= 135)
			{// F: 1 West
				double renderX = mouseOver.getBlockPos().getX() - BuildUtils.convertPosInBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				double renderY = mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				double renderZ = mouseOver.getBlockPos().getZ() + BuildUtils.convertPosInBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}else if(playerYaw > 135 || playerYaw <= -135)
			{// F: 2 North
				double renderX = mouseOver.getBlockPos().getX() - BuildUtils.convertPosInBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				double renderY = mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				double renderZ = mouseOver.getBlockPos().getZ() - BuildUtils.convertPosInBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}else if(playerYaw > -135 && playerYaw <= -45)
			{// F: 3 East
				double renderX = mouseOver.getBlockPos().getX() + BuildUtils.convertPosInBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				double renderY = mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				double renderZ = mouseOver.getBlockPos().getZ() - BuildUtils.convertPosInBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
				RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}
		if(shouldBuild && mouseOver != null)
		{
			double renderX = BuildUtils.convertPosNext(1, mouseOver);
			double renderY = BuildUtils.convertPosNext(2, mouseOver) + 1;
			double renderZ = BuildUtils.convertPosNext(3, mouseOver);
			RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
		}
		for(int i = 0; i < buildings.get(Client.wurst.options.autobuildMode).length; i++)
			if(shouldBuild && mouseOver != null)
				if(playerYaw > -45 && playerYaw <= 45)
				{// F: 0 South
					double renderX = mouseOver.getBlockPos().getX() + BuildUtils.convertPosInBuiling(1, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					double renderY = mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					double renderZ = mouseOver.getBlockPos().getZ() + BuildUtils.convertPosInBuiling(3, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
				}else if(playerYaw > 45 && playerYaw <= 135)
				{// F: 1 West
					double renderX = mouseOver.getBlockPos().getX() - BuildUtils.convertPosInBuiling(3, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					double renderY = mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					double renderZ = mouseOver.getBlockPos().getZ() + BuildUtils.convertPosInBuiling(1, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
				}else if(playerYaw > 135 || playerYaw <= -135)
				{// F: 2 North
					double renderX = mouseOver.getBlockPos().getX() - BuildUtils.convertPosInBuiling(1, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					double renderY = mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					double renderZ = mouseOver.getBlockPos().getZ() - BuildUtils.convertPosInBuiling(3, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
				}else if(playerYaw > -135 && playerYaw <= -45)
				{// F: 3 East
					double renderX = mouseOver.getBlockPos().getX() + BuildUtils.convertPosInBuiling(3, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					double renderY = mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					double renderZ = mouseOver.getBlockPos().getZ() - BuildUtils.convertPosInBuiling(1, i, buildings.get(Client.wurst.options.autobuildMode), mouseOver);
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
				}
	}
	
	private void buildAdvanced()
	{
		updateMS();
		if(!shouldBuild
			&& (Minecraft.getMinecraft().rightClickDelayTimer == 4 || Client.wurst.moduleManager.getModuleFromClass(FastPlace.class).getToggled())
			&& Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed
			&& Minecraft.getMinecraft().objectMouseOver != null
			&& Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null
			&& Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air)
		{
			if(Client.wurst.moduleManager.getModuleFromClass(FastPlace.class).getToggled())
				speed = 1000000000;
			else
				speed = 5;
			if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class).getToggled())
			{
				blockIndex = 0;
				shouldBuild = true;
				mouseOver = Minecraft.getMinecraft().objectMouseOver;
				playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
				while(playerYaw > 180)
					playerYaw -= 360;
				while(playerYaw < -180)
					playerYaw += 360;
			}else
				BuildUtils.advancedBuild(buildings.get(Client.wurst.options.autobuildMode));
			updateLastMS();
			return;
		}
		if(shouldBuild)
			if((hasTimePassedS(speed) || Client.wurst.moduleManager.getModuleFromClass(FastPlace.class).getToggled()) && blockIndex < buildings.get(Client.wurst.options.autobuildMode).length)
			{
				BuildUtils.advancedBuildNext(buildings.get(Client.wurst.options.autobuildMode), mouseOver, playerYaw, blockIndex);
				if(playerYaw > -45 && playerYaw <= 45)
					try
					{
						if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos
							(
								BuildUtils.convertPosNext(1, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode)),
								BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode)),
								BuildUtils.convertPosNext(3, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode))
							)).getBlock()) != 0)
							blockIndex += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				else if(playerYaw > 45 && playerYaw <= 135)
					try
					{
						if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos
							(
								BuildUtils.convertPosNext(1, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode)),
								BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode)),
								BuildUtils.convertPosNext(3, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode))
							)).getBlock()) != 0)
							blockIndex += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				else if(playerYaw > 135 || playerYaw <= -135)
					try
					{
						if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos
							(
								BuildUtils.convertPosNext(1, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode)),
								BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode)),
								BuildUtils.convertPosNext(3, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode))
							)).getBlock()) != 0)
							blockIndex += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				else if(playerYaw > -135 && playerYaw <= -45)
					try
					{
						if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos
							(
								BuildUtils.convertPosNext(1, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode)),
								BuildUtils.convertPosNext(2, mouseOver) + BuildUtils.convertPosInAdvancedBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode)),
								BuildUtils.convertPosNext(3, mouseOver) - BuildUtils.convertPosInAdvancedBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode))
							)).getBlock()) != 0)
							blockIndex += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				updateLastMS();
			}else if(blockIndex == buildings.get(Client.wurst.options.autobuildMode).length)
				shouldBuild = false;
	}
	
	private void buildSimple()
	{
		updateMS();
		if(!shouldBuild
			&& (Minecraft.getMinecraft().rightClickDelayTimer == 4 || Client.wurst.moduleManager.getModuleFromClass(FastPlace.class).getToggled())
			&& Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed
			&& Minecraft.getMinecraft().objectMouseOver != null
			&& Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null
			&& Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air)
		{
			if(Client.wurst.moduleManager.getModuleFromClass(FastPlace.class).getToggled())
				speed = 1000000000;
			else
				speed = 5;
			if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class).getToggled())
			{
				blockIndex = 0;
				shouldBuild = true;
				mouseOver = Minecraft.getMinecraft().objectMouseOver;
				playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
				while(playerYaw > 180)
					playerYaw -= 360;
				while(playerYaw < -180)
					playerYaw += 360;
			}else
				BuildUtils.build(buildings.get(Client.wurst.options.autobuildMode));
			updateLastMS();
			return;
		}
		if(shouldBuild)
			if((hasTimePassedS(speed) || Client.wurst.moduleManager.getModuleFromClass(FastPlace.class).getToggled()) && blockIndex < buildings.get(Client.wurst.options.autobuildMode).length)
			{
				BuildUtils.buildNext(buildings.get(Client.wurst.options.autobuildMode), mouseOver, playerYaw, blockIndex);
				if(playerYaw > -45 && playerYaw <= 45)
					try
					{
						if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos
							(
								mouseOver.getBlockPos().getX() + BuildUtils.convertPosInBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver),
								mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver),
								mouseOver.getBlockPos().getZ() + BuildUtils.convertPosInBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver)
							)).getBlock()) != 0)
							blockIndex += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				else if(playerYaw > 45 && playerYaw <= 135)
					try
					{
						if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos
							(
								mouseOver.getBlockPos().getX() - BuildUtils.convertPosInBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver),
								mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver),
								mouseOver.getBlockPos().getZ() + BuildUtils.convertPosInBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver)
							)).getBlock()) != 0)
							blockIndex += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				else if(playerYaw > 135 || playerYaw <= -135)
					try
					{
						if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos
							(
								mouseOver.getBlockPos().getX() - BuildUtils.convertPosInBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver),
								mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver),
								mouseOver.getBlockPos().getZ() - BuildUtils.convertPosInBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver)
							)).getBlock()) != 0)
							blockIndex += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				else if(playerYaw > -135 && playerYaw <= -45)
					try
					{
						if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos
							(
								mouseOver.getBlockPos().getX() + BuildUtils.convertPosInBuiling(3, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver),
								mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver),
								mouseOver.getBlockPos().getZ() - BuildUtils.convertPosInBuiling(1, blockIndex, buildings.get(Client.wurst.options.autobuildMode), mouseOver)
							)).getBlock()) != 0)
							blockIndex += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				updateLastMS();
			}else if(blockIndex == buildings.get(Client.wurst.options.autobuildMode).length)
				shouldBuild = false;
	}
}
