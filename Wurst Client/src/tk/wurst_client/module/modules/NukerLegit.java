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
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.RenderListener;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.RenderUtils;

public class NukerLegit extends Module implements UpdateListener, RenderListener
{
	private static Block currentBlock;
	private float currentDamage;
	private EnumFacing side = EnumFacing.UP;
	private byte blockHitDelay = 0;
	private BlockPos pos;
	private boolean shouldRenderESP;
	private int oldSlot = -1;
	
	public NukerLegit()
	{
		super(
			"NukerLegit",
			"Slower Nuker that bypasses any cheat prevention\n"
				+ "PlugIn. Not required on most NoCheat+ servers!",
			Category.BLOCKS);
	}
	
	@Override
	public String getRenderName()
	{
		if(Client.wurst.options.nukerMode == 1)
			return "IDNukerLegit [" + Nuker.id + "]";
		else if(Client.wurst.options.nukerMode == 2)
			return "FlatNukerLegit";
		else if(Client.wurst.options.nukerMode == 3)
			return "SmashNukerLegit";
		else
			return "NukerLegit";
	}
	
	@Override
	public void onEnable()
	{
		if(Client.wurst.moduleManager.getMod(Nuker.class)
			.getToggled())
			Client.wurst.moduleManager.getMod(Nuker.class)
				.setToggled(false);
		if(Client.wurst.moduleManager.getMod(SpeedNuker.class)
			.getToggled())
			Client.wurst.moduleManager.getMod(SpeedNuker.class)
				.setToggled(false);
		EventManager.addUpdateListener(this);
		EventManager.addRenderListener(this);
	}
	
	@Override
	public void onRender()
	{
		if(blockHitDelay == 0 && shouldRenderESP)
			if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
				&& currentBlock.getPlayerRelativeBlockHardness(
					Minecraft.getMinecraft().thePlayer,
					Minecraft.getMinecraft().theWorld, pos) < 1)
				RenderUtils.nukerBox(pos, currentDamage);
			else
				RenderUtils.nukerBox(pos, 1);
	}
	
	@Override
	public void onUpdate()
	{
		shouldRenderESP = false;
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
		if(pos == null || !pos.equals(newPos))
			currentDamage = 0;
		pos = newPos;
		currentBlock =
			Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
		if(blockHitDelay > 0)
		{
			blockHitDelay--;
			return;
		}
		BlockUtils.faceBlockClient(pos);
		if(currentDamage == 0)
		{
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C07PacketPlayerDigging(
					Action.START_DESTROY_BLOCK, pos, side));
			if(Client.wurst.moduleManager.getMod(AutoTool.class)
				.getToggled() && oldSlot == -1)
				oldSlot =
					Minecraft.getMinecraft().thePlayer.inventory.currentItem;
			if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
				|| currentBlock.getPlayerRelativeBlockHardness(
					Minecraft.getMinecraft().thePlayer,
					Minecraft.getMinecraft().theWorld, pos) >= 1)
			{
				currentDamage = 0;
				shouldRenderESP = true;
				Minecraft.getMinecraft().thePlayer.swingItem();
				Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(
					pos, side);
				blockHitDelay = (byte)4;
				return;
			}
		}
		if(Client.wurst.moduleManager.getMod(AutoTool.class)
			.getToggled())
			AutoTool.setSlot(pos);
		Minecraft.getMinecraft().thePlayer.sendQueue
			.addToSendQueue(new C0APacketAnimation());
		shouldRenderESP = true;
		currentDamage +=
			currentBlock.getPlayerRelativeBlockHardness(
				Minecraft.getMinecraft().thePlayer,
				Minecraft.getMinecraft().theWorld, pos);
		Minecraft.getMinecraft().theWorld.sendBlockBreakProgress(
			Minecraft.getMinecraft().thePlayer.getEntityId(), pos,
			(int)(currentDamage * 10.0F) - 1);
		if(currentDamage >= 1)
		{
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C07PacketPlayerDigging(
					Action.STOP_DESTROY_BLOCK, pos, side));
			Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(pos,
				side);
			blockHitDelay = (byte)4;
			currentDamage = 0;
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
		EventManager.removeRenderListener(this);
		if(oldSlot != -1)
		{
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
			oldSlot = -1;
		}
		currentDamage = 0;
		shouldRenderESP = false;
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
						side = fakeObjectMouseOver.sideHit;
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
}
