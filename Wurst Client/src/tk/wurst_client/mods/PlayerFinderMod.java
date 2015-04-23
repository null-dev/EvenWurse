/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.BlockPos;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.PacketInputEvent;
import tk.wurst_client.events.listeners.PacketInputListener;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Finds far players during thunderstorms.",
	name = "PlayerFinder")
public class PlayerFinderMod extends Mod implements PacketInputListener,
	RenderListener
{
	private BlockPos blockPos;
	
	@Override
	public void onEnable()
	{
		blockPos = null;
		WurstClient.INSTANCE.eventManager.add(PacketInputListener.class, this);
		WurstClient.INSTANCE.eventManager.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		if(blockPos == null)
			return;
		float red =
			(1F - (float)Math.sin((float)(System.currentTimeMillis() % 1000L)
				/ 1000L * Math.PI * 2)) / 2F;
		float green =
			(1F - (float)Math
				.sin((float)((System.currentTimeMillis() + 333L) % 1000L)
					/ 1000L * Math.PI * 2)) / 2F;
		float blue =
			(1F - (float)Math
				.sin((float)((System.currentTimeMillis() + 666L) % 1000L)
					/ 1000L * Math.PI * 2)) / 2F;
		Color color = new Color(red, green, blue);
		RenderUtils.tracerLine(blockPos.getX(), blockPos.getY(),
			blockPos.getZ(), color);
		RenderUtils.blockESPBox(blockPos);
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(PacketInputListener.class,
			this);
		WurstClient.INSTANCE.eventManager.remove(RenderListener.class, this);
	}
	
	@Override
	public void onReceivedPacket(PacketInputEvent event)
	{
		if(Minecraft.getMinecraft().thePlayer == null)
			return;
		Packet packet = event.getPacket();
		if(packet instanceof S28PacketEffect)
		{
			S28PacketEffect effect = (S28PacketEffect)packet;
			BlockPos pos = effect.func_179746_d();
			if(BlockUtils.getPlayerBlockDistance(pos) >= 160)
				blockPos = pos;
		}else if(packet instanceof S29PacketSoundEffect)
		{
			S29PacketSoundEffect sound = (S29PacketSoundEffect)packet;
			BlockPos pos =
				new BlockPos(sound.func_149207_d(), sound.func_149211_e(),
					sound.func_149210_f());
			if(BlockUtils.getPlayerBlockDistance(pos) >= 160)
				blockPos = pos;
		}else if(packet instanceof S2CPacketSpawnGlobalEntity)
		{
			S2CPacketSpawnGlobalEntity lightning =
				(S2CPacketSpawnGlobalEntity)packet;
			BlockPos pos =
				new BlockPos(lightning.func_149051_d() / 32D,
					lightning.func_149050_e() / 32D,
					lightning.func_149049_f() / 32D);
			if(BlockUtils.getPlayerBlockDistance(pos) >= 160)
				blockPos = pos;
		}
	}
}
