/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import tk.wurst_client.events.EventManager;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Auto disconnects you if your health\n"
		+ "gets below 4 hearts.\n" + "It can bypass combat logger.",
	name = "AutoLeave")
public class AutoLeaveMod extends Mod implements UpdateListener
{
	
	@Override
	public void onEnable()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		
		float health = Minecraft.getMinecraft().thePlayer.getHealth();
		
		if(health <= 8.0F
			&& !Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
			&& (!Minecraft.getMinecraft().isIntegratedServerRunning() || Minecraft
				.getMinecraft().thePlayer.sendQueue.getPlayerInfo().size() > 1))
		{
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C01PacketChatMessage("§"));
			setEnabled(false);
		}
		
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
}
