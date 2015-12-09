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
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Protects you from fall damage.\n" + "Bypasses AntiCheat.",
	name = "NoFall")
public class NoFallMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.events.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().thePlayer.fallDistance > 2)
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C03PacketPlayer(true));
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.removeUpdateListener(this);
	}
}
