/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.commands.Cmd.SyntaxError;
import tk.wurst_client.events.ChatOutputEvent;
import tk.wurst_client.events.listeners.ChatOutputListener;

public class CmdManager implements ChatOutputListener
{
	private final TreeMap<String, Cmd> cmds = new TreeMap<String, Cmd>(
		new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				return o1.compareToIgnoreCase(o2);
			}
		});
	
	public CmdManager()
	{
		addCommand(new AddAltCmd());
		addCommand(new AnnoyCmd());
		addCommand(new AuthorCmd());
		addCommand(new BindsCmd());
		addCommand(new BlinkCmd());
		addCommand(new ClearCmd());
		addCommand(new DamageCmd());
		addCommand(new DropCmd());
		addCommand(new EnchantCmd());
		addCommand(new FastBreakCmd());
		addCommand(new FeaturesCmd());
		addCommand(new FollowCmd());
		addCommand(new FriendsCmd());
		addCommand(new GetPosCmd());
		addCommand(new GhostHandCmd());
		addCommand(new GiveCmd());
		addCommand(new GmCmd());
		addCommand(new GoToCmd());
		addCommand(new HelpCmd());
		addCommand(new InvseeCmd());
		addCommand(new IpCmd());
		addCommand(new JumpCmd());
		addCommand(new LeaveCmd());
		addCommand(new NothingCmd());
		addCommand(new NukerCmd());
		addCommand(new PathCmd());
		addCommand(new PotionCmd());
		addCommand(new ProtectCmd());
		addCommand(new RenameCmd());
		addCommand(new RvCmd());
		addCommand(new SayCmd());
		addCommand(new SearchCmd());
		addCommand(new SpammerCmd());
		addCommand(new TacoCmd());
		addCommand(new TCmd());
		addCommand(new ThrowCmd());
		addCommand(new TpCmd());
		addCommand(new VClipCmd());
		addCommand(new WmsCmd());
		addCommand(new XRayCmd());
	}
	
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
			Cmd cmd = getCommandByName(commandName);
			if(cmd != null)
				try
				{
					cmd.execute(args);
					if(!event.isAutomatic())
						WurstClient.INSTANCE.analytics.trackEvent("command",
							commandName);
				}catch(SyntaxError e)
				{
					if(e.getMessage() != null)
						WurstClient.INSTANCE.chat
							.message("§4Syntax error:§r " + e.getMessage());
					else
						WurstClient.INSTANCE.chat
							.message("§4Syntax error!§r");
					cmd.printSyntax();
				}catch(Cmd.Error e)
				{
					WurstClient.INSTANCE.chat.error(e.getMessage());
				}catch(Exception e)
				{
					WurstClient.INSTANCE.eventManager.handleException(e, cmd,
						"executing", "Exact input: `" + event.getMessage()
							+ "`");
				}
			else
				WurstClient.INSTANCE.chat.error("\"." + commandName
					+ "\" is not a valid command.");
		}
	}
	
	public Cmd getCommandByClass(Class<?> commandClass)
	{
		return cmds.get(commandClass.getAnnotation(Info.class).name());
	}
	
	public Cmd getCommandByName(String name)
	{
		return cmds.get(name);
	}
	
	public Collection<Cmd> getAllCommands()
	{
		return cmds.values();
	}
	
	public int countCommands()
	{
		return cmds.size();
	}
	
	private void addCommand(Cmd commmand)
	{
		cmds.put(commmand.getName(), commmand);
	}
}
