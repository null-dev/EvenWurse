/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.block.Block;
import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.mods.NukerMod;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Changes the settings of Nuker.", name = "nuker", syntax = {
	"mode (normal|id|flat|smash)", "id <block_id>", "name <block_name>"})
public class NukerCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length != 2)
			syntaxError();
		else if(args[0].toLowerCase().equals("mode"))
		{// 0=normal, 1=id, 2=flat, 3=smash
			if(args[1].toLowerCase().equals("normal"))
			{
				WurstClient.INSTANCE.options.nukerMode = 0;
				NukerMod.id = 0;
			}else if(args[1].toLowerCase().equals("id"))
			{
				WurstClient.INSTANCE.options.nukerMode = 1;
				NukerMod.id = 0;
			}else if(args[1].toLowerCase().equals("flat"))
			{
				WurstClient.INSTANCE.options.nukerMode = 2;
				NukerMod.id = 0;
			}else if(args[1].toLowerCase().equals("smash"))
			{
				WurstClient.INSTANCE.options.nukerMode = 3;
				NukerMod.id = 0;
			}else
				syntaxError();
			WurstClient.INSTANCE.fileManager.saveOptions();
			WurstClient.INSTANCE.chat.message("Nuker mode set to \"" + args[1] + "\".");
		}else if(args[0].equalsIgnoreCase("id") && MiscUtils.isInteger(args[1]))
		{
			if(WurstClient.INSTANCE.options.nukerMode != 1)
			{
				WurstClient.INSTANCE.options.nukerMode = 1;
				WurstClient.INSTANCE.chat.message("Nuker mode set to \"" + args[0]
					+ "\".");
			}
			NukerMod.id = Integer.valueOf(args[1]);
			WurstClient.INSTANCE.fileManager.saveOptions();
			WurstClient.INSTANCE.chat.message("Nuker ID set to " + args[1] + ".");
		}else if(args[0].equalsIgnoreCase("name"))
		{
			if(WurstClient.INSTANCE.options.nukerMode != 1)
			{
				WurstClient.INSTANCE.options.nukerMode = 1;
				WurstClient.INSTANCE.chat.message("Nuker mode set to \"" + args[0]
					+ "\".");
			}
			int newID = Block.getIdFromBlock(Block.getBlockFromName(args[1]));
			if(newID == -1)
			{
				WurstClient.INSTANCE.chat.message("The block \"" + args[1]
					+ "\" could not be found.");
				return;
			}
			NukerMod.id = Integer.valueOf(newID);
			WurstClient.INSTANCE.fileManager.saveOptions();
			WurstClient.INSTANCE.chat.message("Nuker ID set to " + newID + " ("
				+ args[1] + ").");
		}else
			syntaxError();
	}
}
