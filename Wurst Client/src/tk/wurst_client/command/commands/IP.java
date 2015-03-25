/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.command.Command.Info;

@Info(help = "Shows the IP of the server you are currently playing on or copies it to the clipboard.",
	name = "ip",
	syntax = {"[copy]"})
public class IP extends Command
{
	@Override
	public void execute(String[] args)
	{
		if(args.length == 0)
			Client.wurst.chat.message("IP: " + Client.wurst.currentServerIP);
		else if(args[0].toLowerCase().equals("copy"))
		{
			Toolkit
				.getDefaultToolkit()
				.getSystemClipboard()
				.setContents(new StringSelection(Client.wurst.currentServerIP),
					null);
			Client.wurst.chat.message("IP copied to clipboard.");
		}else
			commandError();
	}
}
