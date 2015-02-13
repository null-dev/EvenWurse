/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.utils.MiscUtils;

public class Bind extends Command
{
	private static String[] commandHelp =
	{
		"Changes a keybind or lists all keybinds.",
		".bind <mod> <key>",
		".bind <mod> remove",
		".bind list",
		".bind list <page>"
	};
	
	public Bind()
	{
		super("bind", commandHelp);
	}
	
	private int bindsPerPage = 8;
	
	@Override
	public void onEnable(String input, String[] args)
	{
		if(args[0].equalsIgnoreCase("list"))
		{
			int totalBinds = 0;
			for(int i = 0; i < Client.wurst.moduleManager.activeModules.size(); i++)
				if(Client.wurst.moduleManager.activeModules.get(i).getBind() != 0)
					totalBinds++;
			float pagesF = (float)((double)totalBinds / (double)bindsPerPage);
			int pages = (int)(Math.round(pagesF) == pagesF ? pagesF : pagesF + 1);
			bindsPerPage = 8;
			if(args.length == 1)
			{
				if(pages <= 1)
				{
					bindsPerPage = totalBinds;
					Client.wurst.chat.message("Current binds: " + totalBinds);
					int i2 = 0;
					for(int i = 0; i < Client.wurst.moduleManager.activeModules.size() && i2 < bindsPerPage; i++)
						if(Client.wurst.moduleManager.activeModules.get(i).getBind() != 0)
						{
							Client.wurst.chat.message(Client.wurst.moduleManager.activeModules.get(i).getName() + ": " + Keyboard.getKeyName(Client.wurst.moduleManager.activeModules.get(i).getBind()));
							i2++;
						}
				}else
				{
					Client.wurst.chat.message("Current binds: " + totalBinds);
					Client.wurst.chat.message("Bind list (page 1/" + pages + "):");
					int i2 = 0;
					for(int i = 0; i < Client.wurst.moduleManager.activeModules.size() && i2 < bindsPerPage; i++)
						if(Client.wurst.moduleManager.activeModules.get(i).getBind() != 0)
						{
							Client.wurst.chat.message(Client.wurst.moduleManager.activeModules.get(i).getName() + ": " + Keyboard.getKeyName(Client.wurst.moduleManager.activeModules.get(i).getBind()));
							i2++;
						}
				}
			}else
			{
				if(MiscUtils.isInteger(args[1]))
				{
					int page = Integer.valueOf(args[1]);
					if(page > pages || page == 0)
					{
						commandError();
						return;
					}
					Client.wurst.chat.message("Current binds: " + Integer.toString(totalBinds));
					Client.wurst.chat.message("Bind list (page " + page + "/" + pages + "):");
					int i2 = 0;
					for(int i = 0; i < Client.wurst.moduleManager.activeModules.size() && i2 < (page - 1) * bindsPerPage + bindsPerPage; i++)
						if(Client.wurst.moduleManager.activeModules.get(i).getBind() != 0)
						{
							if(i2 >= (page - 1) * bindsPerPage)
								Client.wurst.chat.message(Client.wurst.moduleManager.activeModules.get(i).getName() + ": " + Keyboard.getKeyName(Client.wurst.moduleManager.activeModules.get(i).getBind()));
							i2++;
						}
					return;
				}
				commandError();
			}
		}else if(args[1].equalsIgnoreCase("remove"))
		{
			for(int i = 0; i < Client.wurst.moduleManager.activeModules.size(); i++)
				if(Client.wurst.moduleManager.activeModules.get(i).getName().toLowerCase().equals(args[0].toLowerCase()))
				{
					Client.wurst.moduleManager.activeModules.get(i).setBind(0);
					Client.wurst.fileManager.saveModules();
					Client.wurst.chat.message("Removed keybind for \"" + args[0] + "\".");
					return;
				}
			commandError();
		}else
		{
			for(int i = 0; i < Client.wurst.moduleManager.activeModules.size(); i++)
				if(Client.wurst.moduleManager.activeModules.get(i).getName().toLowerCase().equals(args[0].toLowerCase()))
				{
					Client.wurst.moduleManager.activeModules.get(i).setBind(Keyboard.getKeyIndex(args[1].toUpperCase()));
					Client.wurst.fileManager.saveModules();
					Client.wurst.chat.message("Changed keybind for \"" + args[0] + "\" to " + args[1] + ".");
					return;
				}
			commandError();
		}
	}
}
