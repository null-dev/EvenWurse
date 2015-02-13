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

public class Toggle extends Command
{
	private static String[] commandHelp =
	{
		"Toggles a command.",
		".t <command>"
	};
	
	public Toggle()
	{
		super("t", commandHelp);
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		for(int i = 0; i < Client.wurst.moduleManager.activeModules.size(); i++)
			if(Client.wurst.moduleManager.activeModules.get(i).getName().toLowerCase().equals(args[0].toLowerCase()))
			{
				Client.wurst.moduleManager.activeModules.get(i).toggleModule();
				return;
			}
		commandError();
	}
}
