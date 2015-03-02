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

public class RenameForceOPEvenThoughTheNameIsTechnicallyCorrect extends Command
{
	private static String[] commandHelp =
	{
		"You know what this does. Happy typing!",
		"Note that it is case sensitive.",
		"§o.RenameForceOPEvenThoughTheNameIsTechnicallyCorrect§r"
	};
	
	public RenameForceOPEvenThoughTheNameIsTechnicallyCorrect()
	{
		super("RenameForceOPEvenThoughTheNameIsTechnicallyCorrect", commandHelp);
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		Client.wurst.options.renameForceOPEvenThoughTheNameIsTechnicallyCorrect = !Client.wurst.options.renameForceOPEvenThoughTheNameIsTechnicallyCorrect;
		Client.wurst.fileManager.saveOptions();
		Client.wurst.chat.message("Congratulations! You spelled that correctly.");
		Client.wurst.chat.message("Now you need to restart Wurst.");
	}
}
