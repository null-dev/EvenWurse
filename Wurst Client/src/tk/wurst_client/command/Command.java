/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command;

import tk.wurst_client.Client;

public class Command
{
	private String commandName;
	
	private String[] commandHelp;

	public Command(String commandName, String[] commandHelp)
	{
		this.commandName = commandName;
		this.commandHelp = commandHelp;
	}

	public String getName()
	{
		return commandName;
	}

	public String[] getHelp()
	{
		return commandHelp;
	}

	public void commandError()
	{
		Client.Wurst.chat.error("Something went wrong.");
		Client.Wurst.chat.message("If you need help, type \".help " + commandName + "\".");
	}

	public void onEnable(String input, String[] args)
	{}
}
