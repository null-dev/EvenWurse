
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
import net.minecraft.util.ChatComponentText;
import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Generates a fake message into the chat screen. Use $ for colors, use $$ for $.",
	name = "fakemsg",
	syntax = {"Message"})
public class FakeMsgCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{	
		if(args.length == 0)
			syntaxError();
		String message = args[0];
		for(int i = 1; i < args.length; i++)
			message += " " + args[i];
		message = message.replace("$", "§").replace("§§", "$");
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
				new ChatComponentText(message));
	}
}
