/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.block.Block;
import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.mods.SearchMod;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Changes the settings of Search or toggles it.",
	name = "search",
	syntax = {"id <block_id>", "name <block_name>"})
public class SearchCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length == 0)
		{
			WurstClient.INSTANCE.modManager.getModByClass(SearchMod.class)
				.toggle();
			WurstClient.INSTANCE.chat.message("Search turned "
				+ (WurstClient.INSTANCE.modManager.getModByClass(
					SearchMod.class).isEnabled() == true ? "on" : "off") + ".");
		}else if(args.length == 2)
		{
			SearchMod search =
				(SearchMod)WurstClient.INSTANCE.modManager
					.getModByClass(SearchMod.class);
			if(args[0].toLowerCase().equals("id"))
			{
				if(MiscUtils.isInteger(args[1]))
					WurstClient.INSTANCE.options.searchID =
						Integer.valueOf(args[1]);
				else
					syntaxError("ID must be a number.");
				WurstClient.INSTANCE.fileManager.saveOptions();
				search.notify = true;
				WurstClient.INSTANCE.chat.message("Search ID set to " + args[1]
					+ ".");
			}else if(args[0].equalsIgnoreCase("name"))
			{
				int newID =
					Block.getIdFromBlock(Block.getBlockFromName(args[1]));
				if(newID == -1)
					error("Block \"" + args[1] + "\" could not be found.");
				WurstClient.INSTANCE.options.searchID = Integer.valueOf(newID);
				WurstClient.INSTANCE.fileManager.saveOptions();
				search.notify = true;
				WurstClient.INSTANCE.chat.message("Search ID set to " + newID
					+ " (" + args[1] + ").");
			}
		}else
			syntaxError();
	}
}
