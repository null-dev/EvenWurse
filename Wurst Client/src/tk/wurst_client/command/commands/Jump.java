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

@Command.Info(help = "Makes you jump once.", name = "jump", syntax = {})
public class Jump extends Command
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length != 0)
			syntaxError();
		if(!Minecraft.getMinecraft().thePlayer.onGround)
			error("Can't jump in mid-air.");
		Minecraft.getMinecraft().thePlayer.jump();
	}
}
