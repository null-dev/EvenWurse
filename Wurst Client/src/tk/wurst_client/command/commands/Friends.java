/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import java.util.Iterator;

import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.command.Command.Info;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Manages your friends list.", name = "friends", syntax = {
	"(add | remove) <player>", "list [<page>]"})
public class Friends extends Command
{
	@Override
	public void execute(String[] args)
	{
		if(args.length == 0)
		{
			syntaxError();
			return;
		}
		if(args[0].equalsIgnoreCase("list"))
		{
			if(args.length == 1)
			{
				execute(new String[]{"list", "1"});
				return;
			}
			int pages = (int)Math.ceil(Client.wurst.friends.size() / 8D);
			if(MiscUtils.isInteger(args[1]))
			{
				int page = Integer.valueOf(args[1]);
				if(page > pages || page < 1)
				{
					syntaxError();
					return;
				}
				Client.wurst.chat.message("Current friends: "
					+ Client.wurst.friends.size());
				Client.wurst.chat.message("Friends list (page " + page + "/"
					+ pages + "):");
				Iterator<String> itr = Client.wurst.friends.iterator();
				for(int i = 0; itr.hasNext(); i++)
				{
					String friend = itr.next();
					if(i >= (page - 1) * 8 && i < (page - 1) * 8 + 8)
						Client.wurst.chat.message(friend);
				}
			}else
				syntaxError();
		}else if(args[0].equalsIgnoreCase("add"))
		{
			if(Client.wurst.friends.contains(args[1]))
			{
				Client.wurst.chat.error("\"" + args[1]
					+ "\" is already in your friends list.");
				return;
			}
			Client.wurst.friends.add(args[1]);
			Client.wurst.fileManager.saveFriends();
			Client.wurst.chat.message("Added friend \"" + args[1] + "\".");
		}else if(args[0].equalsIgnoreCase("remove"))
		{
			if(Client.wurst.friends.remove(args[1]))
			{
				Client.wurst.fileManager.saveFriends();
				Client.wurst.chat.message("Removed friend \"" + args[1]
					+ "\".");
			}else
				Client.wurst.chat.error("\"" + args[1]
					+ "\" is not in your friends list.");
		}else
			syntaxError();
	}
}
