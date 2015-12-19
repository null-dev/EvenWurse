/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
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
		if(WurstClient.INSTANCE.mods.nukerMod.isEnabled())
			WurstClient.INSTANCE.mods.nukerMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.nukerLegitMod.isEnabled())
			WurstClient.INSTANCE.mods.nukerLegitMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.tunnellerMod.isEnabled())
			WurstClient.INSTANCE.mods.tunnellerMod.setEnabled(false);
		WurstClient.INSTANCE.events.add(LeftClickListener.class, this);
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(WurstClient.INSTANCE.mods.yesCheatMod.isActive())
		{
			noCheatMessage();
			setEnabled(false);
			WurstClient.INSTANCE.chat.message("Switching to "
				+ WurstClient.INSTANCE.mods.nukerMod.getName() + ".");
			WurstClient.INSTANCE.mods.nukerMod.setEnabled(true);
			return;
		}
		if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
		{
			WurstClient.INSTANCE.chat.error(getName()
				+ " doesn't work in creative mode.");
			setEnabled(false);
			WurstClient.INSTANCE.chat.message("Switching to "
				+ WurstClient.INSTANCE.mods.nukerMod.getName() + ".");
			WurstClient.INSTANCE.mods.nukerMod.setEnabled(true);
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
		Block currentBlock = Minecraft.getMinecraft().theWorld.getBlockState(newPos).getBlock();
		if(WurstClient.INSTANCE.mods.autoToolMod.isActive() && oldSlot == -1)
			oldSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
			&& WurstClient.INSTANCE.mods.autoToolMod.isActive()
			&& currentBlock.getPlayerRelativeBlockHardness(
				Minecraft.getMinecraft().thePlayer,
				Minecraft.getMinecraft().theWorld, newPos) < 1)
			AutoToolMod.setSlot(newPos);
		nukeAll();
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(LeftClickListener.class, this);
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
		if(oldSlot != -1)
		{
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
			oldSlot = -1;
		}
		NukerMod.id = 0;
		WurstClient.INSTANCE.files.saveOptions();
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
			WurstClient.INSTANCE.files.saveOptions();
		}
	}
	
	private BlockPos find()
	{
		BlockPos closest = null;
		float closestDistance =
			WurstClient.INSTANCE.mods.nukerMod.yesCheatRange + 1;
		for(int y = (int)WurstClient.INSTANCE.mods.nukerMod.yesCheatRange; y >= (WurstClient.INSTANCE.options.nukerMode == 2
			? 0 : -WurstClient.INSTANCE.mods.nukerMod.yesCheatRange); y--)
			for(int x = (int)WurstClient.INSTANCE.mods.nukerMod.yesCheatRange; x >= -WurstClient.INSTANCE.mods.nukerMod.yesCheatRange - 1; x--)
				for(int z =
					(int)WurstClient.INSTANCE.mods.nukerMod.yesCheatRange; z >= -WurstClient.INSTANCE.mods.nukerMod.yesCheatRange; z--)
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
					if(Block.getIdFromBlock(block) != 0
						&& posY >= 0
						&& currentDistance <= WurstClient.INSTANCE.mods.nukerMod.yesCheatRange)
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
		for(int y = (int)WurstClient.INSTANCE.mods.nukerMod.normalRange; y >= (WurstClient.INSTANCE.options.nukerMode == 2
			? 0 : -WurstClient.INSTANCE.mods.nukerMod.normalRange); y--)
			for(int x = (int)WurstClient.INSTANCE.mods.nukerMod.normalRange; x >= -WurstClient.INSTANCE.mods.nukerMod.normalRange - 1; x--)
				for(int z = (int)WurstClient.INSTANCE.mods.nukerMod.normalRange; z >= -WurstClient.INSTANCE.mods.nukerMod.normalRange; z--)
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
					if(Block.getIdFromBlock(block) != 0
						&& posY >= 0
						&& currentDistance <= WurstClient.INSTANCE.mods.nukerMod.normalRange)
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
