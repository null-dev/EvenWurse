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
import tk.wurst_client.utils.MiscUtils;

public class Help extends Command
{
	public Help()
	{
		super("help",
			"Shows the command list or the help for a command.",
			"§o.help§r <page>",
			"    <command>");
	}
	
	private int commandsPerPage = 8;
	
	@Override
	public void onEnable(String input, String[] args)
	{
		commandsPerPage = 8;
		float pagesF = (float)Client.wurst.commandManager.activeCommands.size() / commandsPerPage;
		int pages = (int)(Math.floor(pagesF) == pagesF ? pagesF : pagesF + 1);
		if(args == null)
		{
			if(pages <= 1)
			{
				commandsPerPage = Client.wurst.commandManager.activeCommands.size();
				Client.wurst.chat.message("Available commands: " + Integer.toString(Client.wurst.commandManager.activeCommands.size()));
				for(int i = 0; i < commandsPerPage; i++)
					Client.wurst.chat.message("." + Client.wurst.commandManager.activeCommands.get(i).getName());
			}else
			{
				Client.wurst.chat.message("Available commands: " + Integer.toString(Client.wurst.commandManager.activeCommands.size()));
				Client.wurst.chat.message("Command list (page 1/" + pages + "):");
				for(int i = 0; i < commandsPerPage; i++)
					Client.wurst.chat.message("." + Client.wurst.commandManager.activeCommands.get(i).getName());
			}
		}else
		{
			for(int i = 0; i < Client.wurst.commandManager.activeCommands.size(); i++)
				if(Client.wurst.commandManager.activeCommands.get(i).getName().equals(args[0]))
				{
					Client.wurst.chat.message("Available help for ." + args[0] + ":");
					for(int i2 = 0; i2 < Client.wurst.commandManager.activeCommands.get(i).getHelp().length; i2++)
						Client.wurst.chat.message(Client.wurst.commandManager.activeCommands.get(i).getHelp()[i2]);
					return;
				}else if(MiscUtils.isInteger(args[0]))
				{
					int page = Integer.valueOf(args[0]);
					if(page > pages || page == 0)
					{
						commandError();
						return;
					}
					Client.wurst.chat.message("Available commands: " + Integer.toString(Client.wurst.commandManager.activeCommands.size()));
					Client.wurst.chat.message("Command list (page " + page + "/" + pages + "):");
					for(int i2 = (page - 1) * commandsPerPage; i2 < (page - 1) * commandsPerPage + commandsPerPage && i2 < Client.wurst.commandManager.activeCommands.size(); i2++)
						Client.wurst.chat.message("." + Client.wurst.commandManager.activeCommands.get(i2).getName());
					return;
				}
			commandError();
		}
	}
}
