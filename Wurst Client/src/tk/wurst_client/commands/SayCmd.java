/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Sends a chat message, even if the message starts with a dot.",
	name = "say",
	syntax = {"<message>"})
public class SayCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length > 0)
		{
			String message = args[0];
			for(int i = 1; i < args.length; i++)
				message += " " + args[i];
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C01PacketChatMessage(message));
		}else
			syntaxError("Message required.");
	}
}
