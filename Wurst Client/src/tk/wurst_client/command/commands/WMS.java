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

public class WMS extends Command
{
	public WMS()
	{
		super("wms",
			"Enables/disables Wurst messages or sends a message.",
			"§o.wms§r (on | off)",
			"    echo <message>");
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off"))
			Client.wurst.chat.setEnabled(args[0].equalsIgnoreCase("on"));
		else if(args[0].equalsIgnoreCase("echo"))
			Client.wurst.chat.cmd(input.substring(9));
		else
			commandError();
	}
}
