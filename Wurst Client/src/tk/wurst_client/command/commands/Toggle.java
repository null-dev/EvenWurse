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
import tk.wurst_client.module.Mod;

public class Toggle extends Command
{
	private static String[] commandHelp =
	{
		"Toggles a mod.",
		"§o.t§r <mod> [(on | off)]"
	};
	
	public Toggle()
	{
		super("t", commandHelp);
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		int mode;
		if(args.length == 1)
			mode = 0;
		else if(args.length == 2 && args[1].equalsIgnoreCase("on"))
			mode = 1;
		else if(args.length == 2 && args[1].equalsIgnoreCase("off"))
			mode = 2;
		else
		{
			commandError();
			return;
		}
		Mod mod = Client.wurst.modManager.getModByName(args[0]);
		if(mod == null)
		{
			Client.wurst.chat.error("Could not find mod \"" + args[0] + "\".");
			return;
		}
		if(mode == 0)
			mod.toggle();
		else if(mode == 1)
			mod.setToggled(true);
		else if(mode == 2)
			mod.setToggled(false);
	}
}
