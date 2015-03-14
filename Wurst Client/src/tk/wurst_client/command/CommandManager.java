/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command;

import java.util.ArrayList;

import tk.wurst_client.Client;
import tk.wurst_client.command.commands.*;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.events.ChatOutputEvent;
import tk.wurst_client.event.listeners.ChatOutputListener;

public class CommandManager implements ChatOutputListener
{
	public ArrayList<Command> activeCommands = new ArrayList<Command>();

	@Override
	public void onSentMessage(ChatOutputEvent event)
	{
      	if(event.getMessage().startsWith("."))
      	{
      		event.cancel();
      		String input = event.getMessage().substring(1);
      		String command = input.split(" ")[0];
      	    for(Command eventCommand : Client.wurst.commandManager.activeCommands)
      	    {
      	        if(eventCommand.getName().equals(command))
      	        {
      	        	try
      	        	{
      	        		String[] args = input.contains(" ") ? input.substring(input.indexOf(" ") + 1).split(" ") : new String[0];
      	        		eventCommand.onEnable(input, args);
      	        	} catch (Exception e)
      	        	{
      	        		eventCommand.commandError();
      	        	}
      	        	return;
      	        }
      	    }
  	        Client.wurst.chat.message("\"." + command + "\" is not a valid command.");
  	        Client.wurst.chat.message("Type \".help\" to see the command list.");
      	}
	}
	
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
		activeCommands.add(new GoTo());
		activeCommands.add(new Help());
		activeCommands.add(new Invsee());
		activeCommands.add(new IP());
		activeCommands.add(new Nothing());
		activeCommands.add(new NukerMod());
		activeCommands.add(new Path());
		activeCommands
			.add(new RenameForceOPEvenThoughTheNameIsTechnicallyCorrect());
		activeCommands.add(new RV());
		activeCommands.add(new Say());
		activeCommands.add(new SearchMod());
		activeCommands.add(new SpammerMod());
		activeCommands.add(new Taco());
		activeCommands.add(new ThrowMod());
		activeCommands.add(new Toggle());
		activeCommands.add(new TP());
		activeCommands.add(new VClip());
		activeCommands.add(new WMS());
		activeCommands.add(new XRay());
		EventManager.addChatOutputListener(this);
	}
}
