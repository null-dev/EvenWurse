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
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.LeftClickListener;
import tk.wurst_client.event.listeners.RenderListener;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.BLOCKS,
	description = "Slower Nuker that bypasses any cheat prevention\n"
		+ "PlugIn. Not required on most NoCheat+ servers!",
	name = "NukerLegit")
public class NukerLegitMod extends Mod implements LeftClickListener,
	RenderListener, UpdateListener
{
	private static Block currentBlock;
	private float currentDamage;
	private EnumFacing side = EnumFacing.UP;
	private byte blockHitDelay = 0;
	private BlockPos pos;
	private boolean shouldRenderESP;
	private int oldSlot = -1;
	
	@Override
	public String getRenderName()
	{
		if(Client.wurst.options.nukerMode == 1)
			return "IDNukerLegit [" + NukerMod.id + "]";
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
		if(Client.wurst.modManager.getModByClass(NukerMod.class)
			.isEnabled())
			Client.wurst.modManager.getModByClass(NukerMod.class)
				.setEnabled(false);
		if(Client.wurst.modManager.getModByClass(SpeedNukerMod.class)
			.isEnabled())
			Client.wurst.modManager.getModByClass(SpeedNukerMod.class)
				.setEnabled(false);
		EventManager.leftClick.addListener(this);
		EventManager.update.addListener(this);
		EventManager.render.addListener(this);
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
			if(Client.wurst.modManager.getModByClass(AutoToolMod.class)
				.isEnabled() && oldSlot == -1)
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
		if(Client.wurst.modManager.getModByClass(AutoToolMod.class)
			.isEnabled())
			AutoToolMod.setSlot(pos);
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
		EventManager.leftClick.removeListener(this);
		EventManager.update.removeListener(this);
		EventManager.render.removeListener(this);
		if(oldSlot != -1)
		{
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
			oldSlot = -1;
		}
		currentDamage = 0;
		shouldRenderESP = false;
		NukerMod.id = 0;
		Client.wurst.fileManager.saveOptions();
	}
	
	@Override
	public void onLeftClick()
	{
		if(Minecraft.getMinecraft().objectMouseOver == null
			|| Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null)
			return;
		if(Client.wurst.options.nukerMode == 1
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
			Client.wurst.fileManager.saveOptions();
		}
	}
	
	private BlockPos find()
	{
		BlockPos closest = null;
		float closestDistance = NukerMod.yesCheatRange + 1;
		for(int y = (int)NukerMod.yesCheatRange; y >= (Client.wurst.options.nukerMode == 2
			? 0 : -NukerMod.yesCheatRange); y--)
			for(int x = (int)NukerMod.yesCheatRange; x >= -NukerMod.yesCheatRange - 1; x--)
				for(int z = (int)NukerMod.yesCheatRange; z >= -NukerMod.yesCheatRange; z--)
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
						&& currentDistance <= NukerMod.yesCheatRange)
					{
						if(Client.wurst.options.nukerMode == 1
							&& Block.getIdFromBlock(block) != NukerMod.id)
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
