/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Home extends Module
{
	public Home()
	{
		super(
			"/home",
			"Types \"/home\" instantly.",
			Keyboard.KEY_H,
			Category.CHAT);
	}

	private int disableTimer;

	@Override
	public void onUpdate()
	{
		if(getToggled())
		{
			if(disableTimer == 4)
				setToggled(false);
			else if(disableTimer == 0)
				Minecraft.getMinecraft().thePlayer.sendChatMessage("/home");
			disableTimer++;
		}
	}

	@Override
	public void onEnable()
	{
		disableTimer = 0;
	}

	@Override
	public void onReceivedMessage(String message)
	{
		if(!getToggled() || message.startsWith("§c[§6" + Client.wurst.CLIENT_NAME + "§c]§f "))
			return;
		if(message.toLowerCase().contains("/help") || message.toLowerCase().contains("permission"))
			Client.wurst.chat.error("This server doesn't have /home.");
	}
}
