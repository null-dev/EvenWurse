/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.events.ChatInputEvent;
import tk.wurst_client.event.listeners.ChatInputListener;

public class Annoy extends Command implements ChatInputListener
{
	private boolean toggled;
	private String name;
	
	public Annoy()
	{
		super("annoy",
			"Annoys a player by repeating everything he says.",
			"§o.annoy§r [<player>]");
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		toggled = !toggled;
		if(toggled)
		{
			if(args != null && args.length == 1)
			{
				name = args[0];
				Client.wurst.chat.message("Now annoying " + name + ".");
				if(name.equals(Minecraft.getMinecraft().thePlayer.getName()))
					Client.wurst.chat.warning("Annoying yourself is a bad idea!");
				EventManager.addChatInputListener(this);
			}else
			{
				toggled = false;
				commandError();
			}
		}else
		{
			EventManager.removeChatInputListener(this);
			if(name != null)
			{
				Client.wurst.chat.message("No longer annoying " + name + ".");
				name = null;
			}
		}
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event)
	{
		String message = new String(event.getMessage());
		if(message.startsWith("§c[§6Wurst§c]§f "))
			return;
		if(message.startsWith("<" + name + ">") || message.contains(name + ">"))
		{
			String repeatMessage = message.substring(message.indexOf(">") + 1);
			Minecraft.getMinecraft().thePlayer.sendChatMessage(repeatMessage);
		}else if(message.contains("] " + name + ":")
			|| message.contains("]" + name + ":"))
		{
			String repeatMessage = message.substring(message.indexOf(":") + 1);
			Minecraft.getMinecraft().thePlayer.sendChatMessage(repeatMessage);
		}
	}
}
