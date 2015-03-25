/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.client.Minecraft;
import tk.wurst_client.command.Command;
import tk.wurst_client.command.Command.Info;

@Info(help = "Types \"/gamemode <args>\".\nUseful for servers that don't support /gm.",
	name = "gm",
	syntax = {"<gamemode>"})
public class GM extends Command
{
	@Override
	public void execute(String[] args)
	{
		if(args.length != 1)
		{
			syntaxError();
			return;
		}
		Minecraft.getMinecraft().thePlayer.sendChatMessage("/gamemode "
			+ args[0]);
	}
}
