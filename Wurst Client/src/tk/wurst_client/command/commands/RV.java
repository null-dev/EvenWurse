/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import tk.wurst_client.command.Command;
import tk.wurst_client.mod.mods.RemoteView;

public class RV extends Command
{
	public RV()
	{
		super("rv",
			"Toggles RemoteView or makes it target a specific entity.",
			"§o.rv§r [<Player>]");
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		if(args == null)
		{
			RemoteView.onEnabledByCommand("");
			return;
		}
		else
			RemoteView.onEnabledByCommand(args[0]);
	}
}
