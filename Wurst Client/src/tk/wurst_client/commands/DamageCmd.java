/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import tk.wurst_client.utils.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;

@Cmd.Info(help = "Damages you with given amount.",
	name = "damage",
	syntax = {"<amount>"})
public class DamageCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length == 0)
			syntaxError();
		if(!MiscUtils.isInteger(args[0]))
			syntaxError("Amount must be a number.");
		if(Minecraft.getMinecraft().isIntegratedServerRunning()
			&& Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfo()
				.size() == 1)
			error("Cannot damage in singleplayer.");
		if(Minecraft.getMinecraft().thePlayer.isOnLadder())
			error("Cannot damage while climbing ladders.");
		if(!Minecraft.getMinecraft().thePlayer.onGround)
			error("Cannot damage in mid-air.");
		if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
			error("Cannot damage in creative mode.");
		final double dmg = Double.parseDouble(args[0]);
		if(dmg < 1)
			error("Amount must be at least 1.");
		if(dmg > 40)
			error("Amount must be at most 40.");
		final double x = Minecraft.getMinecraft().thePlayer.posX;
		final double y = Minecraft.getMinecraft().thePlayer.posY;
		final double z = Minecraft.getMinecraft().thePlayer.posZ;
		Minecraft
			.getMinecraft()
			.getNetHandler()
			.addToSendQueue(
				new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.3, z,
					false));
		Minecraft
			.getMinecraft()
			.getNetHandler()
			.addToSendQueue(
				new C03PacketPlayer.C04PacketPlayerPosition(x, y - 3.1 - dmg,
					z, false));
		Minecraft
			.getMinecraft()
			.getNetHandler()
			.addToSendQueue(
				new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
	}
}
