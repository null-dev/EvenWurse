/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
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

@Info(category = Category.MOVEMENT, description = "Suspends all motion updates while enabled.\n"
		+ "Can be used for teleportation, instant picking up of items and more.", name = "Blink")
public class BlinkMod extends Mod {
	private static ArrayList<Packet> packets = new ArrayList<Packet>();
	private EntityOtherPlayerMP fakePlayer = null;
	private double oldX;
	private double oldY;
	private double oldZ;
	private long startTime;

	@Override
	public String getRenderName() {
		if (fakePlayer != null)
			return "Blink " + Math.abs(startTime - System.currentTimeMillis())
			+ " ms";
		else
			return "Blink";
	}

	@Override
	public void onEnable() {
		startTime = System.currentTimeMillis();

		oldX = Minecraft.getMinecraft().thePlayer.posX;
		oldY = Minecraft.getMinecraft().thePlayer.posY;
		oldZ = Minecraft.getMinecraft().thePlayer.posZ;
		fakePlayer = new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld,
				Minecraft.getMinecraft().thePlayer.getGameProfile());
		fakePlayer.clonePlayer(Minecraft.getMinecraft().thePlayer, true);
		fakePlayer
		.copyLocationAndAnglesFrom(Minecraft.getMinecraft().thePlayer);
		fakePlayer.rotationYawHead = Minecraft.getMinecraft().thePlayer.rotationYawHead;
		Minecraft.getMinecraft().theWorld.addEntityToWorld(-69, fakePlayer);
	}

	@Override
	public void onDisable() {
		for (Packet packet : packets)
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
		packets.clear();
		Minecraft.getMinecraft().theWorld.removeEntityFromWorld(-69);
		fakePlayer = null;
	}

	public static void addToBlinkQueue(Packet packet) {
		packets.add(packet);
	}

	public void cancel() {
		packets.clear();
		Minecraft.getMinecraft().thePlayer.setPositionAndRotation(oldX, oldY,
				oldZ, Minecraft.getMinecraft().thePlayer.rotationYaw,
				Minecraft.getMinecraft().thePlayer.rotationPitch);
		setEnabled(false);
	}
}
