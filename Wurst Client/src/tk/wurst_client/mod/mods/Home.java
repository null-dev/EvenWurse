/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mod.mods;

import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.events.ChatInputEvent;
import tk.wurst_client.event.listeners.ChatInputListener;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.Mod.Category;
import tk.wurst_client.mod.Mod.Info;

@Info(category = Category.CHAT,
	description = "Types \"/home\" instantly.",
	name = "/home")
public class Home extends Mod implements UpdateListener, ChatInputListener
{
	private int disableTimer;
	
	@Override
	public void onEnable()
	{
		disableTimer = 0;
		EventManager.addChatInputListener(this);
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(disableTimer == 4)
			setEnabled(false);
		else if(disableTimer == 0)
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/home");
		disableTimer++;
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeChatInputListener(this);
		EventManager.removeUpdateListener(this);
	}
	
	@Override
	public void onReceivedMessage(ChatInputEvent event)
	{
		String message = event.getComponent().getUnformattedText();
		if(message.startsWith("§c[§6Wurst§c]§f "))
			return;
		if(message.toLowerCase().contains("/help")
			|| message.toLowerCase().contains("permission"))
		{
			event.cancel();
			Client.wurst.chat.error("This server doesn't have /home.");
		}
	}
}
