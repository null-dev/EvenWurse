/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command;

import java.util.ArrayList;

import tk.wurst_client.command.commands.*;

public class CommandManager
{
	public ArrayList<Command> activeCommands = new ArrayList<Command>();
	
	public CommandManager()
	{
		activeCommands.add(new AddAlt());
		activeCommands.add(new Annoy());
		activeCommands.add(new Binds());
		activeCommands.add(new Clear());
		activeCommands.add(new Drop());
		activeCommands.add(new Enchant());
		activeCommands.add(new FastBreakMod());
		activeCommands.add(new Features());
		activeCommands.add(new Friends());
		activeCommands.add(new GM());
		activeCommands.add(new Help());
		activeCommands.add(new IP());
		activeCommands.add(new Nothing());
		activeCommands.add(new NukerMod());
		activeCommands.add(new RenameForceOPEvenThoughTheNameIsTechnicallyCorrect());
		activeCommands.add(new RV());
		activeCommands.add(new Say());
		activeCommands.add(new SearchMod());
		activeCommands.add(new SpammerMod());
		activeCommands.add(new Taco());
		activeCommands.add(new ThrowMod());
		activeCommands.add(new Toggle());
		activeCommands.add(new TP());
		activeCommands.add(new VClip());
		activeCommands.add(new XRay());
	}
}
