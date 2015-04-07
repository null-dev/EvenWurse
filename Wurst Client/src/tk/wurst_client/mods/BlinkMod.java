/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Makes it harder for other players to see where you are.\n"
		+ "You have the control of lag in your hands!\n"
		+ "Position will be updated every time you disable and enable it.\n",
	name = "Blink")
public class BlinkMod extends Mod
{
	private static ArrayList<Packet> packets = new ArrayList<Packet>();
	private EntityOtherPlayerMP fakePlayer = null;
	
	@Override
	public void onEnable()
	{
		fakePlayer =
			new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld,
				Minecraft.getMinecraft().thePlayer.getGameProfile());
		fakePlayer
			.copyLocationAndAnglesFrom(Minecraft.getMinecraft().thePlayer);
		fakePlayer.rotationYawHead =
			Minecraft.getMinecraft().thePlayer.rotationYawHead;
		Minecraft.getMinecraft().theWorld.addEntityToWorld(-69, fakePlayer);
	}
	
	@Override
	public void onDisable()
	{
		for(Packet packet : packets)
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
		packets.clear();
		Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-69);
		fakePlayer = null;
	}
	
	public static void addToBlinkQueue(Packet packet)
	{
		packets.add(packet);
	}
}
