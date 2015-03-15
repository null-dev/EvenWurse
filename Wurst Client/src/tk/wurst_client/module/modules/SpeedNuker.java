/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.BlockUtils;

public class SpeedNuker extends Module implements UpdateListener
{
	private static Block currentBlock;
	private BlockPos pos;
	private int oldSlot = -1;
	
	public SpeedNuker()
	{
		super(
			"SpeedNuker",
			"Faster Nuker that cannot bypass NoCheat+.",
			Category.BLOCKS);
	}
	
	@Override
	public String getRenderName()
	{
		if(Client.wurst.options.nukerMode == 1)
			return "IDSpeedNuker [" + Nuker.id + "]";
		else if(Client.wurst.options.nukerMode == 2)
			return "FlatSpeedNuker";
		else if(Client.wurst.options.nukerMode == 3)
			return "SmashSpeedNuker";
		else
			return "SpeedNuker";
	}
	
	@Override
	public void onEnable()
	{
		if(Client.wurst.modManager.getMod(Nuker.class)
			.getToggled())
			Client.wurst.modManager.getMod(Nuker.class)
				.setToggled(false);
		if(Client.wurst.modManager.getMod(NukerLegit.class)
			.getToggled())
			Client.wurst.modManager.getMod(NukerLegit.class)
				.setToggled(false);
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getMod(YesCheat.class)
			.getToggled())
		{
			noCheatMessage();
			setToggled(false);
			Client.wurst.chat.message("Switching to "
				+ Client.wurst.modManager.getMod(Nuker.class)
					.getName() + ".");
			Client.wurst.modManager.getMod(Nuker.class)
				.setToggled(true);
			return;
		}
		if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
		{
			Client.wurst.chat.error(getName()
				+ " doesn't work in creative mode.");
			setToggled(false);
			Client.wurst.chat.message("Switching to "
				+ Client.wurst.modManager.getMod(Nuker.class)
					.getName() + ".");
			Client.wurst.modManager.getMod(Nuker.class)
				.setToggled(true);
			return;
		}
		BlockPos newPos = find();
		if(newPos == null)
		{
			if(oldSlot != -1)
			{
				Minecraft.getMinecraft().thePlayer.inventory.currentItem =
					oldSlot;
				oldSlot = -1;
			}
			return;
		}
		pos = newPos;
		currentBlock =
			Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
		if(Client.wurst.modManager.getMod(AutoTool.class)
			.getToggled() && oldSlot == -1)
			oldSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
			&& Client.wurst.modManager.getMod(AutoTool.class)
				.getToggled()
			&& currentBlock.getPlayerRelativeBlockHardness(
				Minecraft.getMinecraft().thePlayer,
				Minecraft.getMinecraft().theWorld, pos) < 1)
			AutoTool.setSlot(pos);
		nukeAll();
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		if(oldSlot != -1)
		{
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
			oldSlot = -1;
		}
		Nuker.id = 0;
		Client.wurst.fileManager.saveOptions();
	}
	
	@Override
	public void onLeftClick()
	{
		if(!getToggled()
			|| Minecraft.getMinecraft().objectMouseOver == null
			|| Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null)
			return;
		if(Client.wurst.options.nukerMode == 1
			&& Minecraft.getMinecraft().theWorld
				.getBlockState(
					Minecraft.getMinecraft().objectMouseOver.getBlockPos())
				.getBlock().getMaterial() != Material.air)
		{
			Nuker.id =
				Block.getIdFromBlock(Minecraft.getMinecraft().theWorld
					.getBlockState(
						Minecraft.getMinecraft().objectMouseOver.getBlockPos())
					.getBlock());
			Client.wurst.fileManager.saveOptions();
		}
	}
	
	private BlockPos find()
	{
		BlockPos closest = null;
		float closestDistance = Nuker.yesCheatRange + 1;
		for(int y = (int)Nuker.yesCheatRange; y >= (Client.wurst.options.nukerMode == 2
			? 0 : -Nuker.yesCheatRange); y--)
			for(int x = (int)Nuker.yesCheatRange; x >= -Nuker.yesCheatRange - 1; x--)
				for(int z = (int)Nuker.yesCheatRange; z >= -Nuker.yesCheatRange; z--)
				{
					if(Minecraft.getMinecraft().thePlayer == null)
						continue;
					if(x == 0 && y == -1 && z == 0)
						continue;
					int posX =
						(int)(Math
							.floor(Minecraft.getMinecraft().thePlayer.posX) + x);
					int posY =
						(int)(Math
							.floor(Minecraft.getMinecraft().thePlayer.posY) + y);
					int posZ =
						(int)(Math
							.floor(Minecraft.getMinecraft().thePlayer.posZ) + z);
					BlockPos blockPos = new BlockPos(posX, posY, posZ);
					Block block =
						Minecraft.getMinecraft().theWorld.getBlockState(
							blockPos).getBlock();
					float xDiff =
						(float)(Minecraft.getMinecraft().thePlayer.posX - posX);
					float yDiff =
						(float)(Minecraft.getMinecraft().thePlayer.posY - posY);
					float zDiff =
						(float)(Minecraft.getMinecraft().thePlayer.posZ - posZ);
					float currentDistance =
						BlockUtils.getBlockDistance(xDiff, yDiff, zDiff);
					MovingObjectPosition fakeObjectMouseOver =
						Minecraft.getMinecraft().objectMouseOver;
					if(fakeObjectMouseOver == null)
						continue;
					fakeObjectMouseOver.setBlockPos(blockPos);
					if(Block.getIdFromBlock(block) != 0 && posY >= 0
						&& currentDistance <= Nuker.yesCheatRange)
					{
						if(Client.wurst.options.nukerMode == 1
							&& Block.getIdFromBlock(block) != Nuker.id)
							continue;
						if(Client.wurst.options.nukerMode == 3
							&& block.getPlayerRelativeBlockHardness(
								Minecraft.getMinecraft().thePlayer,
								Minecraft.getMinecraft().theWorld, blockPos) < 1)
							continue;
						if(closest == null)
						{
							closest = blockPos;
							closestDistance = currentDistance;
						}else if(currentDistance < closestDistance)
						{
							closest = blockPos;
							closestDistance = currentDistance;
						}
					}
				}
		return closest;
	}
	
	private void nukeAll()
	{
		for(int y = (int)Nuker.normalRange; y >= (Client.wurst.options.nukerMode == 2
			? 0 : -Nuker.normalRange); y--)
			for(int x = (int)Nuker.normalRange; x >= -Nuker.normalRange - 1; x--)
				for(int z = (int)Nuker.normalRange; z >= -Nuker.normalRange; z--)
				{
					int posX =
						(int)(Math
							.floor(Minecraft.getMinecraft().thePlayer.posX) + x);
					int posY =
						(int)(Math
							.floor(Minecraft.getMinecraft().thePlayer.posY) + y);
					int posZ =
						(int)(Math
							.floor(Minecraft.getMinecraft().thePlayer.posZ) + z);
					if(x == 0 && y == -1 && z == 0)
						continue;
					BlockPos blockPos = new BlockPos(posX, posY, posZ);
					Block block =
						Minecraft.getMinecraft().theWorld.getBlockState(
							blockPos).getBlock();
					float xDiff =
						(float)(Minecraft.getMinecraft().thePlayer.posX - posX);
					float yDiff =
						(float)(Minecraft.getMinecraft().thePlayer.posY - posY);
					float zDiff =
						(float)(Minecraft.getMinecraft().thePlayer.posZ - posZ);
					float currentDistance =
						BlockUtils.getBlockDistance(xDiff, yDiff, zDiff);
					MovingObjectPosition fakeObjectMouseOver =
						Minecraft.getMinecraft().objectMouseOver;
					fakeObjectMouseOver.setBlockPos(new BlockPos(posX, posY,
						posZ));
					if(Block.getIdFromBlock(block) != 0 && posY >= 0
						&& currentDistance <= Nuker.normalRange)
					{
						if(Client.wurst.options.nukerMode == 1
							&& Block.getIdFromBlock(block) != Nuker.id)
							continue;
						if(Client.wurst.options.nukerMode == 3
							&& block.getPlayerRelativeBlockHardness(
								Minecraft.getMinecraft().thePlayer,
								Minecraft.getMinecraft().theWorld, blockPos) < 1)
							continue;
						if(!Minecraft.getMinecraft().thePlayer.onGround)
							continue;
						EnumFacing side = fakeObjectMouseOver.sideHit;
						Minecraft.getMinecraft().thePlayer.sendQueue
							.addToSendQueue(new C07PacketPlayerDigging(
								Action.START_DESTROY_BLOCK, blockPos, side));
						Minecraft.getMinecraft().thePlayer.sendQueue
							.addToSendQueue(new C07PacketPlayerDigging(
								Action.STOP_DESTROY_BLOCK, blockPos, side));
					}
				}
	}
}
