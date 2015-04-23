/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import tk.wurst_client.WurstClient;
import tk.wurst_client.ai.PathUtils;
import tk.wurst_client.mods.YesCheatMod;
import tk.wurst_client.utils.MiscUtils;

@Cmd.Info(help = "Applies the given amount of damage.",
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
		if(Minecraft.getMinecraft().thePlayer.isOnLadder())
			error("Cannot damage while climbing ladders.");
		if(!Minecraft.getMinecraft().thePlayer.onGround)
			error("Cannot damage in mid-air.");
		if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
			error("Cannot damage in creative mode.");
		int dmg = Integer.parseInt(args[0]);
		if(dmg < 1)
			error("Amount must be at least 1.");
		if(dmg > 40)
			error("Amount must be at most 40.");
		double x = Minecraft.getMinecraft().thePlayer.posX;
		double y = Minecraft.getMinecraft().thePlayer.posY;
		double z = Minecraft.getMinecraft().thePlayer.posZ;
		if(WurstClient.INSTANCE.modManager.getModByClass(YesCheatMod.class)
			.isEnabled())
		{
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
					new C03PacketPlayer.C04PacketPlayerPosition(x, y - 3.1
						- dmg, z, false));
			Minecraft
				.getMinecraft()
				.getNetHandler()
				.addToSendQueue(
					new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
		}else
		{
			for(int i = 1; i < dmg + 5; i++)
			{
				if(PathUtils.isSolid(new BlockPos(x, y + i, z)))
				{
					if(i < 6)
						error("Not enough space. Cannot apply any damage.");
					else
					{
						WurstClient.INSTANCE.chat.warning("Not enough space. Can only apply " + (i - 5) + " of " + dmg + " damage.");
						dmg = i - 6;
						break;
					}
				}
			}
			for(int i = 1; i < dmg + 5; i++)
			{
				Minecraft
					.getMinecraft()
					.getNetHandler()
					.addToSendQueue(
						new C03PacketPlayer.C04PacketPlayerPosition(x, y + i,
							z, false));
			}
			for(int i = dmg + 4; i > 0; i--)
			{
				Minecraft
					.getMinecraft()
					.getNetHandler()
					.addToSendQueue(
						new C03PacketPlayer.C04PacketPlayerPosition(x, y + i,
							z, false));
			}
			Minecraft
				.getMinecraft()
				.getNetHandler()
				.addToSendQueue(
					new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
		}
	}
}
