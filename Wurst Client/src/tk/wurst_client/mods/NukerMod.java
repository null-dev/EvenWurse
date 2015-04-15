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

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.WurstClient;
import tk.wurst_client.events.EventManager;
import tk.wurst_client.events.listeners.LeftClickListener;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.BLOCKS, description = "Destroys blocks around you.\n"
	+ "Use .nuker mode <mode> to change the mode.", name = "Nuker")
public class NukerMod extends Mod implements LeftClickListener, RenderListener,
	UpdateListener
{
	public static float normalRange = 5F;
	public static float yesCheatRange = 4.25F;
	private float realRange;
	private static Block currentBlock;
	private float currentDamage;
	private EnumFacing side = EnumFacing.UP;
	private byte blockHitDelay = 0;
	public static int id = 0;
	private BlockPos pos;
	private boolean shouldRenderESP;
	private int oldSlot = -1;
	
	@Override
	public String getRenderName()
	{
		if(WurstClient.INSTANCE.options.nukerMode == 1)
			return "IDNuker [" + id + "]";
		else if(WurstClient.INSTANCE.options.nukerMode == 2)
			return "FlatNuker";
		else if(WurstClient.INSTANCE.options.nukerMode == 3)
			return "SmashNuker";
		else
			return "Nuker";
	}
	
	@Override
	public void initSliders()
	{
		sliders.add(new BasicSlider("Nuker range", normalRange, 1, 6, 0.05,
			ValueDisplay.DECIMAL));
	}
	
	@Override
	public void updateSettings()
	{
		normalRange = (float)sliders.get(0).getValue();
		yesCheatRange = Math.min(normalRange, 4.25F);
	}
	
	@Override
	public void onEnable()
	{
		if(WurstClient.INSTANCE.modManager.getModByClass(NukerLegitMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(NukerLegitMod.class)
				.setEnabled(false);
		if(WurstClient.INSTANCE.modManager.getModByClass(SpeedNukerMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(SpeedNukerMod.class)
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
		if(WurstClient.INSTANCE.modManager.getModByClass(YesCheatMod.class).isEnabled())
			realRange = yesCheatRange;
		else
			realRange = normalRange;
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
		BlockUtils.faceBlockPacket(pos);
		if(currentDamage == 0)
		{
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C07PacketPlayerDigging(
					Action.START_DESTROY_BLOCK, pos, side));
			if(WurstClient.INSTANCE.modManager.getModByClass(AutoToolMod.class)
				.isEnabled() && oldSlot == -1)
				oldSlot =
					Minecraft.getMinecraft().thePlayer.inventory.currentItem;
			if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
				|| currentBlock.getPlayerRelativeBlockHardness(
					Minecraft.getMinecraft().thePlayer,
					Minecraft.getMinecraft().theWorld, pos) >= 1)
			{
				currentDamage = 0;
				if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
					&& !WurstClient.INSTANCE.modManager
						.getModByClass(YesCheatMod.class).isEnabled())
					nukeAll();
				else
				{
					shouldRenderESP = true;
					Minecraft.getMinecraft().thePlayer.swingItem();
					Minecraft.getMinecraft().playerController
						.onPlayerDestroyBlock(pos, side);
				}
				return;
			}
		}
		if(WurstClient.INSTANCE.modManager.getModByClass(AutoToolMod.class).isEnabled())
			AutoToolMod.setSlot(pos);
		Minecraft.getMinecraft().thePlayer.sendQueue
			.addToSendQueue(new C0APacketAnimation());
		shouldRenderESP = true;
		BlockUtils.faceBlockPacket(pos);
		currentDamage +=
			currentBlock.getPlayerRelativeBlockHardness(
				Minecraft.getMinecraft().thePlayer,
				Minecraft.getMinecraft().theWorld, pos)
				* (WurstClient.INSTANCE.modManager.getModByClass(FastBreakMod.class)
					.isEnabled() && WurstClient.INSTANCE.options.fastbreakMode == 0
					? FastBreakMod.speed : 1);
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
		}else if(WurstClient.INSTANCE.modManager.getModByClass(FastBreakMod.class)
			.isEnabled() && WurstClient.INSTANCE.options.fastbreakMode == 1)
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C07PacketPlayerDigging(
					Action.STOP_DESTROY_BLOCK, pos, side));
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
		id = 0;
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
			id =
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
		float closestDistance = realRange + 1;
		for(int y = (int)realRange; y >= (WurstClient.INSTANCE.options.nukerMode == 2
			? 0 : -realRange); y--)
			for(int x = (int)realRange; x >= -realRange - 1; x--)
				for(int z = (int)realRange; z >= -realRange; z--)
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
						&& currentDistance <= realRange)
					{
						if(WurstClient.INSTANCE.options.nukerMode == 1
							&& Block.getIdFromBlock(block) != id)
							continue;
						if(WurstClient.INSTANCE.options.nukerMode == 3
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
	
	private void nukeAll()
	{
		for(int y = (int)realRange; y >= (WurstClient.INSTANCE.options.nukerMode == 2
			? 0 : -realRange); y--)
			for(int x = (int)realRange; x >= -realRange - 1; x--)
				for(int z = (int)realRange; z >= -realRange; z--)
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
					fakeObjectMouseOver.setBlockPos(blockPos);
					if(Block.getIdFromBlock(block) != 0 && posY >= 0
						&& currentDistance <= realRange)
					{
						if(WurstClient.INSTANCE.options.nukerMode == 1
							&& Block.getIdFromBlock(block) != id)
							continue;
						if(WurstClient.INSTANCE.options.nukerMode == 3
							&& block.getPlayerRelativeBlockHardness(
								Minecraft.getMinecraft().thePlayer,
								Minecraft.getMinecraft().theWorld, blockPos) < 1)
							continue;
						side = fakeObjectMouseOver.sideHit;
						shouldRenderESP = true;
						BlockUtils.faceBlockPacket(pos);
						Minecraft.getMinecraft().thePlayer.sendQueue
							.addToSendQueue(new C07PacketPlayerDigging(
								Action.START_DESTROY_BLOCK, blockPos, side));
						block.onBlockDestroyedByPlayer(
							Minecraft.getMinecraft().theWorld, blockPos,
							Minecraft.getMinecraft().theWorld
								.getBlockState(blockPos));
					}
				}
	}
}
