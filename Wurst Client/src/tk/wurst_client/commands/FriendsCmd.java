/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.util.Iterator;

import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Manages your friends list.", name = "friends", syntax = {
	"(add | remove) <player>", "list [<page>]"})
public class FriendsCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length == 0)
			syntaxError();
		if(args[0].equalsIgnoreCase("list"))
		{
			if(args.length == 1)
			{
				execute(new String[]{"list", "1"});
				return;
			}
			int pages =
				(int)Math.ceil(WurstClient.INSTANCE.friends.size() / 8D);
			if(MiscUtils.isInteger(args[1]))
			{
				int page = Integer.valueOf(args[1]);
				if(page > pages || page < 1)
					syntaxError();
				WurstClient.INSTANCE.chat.message("Current friends: "
					+ WurstClient.INSTANCE.friends.size());
				WurstClient.INSTANCE.chat.message("Friends list (page " + page
					+ "/" + pages + "):");
				Iterator<String> itr = WurstClient.INSTANCE.friends.iterator();
				for(int i = 0; itr.hasNext(); i++)
				{
					String friend = itr.next();
					if(i >= (page - 1) * 8 && i < (page - 1) * 8 + 8)
						WurstClient.INSTANCE.chat.message(friend);
				}
			}else
				syntaxError();
		}else if(args.length < 2)
			syntaxError();
		else if(args[0].equalsIgnoreCase("add"))
		{
			if(WurstClient.INSTANCE.friends.contains(args[1]))
			{
				WurstClient.INSTANCE.chat.error("\"" + args[1]
					+ "\" is already in your friends list.");
				return;
			}
			WurstClient.INSTANCE.friends.add(args[1]);
			WurstClient.INSTANCE.fileManager.saveFriends();
			WurstClient.INSTANCE.chat.message("Added friend \"" + args[1]
				+ "\".");
		}else if(args[0].equalsIgnoreCase("remove"))
		{
			if(WurstClient.INSTANCE.friends.remove(args[1]))
			{
				WurstClient.INSTANCE.fileManager.saveFriends();
				WurstClient.INSTANCE.chat.message("Removed friend \"" + args[1]
					+ "\".");
			}else
				WurstClient.INSTANCE.chat.error("\"" + args[1]
					+ "\" is not in your friends list.");
		}else
			syntaxError();
	}
}
