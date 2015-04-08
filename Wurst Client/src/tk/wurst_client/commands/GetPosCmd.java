/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import tk.wurst_client.Client;

@Cmd.Info(help = "Shows your current position or copies it to the clipboard.",
	name = "getpos",
	syntax = {"[copy]"})
public class GetPosCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length > 1)
			syntaxError();
		BlockPos blockpos = new BlockPos(Minecraft.getMinecraft().thePlayer);
		String pos = blockpos.getX() + " " + blockpos.getY() + " " + blockpos.getZ();
		if(args.length == 0)
			Client.wurst.chat.message("Position: " + pos);
		else if(args.length == 1 && args[0].equalsIgnoreCase("copy"))
		{
			Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(pos), null);
			Client.wurst.chat.message("Position copied to clipboard.");
		}
	}
}
