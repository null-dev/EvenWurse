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
import tk.wurst_client.command.Command.Info;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Manages your friends list.", name = "friends", syntax = {
	"(add | remove) <player>", "list [<page>]"})
public class Friends extends Command
{
	private int friendsPerPage = 8;
	
	@Override
	public void execute(String[] args)
	{
		if(args.length == 0)
		{
			commandError();
			return;
		}
		if(args[0].equalsIgnoreCase("list"))
		{
			int totalFriends = Client.wurst.friends.size();
			float pagesF =
				(float)((double)totalFriends / (double)friendsPerPage);
			int pages =
				(int)(Math.round(pagesF) == pagesF ? pagesF : pagesF + 1);
			friendsPerPage = 8;
			if(args.length == 1)
			{
				if(pages <= 1)
				{
					friendsPerPage = totalFriends;
					Client.wurst.chat.message("Current friends: "
						+ totalFriends);
					for(int i = 0; i < Client.wurst.friends.size()
						&& i < friendsPerPage; i++)
						Client.wurst.chat.message(Client.wurst.friends.get(i));
				}else
				{
					Client.wurst.chat.message("Current friends: "
						+ totalFriends);
					Client.wurst.chat.message("Friends list (page 1/" + pages
						+ "):");
					for(int i = 0; i < Client.wurst.friends.size()
						&& i < friendsPerPage; i++)
						Client.wurst.chat.message(Client.wurst.friends.get(i));
				}
			}else
			{
				if(MiscUtils.isInteger(args[1]))
				{
					int page = Integer.valueOf(args[1]);
					if(page > pages || page == 0)
					{
						commandError();
						return;
					}
					Client.wurst.chat.message("Current friends: "
						+ Integer.toString(totalFriends));
					Client.wurst.chat.message("Friends list (page " + page
						+ "/" + pages + "):");
					int i2 = 0;
					for(int i = 0; i < Client.wurst.friends.size()
						&& i2 < (page - 1) * friendsPerPage + friendsPerPage; i++)
					{
						if(i2 >= (page - 1) * friendsPerPage)
							Client.wurst.chat.message(Client.wurst.friends
								.get(i));
						i2++;
					}
					return;
				}
				commandError();
			}
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
			for(int i = 0; i < Client.wurst.friends.size(); i++)
				if(Client.wurst.friends.get(i).toLowerCase()
					.equals(args[1].toLowerCase()))
				{
					Client.wurst.friends.remove(i);
					Client.wurst.fileManager.saveFriends();
					Client.wurst.chat.message("Removed friend \"" + args[1]
						+ "\".");
					return;
				}
			Client.wurst.chat.error("\"" + args[1]
				+ "\" is not in your friends list.");
		}else
			commandError();
	}
}
