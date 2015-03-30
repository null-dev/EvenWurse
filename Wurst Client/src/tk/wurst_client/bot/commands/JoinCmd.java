/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.bot.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

@Command.Info(help = "Joins a server.", name = "join", syntax = {"<ip>"})
public class JoinCmd extends Command
{
	@Override
	public void execute(final String[] args) throws Error
	{
		if(args.length != 1)
			syntaxError();
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				Minecraft.getMinecraft().displayGuiScreen(
					new GuiConnecting(new GuiMainMenu(), Minecraft
						.getMinecraft(), new ServerData("", args[0])));
				System.out.println("Joined " + args[0] + " as "
					+ Minecraft.getMinecraft().session.getUsername());
			}
		});
	}
}
