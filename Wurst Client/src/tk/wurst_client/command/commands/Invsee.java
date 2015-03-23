/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;
import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.RenderListener;

public class Invsee extends Command implements RenderListener
{
	private String playerName;
	
	public Invsee()
	{
		super("invsee",
			"Allows you to see parts of another player's inventory.",
			"§o.invsee§r <player>");
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		if(args.length != 1)
		{
			commandError();
			return;
		}
		playerName = args[0];
		EventManager.render.addListener(this);
	}
	
	@Override
	public void onRender()
	{
		boolean found = false;
		for(Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(entity instanceof EntityOtherPlayerMP)
			{
				EntityOtherPlayerMP player = (EntityOtherPlayerMP)entity;
				if(player.getName().equals(playerName))
				{
					Client.wurst.chat.message("Showing inventory of "
						+ player.getName() + ".");
					Minecraft.getMinecraft().displayGuiScreen(
						new GuiInventory(player));
					found = true;
				}
			}
		if(!found)
			Client.wurst.chat.error("Player not found.");
		playerName = null;
		EventManager.render.removeListener(this);
	}
}
