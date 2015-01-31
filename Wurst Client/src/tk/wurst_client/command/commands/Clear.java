/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.client.Minecraft;
import tk.wurst_client.command.Command;

public class Clear extends Command
{
	private static String[] commandHelp =
	{
		"Clears the chat completely.",
		".clear"
	};

	public Clear()
	{
		super("clear", commandHelp);
	}

	@Override
	public void onEnable(String input, String[] args)
	{
		if(args == null)
			Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
		else
			commandError();
	}
}
