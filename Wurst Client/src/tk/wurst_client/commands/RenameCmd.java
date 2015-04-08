/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import tk.wurst_client.Client;
import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Renames the item in your hand.",
	name = "rename",
	syntax = {"<item>"})
public class RenameCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		if(args.length > 0)
		{
			String name = args[0];
			ItemStack currentItem =
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
			if(currentItem == null)
				error("There is no item in your hand.");
			else
				currentItem.setStackDisplayName(name);
			Client.wurst.chat.message("Renamed your current item.");
		}
		else
			syntaxError("Name required.");
	}
}
