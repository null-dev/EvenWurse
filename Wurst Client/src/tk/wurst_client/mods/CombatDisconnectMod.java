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
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Instantly disconnects you from the server.\n"
		+ "Can be keybinded for panic disconnects.\n"
		+ "It bypass combat logger. (only works on servers)",
	name = "CBTDisconnect")
public class CombatDisconnectMod extends Mod
{
	
	@Override
	public void onEnable()
	{
		if((!Minecraft.getMinecraft().isIntegratedServerRunning()
				|| Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfo().size() > 1))
		{
		Minecraft.getMinecraft().thePlayer.sendQueue
		.addToSendQueue(new C01PacketChatMessage("§"));
		setEnabled(false);
		}
		setEnabled(false);
	}
	
	@Override
	public void onDisable()
	{
	}
}
