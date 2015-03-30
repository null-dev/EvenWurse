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
import net.minecraft.client.multiplayer.WorldClient;

@Command.Info(help = "Leaves the current server.", name = "leave", syntax = {})
public class LeaveCmd extends Command
{
	@Override
	public void execute(final String[] args) throws Error
	{
		if(args.length != 0)
			syntaxError();
		if(Minecraft.getMinecraft().theWorld == null)
			error("No server to leave.");
		Minecraft.getMinecraft().addScheduledTask(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Minecraft.getMinecraft().theWorld
						.sendQuittingDisconnectingPacket();
					Minecraft.getMinecraft().loadWorld((WorldClient)null);
					Minecraft.getMinecraft()
						.displayGuiScreen(new GuiMainMenu());
					System.out
						.println("Left current server.");
				}catch(Exception e)
				{
					System.err
						.println("Could not leave server.");
					e.printStackTrace();
				}
			}
		});
	}
}
