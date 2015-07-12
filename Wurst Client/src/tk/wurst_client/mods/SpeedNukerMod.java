/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.LeftClickListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.BlockUtils;

@Info(category = Category.BLOCKS,
	description = "Faster Nuker that cannot bypass NoCheat+.",
	name = "SpeedNuker")
public class SpeedNukerMod extends Mod implements LeftClickListener,
	UpdateListener
{
	private static Block currentBlock;
	private BlockPos pos;
	private int oldSlot = -1;
	
	@Override
	public String getRenderName()
	{
		if(WurstClient.INSTANCE.options.nukerMode == 1)
			return "IDSpeedNuker [" + NukerMod.id + "]";
		else if(WurstClient.INSTANCE.options.nukerMode == 2)
			return "FlatSpeedNuker";
		else if(WurstClient.INSTANCE.options.nukerMode == 3)
			return "SmashSpeedNuker";
		else
			return "SpeedNuker";
	}
	
	@Override
	public void onEnable()
	{
		if(WurstClient.INSTANCE.modManager.getModByClass(NukerMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(NukerMod.class)
				.setEnabled(false);
		if(WurstClient.INSTANCE.modManager.getModByClass(NukerLegitMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(NukerLegitMod.class)
				.setEnabled(false);
		if(WurstClient.INSTANCE.modManager.getModByClass(TunnellerMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(TunnellerMod.class)
				.setEnabled(false);
		WurstClient.INSTANCE.eventManager.add(LeftClickListener.class, this);
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(WurstClient.INSTANCE.modManager.getModByClass(YesCheatMod.class)
			.isActive())
		{
			noCheatMessage();
			setEnabled(false);
			WurstClient.INSTANCE.chat.message("Switching to "
				+ WurstClient.INSTANCE.modManager.getModByClass(NukerMod.class)
					.getName() + ".");
			WurstClient.INSTANCE.modManager.getModByClass(NukerMod.class)
				.setEnabled(true);
			return;
		}
		if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
		{
			WurstClient.INSTANCE.chat.error(getName()
				+ " doesn't work in creative mode.");
			setEnabled(false);
			WurstClient.INSTANCE.chat.message("Switching to "
				+ WurstClient.INSTANCE.modManager.getModByClass(NukerMod.class)
					.getName() + ".");
			WurstClient.INSTANCE.modManager.getModByClass(NukerMod.class)
				.setEnabled(true);
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
		if(WurstClient.INSTANCE.modManager.getModByClass(AutoToolMod.class)
			.isActive() && oldSlot == -1)
			oldSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
			&& WurstClient.INSTANCE.modManager.getModByClass(AutoToolMod.class)
				.isActive()
			&& currentBlock.getPlayerRelativeBlockHardness(
				Minecraft.getMinecraft().thePlayer,
				Minecraft.getMinecraft().theWorld, pos) < 1)
			AutoToolMod.setSlot(pos);
		nukeAll();
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(LeftClickListener.class, this);
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
		if(oldSlot != -1)
		{
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
			oldSlot = -1;
		}
		NukerMod.id = 0;
		WurstClient.INSTANCE.fileManager.saveOptions();
	}
	
	@Override
	public void onLeftClick()
	{
		if(Minecraft.getMinecraft().objectMouseOver == null
			|| Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null)
			return;
		if(WurstClient.INSTANCE.options.nukerMode == 1
			&& Minecraft.getMinecraft().theWorld
				.getBlockState(
					Minecraft.getMinecraft().objectMouseOver.getBlockPos())
				.getBlock().getMaterial() != Material.air)
		{
			NukerMod.id =
				Block.getIdFromBlock(Minecraft.getMinecraft().theWorld
					.getBlockState(
						Minecraft.getMinecraft().objectMouseOver.getBlockPos())
					.getBlock());
			WurstClient.INSTANCE.fileManager.saveOptions();
		}
	}
	
	private BlockPos find()
	{
		BlockPos closest = null;
		NukerMod nuker =
			(NukerMod)WurstClient.INSTANCE.modManager
				.getModByClass(NukerMod.class);
		float closestDistance = nuker.yesCheatRange + 1;
		for(int y = (int)nuker.yesCheatRange; y >= (WurstClient.INSTANCE.options.nukerMode == 2
			? 0 : -nuker.yesCheatRange); y--)
			for(int x = (int)nuker.yesCheatRange; x >= -nuker.yesCheatRange - 1; x--)
				for(int z = (int)nuker.yesCheatRange; z >= -nuker.yesCheatRange; z--)
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
						&& currentDistance <= nuker.yesCheatRange)
					{
						if(WurstClient.INSTANCE.options.nukerMode == 1
							&& Block.getIdFromBlock(block) != NukerMod.id)
							continue;
						if(WurstClient.INSTANCE.options.nukerMode == 3
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
		NukerMod nuker =
			(NukerMod)WurstClient.INSTANCE.modManager
				.getModByClass(NukerMod.class);
		for(int y = (int)nuker.normalRange; y >= (WurstClient.INSTANCE.options.nukerMode == 2
			? 0 : -nuker.normalRange); y--)
			for(int x = (int)nuker.normalRange; x >= -nuker.normalRange - 1; x--)
				for(int z = (int)nuker.normalRange; z >= -nuker.normalRange; z--)
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
						&& currentDistance <= nuker.normalRange)
					{
						if(WurstClient.INSTANCE.options.nukerMode == 1
							&& Block.getIdFromBlock(block) != NukerMod.id)
							continue;
						if(WurstClient.INSTANCE.options.nukerMode == 3
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
