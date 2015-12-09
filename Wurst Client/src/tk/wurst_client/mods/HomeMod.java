/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.ChatInputEvent;
import tk.wurst_client.events.listeners.ChatInputListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.CHAT,
	description = "Types \"/home\" instantly.",
	name = "/home")
public class HomeMod extends Mod implements UpdateListener, ChatInputListener
{
	private int disableTimer;
	
	@Override
	public void onEnable()
	{
		disableTimer = 0;
		WurstClient.INSTANCE.events.addChatInputListener(this);
		WurstClient.INSTANCE.events.addUpdateListener(this);
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
		WurstClient.INSTANCE.events.remove(ChatInputListener.class, this);
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
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
			WurstClient.INSTANCE.chat.error("This server doesn't have /home.");
		}
	}
}
