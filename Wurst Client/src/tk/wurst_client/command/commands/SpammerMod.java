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
import tk.wurst_client.mod.mods.Spammer;
import tk.wurst_client.spam.SpamProcessor;

public class SpammerMod extends Command
{
	private static String[] commandHelp =
	{
		"Changes the delay of Spammer or spams spam from a file.",
		"§o.spammer§r delay <delay in ms>",
		"    spam <file>"
	};
	
	public SpammerMod()
	{
		super("spammer", commandHelp);
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		if(args.length < 2)
		{
			commandError();
			return;
		}
		if(args[0].equalsIgnoreCase("delay"))
		{
			int newDelay = Integer.parseInt(args[1]);
			if(newDelay % 50 > 0)
				newDelay = newDelay - newDelay % 50;
			Client.wurst.options.spamDelay = newDelay;
			Spammer.updateDelaySpinner();
			Client.wurst.chat.message("Spammer delay set to " + newDelay
				+ "ms.");
		}else if(args[0].equalsIgnoreCase("spam"))
			if(!SpamProcessor.runSpam(args[1]))
				Client.wurst.chat.error("File does not exist.");
	}
}
