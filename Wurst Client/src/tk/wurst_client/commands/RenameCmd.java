/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Renames the item in your hand. Use $ for colors, use $$ for $.",
	name = "rename",
	syntax = {"<new_name>"})
public class RenameCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		if(args.length == 0)
			syntaxError();
		String message = args[0];
		for(int i = 1; i < args.length; i++)
			message += " " + args[i];
		message = message.replace("$", "§").replace("§§", "$");
		ItemStack item =
			Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
		if(item == null)
			error("There is no item in your hand.");
		item.setStackDisplayName(message);
		WurstClient.INSTANCE.chat.message("Renamed item to \"" + message
			+ "§r\".");
	}
}
