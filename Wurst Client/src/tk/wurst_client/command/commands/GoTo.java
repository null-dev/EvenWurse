/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import tk.wurst_client.command.Command;

public class GoTo extends Command
{
	public GoTo()
	{
		super("goto",
			"Walks or flies you to a specific location.",
			"§o.goto§r <x> <y> <z>");
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		
	}
}
