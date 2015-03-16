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
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.RenderListener;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Mod;
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;
import tk.wurst_client.utils.BuildUtils;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.BLOCKS,
	description = "Instantly builds a small bunker around you.",
	name = "InstantBunker")
public class InstantBunker extends Mod implements UpdateListener,
	RenderListener
{
	private float speed = 5;
	private int i;
	private boolean shouldBuild;
	private float playerYaw;
	private MovingObjectPosition MouseOver;
	private double posX;
	private double posY;
	private double posZ;
	
	// Bottom = 0, Top = 1, Front = 2, Back = 3, Right = 4, Left = 5.
	private int[][] building =
	{
		{0, 1, 2, 1},
		{1, 1, 2, 1},
		{-1, 1, 2, 1},
		{2, 1, 2, 1},
		{-2, 1, 2, 1},
		{2, 1, 1, 1},
		{-2, 1, 1, 1},
		{2, 1, 0, 1},
		{-2, 1, 0, 1},
		{2, 1, -1, 1},
		{-2, 1, -1, 1},
		{0, 1, -2, 1},
		{1, 1, -2, 1},
		{-1, 1, -2, 1},
		{2, 1, -2, 1},
		{-2, 1, -2, 1},
		{0, 2, 2, 1},
		{1, 2, 2, 1},
		{-1, 2, 2, 1},
		{2, 2, 2, 1},
		{-2, 2, 2, 1},
		{2, 2, 1, 1},
		{-2, 2, 1, 1},
		{2, 2, 0, 1},
		{-2, 2, 0, 1},
		{2, 2, -1, 1},
		{-2, 2, -1, 1},
		{0, 2, -2, 1},
		{1, 2, -2, 1},
		{-1, 2, -2, 1},
		{2, 2, -2, 1},
		{-2, 2, -2, 1},
		{0, 3, 2, 1},
		{1, 3, 2, 1},
		{-1, 3, 2, 1},
		{2, 3, 2, 1},
		{-2, 3, 2, 1},
		{2, 3, 1, 1},
		{-2, 3, 1, 1},
		{2, 3, 0, 1},
		{-2, 3, 0, 1},
		{2, 3, -1, 1},
		{-2, 3, -1, 1},
		{0, 3, -2, 1},
		{1, 3, -2, 1},
		{-1, 3, -2, 1},
		{2, 3, -2, 1},
		{-2, 3, -2, 1},
		{0, 4, 2, 2},
		{1, 4, 2, 2},
		{-1, 4, 2, 2},
		{0, 4, -2, 3},
		{1, 4, -2, 3},
		{-1, 4, -2, 3},
		{2, 4, 0, 4},
		{-2, 4, 0, 5},
		{0, 4, 1, 2},
	};
	
	@Override
	public void onEnable()
	{
		if(Client.wurst.modManager.getMod(FastPlace.class)
			.getToggled())
			speed = 1000000000;
		else
			speed = 5;
		if(Client.wurst.modManager.getMod(YesCheat.class)
			.getToggled())
		{
			i = 0;
			shouldBuild = true;
			MouseOver = Minecraft.getMinecraft().objectMouseOver;
			posX = Minecraft.getMinecraft().thePlayer.posX;
			posY = Minecraft.getMinecraft().thePlayer.posY;
			posZ = Minecraft.getMinecraft().thePlayer.posZ;
			playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
			while(playerYaw > 180)
				playerYaw -= 360;
			while(playerYaw < -180)
				playerYaw += 360;
		}
		EventManager.addUpdateListener(this);
		EventManager.addRenderListener(this);
	}
	
	@Override
	public void onRender()
	{
		if(shouldBuild && i < building.length && i >= 0)
			if(playerYaw > -45 && playerYaw <= 45)
			{// F: 0 South
				double renderX =
					(int)posX
						+ BuildUtils
							.convertPosInAdvancedBuiling(1, i, building);
				double renderY =
					(int)posY
						- 2
						+ BuildUtils
							.convertPosInAdvancedBuiling(2, i, building);
				double renderZ =
					(int)posZ
						+ BuildUtils
							.convertPosInAdvancedBuiling(3, i, building);
				RenderUtils
					.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}else if(playerYaw > 45 && playerYaw <= 135)
			{// F: 1 West
				double renderX =
					(int)posX
						- BuildUtils
							.convertPosInAdvancedBuiling(3, i, building);
				double renderY =
					(int)posY
						- 2
						+ BuildUtils
							.convertPosInAdvancedBuiling(2, i, building);
				double renderZ =
					(int)posZ
						+ BuildUtils
							.convertPosInAdvancedBuiling(1, i, building);
				RenderUtils
					.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}else if(playerYaw > 135 || playerYaw <= -135)
			{// F: 2 North
				double renderX =
					(int)posX
						- BuildUtils
							.convertPosInAdvancedBuiling(1, i, building);
				double renderY =
					(int)posY
						- 2
						+ BuildUtils
							.convertPosInAdvancedBuiling(2, i, building);
				double renderZ =
					(int)posZ
						- BuildUtils
							.convertPosInAdvancedBuiling(3, i, building);
				RenderUtils
					.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}else if(playerYaw > -135 && playerYaw <= -45)
			{// F: 3 East
				double renderX =
					(int)posX
						+ BuildUtils
							.convertPosInAdvancedBuiling(3, i, building);
				double renderY =
					(int)posY
						- 2
						+ BuildUtils
							.convertPosInAdvancedBuiling(2, i, building);
				double renderZ =
					(int)posZ
						- BuildUtils
							.convertPosInAdvancedBuiling(1, i, building);
				RenderUtils
					.blockESPBox(new BlockPos(renderX, renderY, renderZ));
			}
		for(int i = 0; i < building.length; i++)
			if(shouldBuild && MouseOver != null)
				if(playerYaw > -45 && playerYaw <= 45)
				{// F: 0 South
					double renderX =
						(int)posX
							+ BuildUtils.convertPosInAdvancedBuiling(1, i,
								building);
					double renderY =
						(int)posY
							- 2
							+ BuildUtils.convertPosInAdvancedBuiling(2, i,
								building);
					double renderZ =
						(int)posZ
							+ BuildUtils.convertPosInAdvancedBuiling(3, i,
								building);
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY,
						renderZ));
				}else if(playerYaw > 45 && playerYaw <= 135)
				{// F: 1 West
					double renderX =
						(int)posX
							- BuildUtils.convertPosInAdvancedBuiling(3, i,
								building);
					double renderY =
						(int)posY
							- 2
							+ BuildUtils.convertPosInAdvancedBuiling(2, i,
								building);
					double renderZ =
						(int)posZ
							+ BuildUtils.convertPosInAdvancedBuiling(1, i,
								building);
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY,
						renderZ));
				}else if(playerYaw > 135 || playerYaw <= -135)
				{// F: 2 North
					double renderX =
						(int)posX
							- BuildUtils.convertPosInAdvancedBuiling(1, i,
								building);
					double renderY =
						(int)posY
							- 2
							+ BuildUtils.convertPosInAdvancedBuiling(2, i,
								building);
					double renderZ =
						(int)posZ
							- BuildUtils.convertPosInAdvancedBuiling(3, i,
								building);
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY,
						renderZ));
				}else if(playerYaw > -135 && playerYaw <= -45)
				{// F: 3 East
					double renderX =
						(int)posX
							+ BuildUtils.convertPosInAdvancedBuiling(3, i,
								building);
					double renderY =
						(int)posY
							- 2
							+ BuildUtils.convertPosInAdvancedBuiling(2, i,
								building);
					double renderZ =
						(int)posZ
							- BuildUtils.convertPosInAdvancedBuiling(1, i,
								building);
					RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY,
						renderZ));
				}
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().objectMouseOver == null)
			return;
		updateMS();
		if(shouldBuild)
		{
			if((hasTimePassedS(speed) || Client.wurst.modManager
				.getMod(FastPlace.class).getToggled())
				&& i < building.length)
			{
				BuildUtils.advancedInstantBuildNext(building, MouseOver,
					playerYaw, posX + 1, posY, posZ, i);
				if(playerYaw > -45 && playerYaw <= 45)
					try
					{
						if(Block
							.getIdFromBlock(Minecraft.getMinecraft().theWorld
								.getBlockState(
									new BlockPos
									(
										(int)posX
											+ BuildUtils
												.convertPosInAdvancedBuiling(1,
													i, building),
										(int)posY
											- 2
											+ BuildUtils
												.convertPosInAdvancedBuiling(2,
													i, building),
										(int)posZ
											+ BuildUtils
												.convertPosInAdvancedBuiling(3,
													i, building)
									)).getBlock()) != 0)
							i += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				else if(playerYaw > 45 && playerYaw <= 135)
					try
					{
						if(Block
							.getIdFromBlock(Minecraft.getMinecraft().theWorld
								.getBlockState(
									new BlockPos
									(
										(int)posX
											- BuildUtils
												.convertPosInAdvancedBuiling(3,
													i, building),
										(int)posY
											- 2
											+ BuildUtils
												.convertPosInAdvancedBuiling(2,
													i, building),
										(int)posZ
											+ BuildUtils
												.convertPosInAdvancedBuiling(1,
													i, building)
									)).getBlock()) != 0)
							i += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				else if(playerYaw > 135 || playerYaw <= -135)
					try
					{
						if(Block
							.getIdFromBlock(Minecraft.getMinecraft().theWorld
								.getBlockState(
									new BlockPos
									(
										(int)posX
											- BuildUtils
												.convertPosInAdvancedBuiling(1,
													i, building),
										(int)posY
											- 2
											+ BuildUtils
												.convertPosInAdvancedBuiling(2,
													i, building),
										(int)posZ
											- BuildUtils
												.convertPosInAdvancedBuiling(3,
													i, building)
									)).getBlock()) != 0)
							i += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				else if(playerYaw > -135 && playerYaw <= -45)
					try
					{
						if(Block
							.getIdFromBlock(Minecraft.getMinecraft().theWorld
								.getBlockState(
									new BlockPos
									(
										(int)posX
											+ BuildUtils
												.convertPosInAdvancedBuiling(3,
													i, building),
										(int)posY
											- 2
											+ BuildUtils
												.convertPosInAdvancedBuiling(2,
													i, building),
										(int)posZ
											- BuildUtils
												.convertPosInAdvancedBuiling(1,
													i, building)
									)).getBlock()) != 0)
							i += 1;
					}catch(NullPointerException e)
					{}// If the current item is null.
				updateLastMS();
			}else if(i == building.length)
			{
				shouldBuild = false;
				setToggled(false);
			}
		}else
		{
			BuildUtils.advancedInstantBuild(building);
			setToggled(false);
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		EventManager.addRenderListener(this);
		shouldBuild = false;
	}
}
