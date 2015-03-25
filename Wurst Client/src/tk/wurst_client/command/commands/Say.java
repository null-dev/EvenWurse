/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import tk.wurst_client.command.Command;

public class Say extends Command
{
	public Say()
	{
		super("say", "Sends a chat message, even if the message starts",
			"with a dot.",
			"§o.say§r <message>");
	}
	
	@Override
	public void execute(String input, String[] args)
	{
		if(input.length() > 4)
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C01PacketChatMessage(input.substring(4)));
		else
			commandError();
	}
}
