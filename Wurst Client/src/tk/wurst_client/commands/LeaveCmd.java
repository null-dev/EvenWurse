/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

@Cmd.Info(help = "Leaves the current server.", name = "leave", syntax = {})
public class LeaveCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(!Minecraft.getMinecraft().isIntegratedServerRunning()
			|| Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfo()
				.size() > 1)
		{
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C01PacketChatMessage("§"));
		}
	}
}
