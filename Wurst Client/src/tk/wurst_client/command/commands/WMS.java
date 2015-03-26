/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.command.Command.Info;

@Info(help = "Enables/disables Wurst messages or sends a message.",
	name = "wms",
	syntax = {"(on | off)", "echo <message>"})
public class WMS extends Command
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length == 0)
			syntaxError();
		if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off"))
			Client.wurst.chat.setEnabled(args[0].equalsIgnoreCase("on"));
		else if(args[0].equalsIgnoreCase("echo"))
		{
			String message = args[1];
			for(int i = 2; i < args.length; i++)
				message += " " + args[i];
			Client.wurst.chat.cmd(message);
		}else
			syntaxError();
	}
}
