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
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;
import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.events.listeners.RenderListener;

@Info(help = "Allows you to see parts of another player's inventory.",
	name = "invsee",
	syntax = {"<player>"})
public class InvseeCmd extends Cmd implements RenderListener
{
	private String playerName;
	
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length != 1)
			syntaxError();
		if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
		{
			WurstClient.INSTANCE.chat.error("Survival mode only.");
			return;
		}
		playerName = args[0];
		WurstClient.INSTANCE.events.add(RenderListener.class, this);
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
					WurstClient.INSTANCE.chat.message("Showing inventory of "
						+ player.getName() + ".");
					Minecraft.getMinecraft().displayGuiScreen(
						new GuiInventory(player));
					found = true;
				}
			}
		if(!found)
			WurstClient.INSTANCE.chat.error("Player not found.");
		playerName = null;
		WurstClient.INSTANCE.events.remove(RenderListener.class, this);
	}
}
