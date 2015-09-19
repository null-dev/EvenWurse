/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Shows the version of the server you are currently playing on.",
	name = "sv",
	syntax = {})
public class SvCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length != 0)
			syntaxError();
		if(Minecraft.getMinecraft().isSingleplayer())
			error("Can't check server version in singleplayer.");
		WurstClient.INSTANCE.chat.message("Server version: "
			+ WurstClient.INSTANCE.lastServer.getServerData().gameVersion);
	}
}
