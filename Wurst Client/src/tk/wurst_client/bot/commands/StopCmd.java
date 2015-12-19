/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.bot.commands;

import net.minecraft.client.Minecraft;

@Command.Info(help = "Stops Wurst-Bot.", name = "stop", syntax = {})
public class StopCmd extends Command
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length != 0)
			syntaxError();
		System.out.println("Stopping Wurst-Bot...");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Wurst-Bot stopped.");
        }));
		Minecraft.getMinecraft().shutdown();
	}
}
