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
		".RenameForceOPEvenThoughTheNameIsTechnicallyCorrect"
	};

	public RenameForceOPEvenThoughTheNameIsTechnicallyCorrect()
	{
		super("RenameForceOPEvenThoughTheNameIsTechnicallyCorrect", commandHelp);
	}

	@Override
	public void onEnable(String input, String[] args)
	{
		Client.Wurst.options.renameForceOPEvenThoughTheNameIsTechnicallyCorrect = !Client.Wurst.options.renameForceOPEvenThoughTheNameIsTechnicallyCorrect;
		Client.Wurst.fileManager.saveOptions();
		Client.Wurst.chat.message("Congratulations! You spelled that correctly.");
		Client.Wurst.chat.message("Now you need to restart Wurst.");
	}
}
