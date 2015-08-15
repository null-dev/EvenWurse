/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.util.Iterator;
import java.util.Map.Entry;

import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Lists all keybinds.", name = "binds", syntax = {"[<page>]"})
public class BindsCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length == 0)
		{
			execute(new String[]{"1"});
			return;
		}
		int pages = (int)Math.ceil(WurstClient.INSTANCE.keybinds.size() / 8D);
		if(MiscUtils.isInteger(args[0]))
		{
			int page = Integer.valueOf(args[0]);
			if(page > pages || page == 0)
			{
				syntaxError("Invalid page: " + page);
				return;
			}
			WurstClient.INSTANCE.chat.message("Current keybinds: "
				+ Integer.toString(WurstClient.INSTANCE.keybinds.size()));
			WurstClient.INSTANCE.chat.message("Keybind list (page " + page
				+ "/" + pages + "):");
			Iterator<Entry<String, String>> itr =
				WurstClient.INSTANCE.keybinds.entrySet().iterator();
			for(int i = 0; itr.hasNext(); i++)
			{
				Entry<String, String> entry = itr.next();
				if(i >= (page - 1) * 8 && i < (page - 1) * 8 + 8)
					WurstClient.INSTANCE.chat.message(entry.getKey() + ": "
						+ entry.getValue());
			}
		}else
			syntaxError("Not a number: \"" + args[0] + "\"");
	}
}
