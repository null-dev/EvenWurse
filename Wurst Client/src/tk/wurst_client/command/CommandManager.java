/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

import tk.wurst_client.Client;
import tk.wurst_client.command.commands.*;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.events.ChatOutputEvent;
import tk.wurst_client.event.listeners.ChatOutputListener;
import tk.wurst_client.mod.Mod.Info;

public class CommandManager implements ChatOutputListener
{
	@Deprecated
	public ArrayList<Command> activeCommands = new ArrayList<Command>();
	private final TreeMap<String, Command> commands = new TreeMap<String, Command>(
		new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				return o1.compareToIgnoreCase(o2);
			}
		});
	
	@Override
	public void onSentMessage(ChatOutputEvent event)
	{
		if(event.getMessage().startsWith("."))
		{
			event.cancel();
			String input = event.getMessage().substring(1);
			String command = input.split(" ")[0];
			for(Command eventCommand : Client.wurst.commandManager.activeCommands)
				if(eventCommand.getName().equals(command))
				{
					try
					{
						String[] args =
							input.contains(" ") ? input.substring(
								input.indexOf(" ") + 1).split(" ")
								: new String[0];
						eventCommand.onEnable(input, args);
					}catch(Exception e)
					{
						EventManager.handleException(e, eventCommand,
							"executing", "Exact input: `" + event.getMessage()
								+ "`");
					}
					return;
				}
			Client.wurst.chat.message("\"." + command
				+ "\" is not a valid command.");
			Client.wurst.chat
				.message("Type \".help\" to see the command list.");
		}
	}
	
	public CommandManager()
	{
		addCommand(new AddAlt());
		addCommand(new Annoy());
		addCommand(new Binds());
		addCommand(new Clear());
		addCommand(new Drop());
		addCommand(new Enchant());
		addCommand(new FastBreakMod());
		addCommand(new Features());
		addCommand(new Friends());
		addCommand(new GM());
		addCommand(new GoTo());
		addCommand(new Help());
		addCommand(new Invsee());
		addCommand(new IP());
		addCommand(new Nothing());
		addCommand(new NukerMod());
		addCommand(new Path());
		addCommand(new RV());
		addCommand(new Say());
		addCommand(new SearchMod());
		addCommand(new SpammerMod());
		addCommand(new Taco());
		addCommand(new ThrowMod());
		addCommand(new Toggle());
		addCommand(new TP());
		addCommand(new VClip());
		addCommand(new WMS());
		addCommand(new XRay());
		EventManager.chatOutput.addListener(this);
	}
	
	public Command getCommandByClass(Class<?> commandClass)
	{
		return commands.get(commandClass.getAnnotation(Info.class).name());
	}
	
	public Command getCommandByName(String name)
	{
		return commands.get(name);
	}
	
	public Collection<Command> getAllCommands()
	{
		return commands.values();
	}
	
	public int countCommand()
	{
		return commands.size();
	}
	
	private void addCommand(Command commmand)
	{
		commands.put(commmand.getName(), commmand);
	}
}
