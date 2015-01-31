/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.utils.MiscUtils;

public class XRay extends Command
{
	private static String[] commandHelp =
	{
		"Adds, removes or lists X-Ray blocks or toggles X-Ray.",
		".xray <add|remove> id <block id>",
		".xray <add|remove> name <block name>",
		".xray list",
		".xray list <page>"
	};

	public XRay()
	{
		super("xray", commandHelp);
	}

	private int blocksPerPage = 8;

	@Override
	public void onEnable(String input, String[] args)
	{
		if(args == null)
			commandError();
		else if(args[0].equalsIgnoreCase("list"))
		{
			int totalBlocks = tk.wurst_client.module.modules.XRay.xrayBlocks.size();
			float pagesF = (float)((double)totalBlocks / (double)blocksPerPage);
			int pages = (int)(Math.round(pagesF) == pagesF ? pagesF : pagesF + 1);
			blocksPerPage = 8;
			if(args.length == 1)
			{
				if(pages <= 1)
				{
					blocksPerPage = totalBlocks;
					Client.Wurst.chat.message("Current X-Ray blocks: " + totalBlocks);
					for(int i = 0; i < tk.wurst_client.module.modules.XRay.xrayBlocks.size() && i < blocksPerPage; i++)
						Client.Wurst.chat.message(Integer.toString(Block.getIdFromBlock(tk.wurst_client.module.modules.XRay.xrayBlocks.get(i))));
				}else
				{
					Client.Wurst.chat.message("Current X-Ray blocks: " + totalBlocks);
					Client.Wurst.chat.message("X-Ray blocks list (page 1/" + pages + "):");
					for(int i = 0; i < tk.wurst_client.module.modules.XRay.xrayBlocks.size() && i < blocksPerPage; i++)
						Client.Wurst.chat.message(Integer.toString(Block.getIdFromBlock(tk.wurst_client.module.modules.XRay.xrayBlocks.get(i))));
				}
			}else
			{
				if(MiscUtils.isInteger(args[1]))
				{
					int page = Integer.valueOf(args[1]);
					if(page > pages || page == 0)
					{
						commandError();
						return;
					}
					Client.Wurst.chat.message("Current X-Ray blocks: " + Integer.toString(totalBlocks));
					Client.Wurst.chat.message("X-Ray blocks list (page " + page + "/" + pages + "):");
					int i2 = 0;
					for(int i = 0; i < tk.wurst_client.module.modules.XRay.xrayBlocks.size() && i2 < (page - 1) * blocksPerPage + blocksPerPage; i++)
					{
						if(i2 >= (page - 1) * blocksPerPage)
							Client.Wurst.chat.message(Integer.toString(Block.getIdFromBlock(tk.wurst_client.module.modules.XRay.xrayBlocks.get(i))));
						i2++;
					}
					return;
				}
				commandError();
			}
		}else if(args[0].equalsIgnoreCase("add"))
		{
			if(args[1].equalsIgnoreCase("id") && MiscUtils.isInteger(args[2]))
			{
				if(tk.wurst_client.module.modules.XRay.xrayBlocks.contains(Block.getBlockById(Integer.valueOf(args[2]))))
				{
					Client.Wurst.chat.error("\"" + args[2] + "\" is already in your X-Ray blocks list.");
					return;
				}
				tk.wurst_client.module.modules.XRay.xrayBlocks.add(Block.getBlockById(Integer.valueOf(args[2])));
				Client.Wurst.fileManager.saveXRayBlocks();
				Client.Wurst.chat.message("Added block " + args[2] + ".");
				Minecraft.getMinecraft().renderGlobal.loadRenderers();
			}else if(args[1].equalsIgnoreCase("name"))
			{
				int newID = Block.getIdFromBlock(Block.getBlockFromName(args[2]));
				if(newID == -1)
				{
					Client.Wurst.chat.message("The block \"" + args[1] + "\" could not be found.");
					return;
				}
				tk.wurst_client.module.modules.XRay.xrayBlocks.add(Block.getBlockById(newID));
				Client.Wurst.fileManager.saveXRayBlocks();
				Client.Wurst.chat.message("Added block " + newID + " (\"" + args[2] + "\").");
				Minecraft.getMinecraft().renderGlobal.loadRenderers();
			}else
				commandError();
		}else if(args[0].equalsIgnoreCase("remove"))
		{
			if(args[1].equalsIgnoreCase("id") && MiscUtils.isInteger(args[2]))
			{
				for(int i = 0; i < tk.wurst_client.module.modules.XRay.xrayBlocks.size(); i++)
					if(Integer.toString(Block.getIdFromBlock(tk.wurst_client.module.modules.XRay.xrayBlocks.get(i))).toLowerCase().equals(args[2].toLowerCase()))
					{
						tk.wurst_client.module.modules.XRay.xrayBlocks.remove(i);
						Client.Wurst.fileManager.saveXRayBlocks();
						Client.Wurst.chat.message("Removed block " + args[2] + ".");
						Minecraft.getMinecraft().renderGlobal.loadRenderers();
						return;
					}
				Client.Wurst.chat.error("Block " + args[2] + " is not in your X-Ray blocks list.");
			}else if(args[1].equalsIgnoreCase("name"))
			{
				int newID = Block.getIdFromBlock(Block.getBlockFromName(args[2]));
				if(newID == -1)
				{
					Client.Wurst.chat.message("The block \"" + args[2] + "\" could not be found.");
					return;
				}
				for(int i = 0; i < tk.wurst_client.module.modules.XRay.xrayBlocks.size(); i++)
					if(Block.getIdFromBlock(tk.wurst_client.module.modules.XRay.xrayBlocks.get(i)) == newID)
					{
						tk.wurst_client.module.modules.XRay.xrayBlocks.remove(i);
						Client.Wurst.fileManager.saveXRayBlocks();
						Client.Wurst.chat.message("Removed block " + newID + " (\"" + args[2] + "\").");
						Minecraft.getMinecraft().renderGlobal.loadRenderers();
						return;
					}
				Client.Wurst.chat.error("Block " + newID + " (\"" + args[2] + "\") is not in your X-Ray blocks list.");
			}else
				commandError();
		}else
			commandError();
	}
}
