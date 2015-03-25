/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

import tk.wurst_client.Client;
import tk.wurst_client.command.Command.Info;
import tk.wurst_client.command.commands.*;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.events.ChatOutputEvent;
import tk.wurst_client.event.listeners.ChatOutputListener;

public class CommandManager implements ChatOutputListener
{
	private final TreeMap<String, Command> commands =
		new TreeMap<String, Command>(
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
		String message = event.getMessage();
		if(message.startsWith("."))
		{
			event.cancel();
			String input = message.substring(1);
			String commandName = input.split(" ")[0];
			String[] args;
			if(input.contains(" "))
				args = input.substring(input.indexOf(" ") + 1).split(" ");
			else
				args = new String[0];
			Command command = getCommandByName(commandName);
			if(command != null)
				try
				{
					command.execute(args);
				}catch(CommandSyntaxException e)
				{
					if(e.getMessage() != null)
						Client.wurst.chat.message("§4Syntax error:§r " + e.getMessage());
					else
						Client.wurst.chat.message("§4Syntax error!§r");
				}catch(Exception e)
				{
					EventManager.handleException(e, command, "executing",
						"Exact input: `" + event.getMessage() + "`");
				}
			else
				Client.wurst.chat.error("\"." + commandName
					+ "\" is not a valid command.");
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
		addCommand(new XRayMod());
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
	
	public int countCommands()
	{
		return commands.size();
	}
	
	private void addCommand(Command commmand)
	{
		commands.put(commmand.getName(), commmand);
	}
}
