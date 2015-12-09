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

@Info(category = Category.COMBAT,
	description = "Regenerates your health 100 times faster.\n"
		+ "Can cause unwanted \"Flying is not enabled!\" kicks.",
	name = "Regen",
	noCheatCompatible = false)
public class RegenMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.events.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
			&& Minecraft.getMinecraft().thePlayer.getFoodStats().getFoodLevel() > 17
			&& Minecraft.getMinecraft().thePlayer.getHealth() < 20
			&& Minecraft.getMinecraft().thePlayer.getHealth() != 0
			&& Minecraft.getMinecraft().thePlayer.onGround)
			for(int i = 0; i < 1000; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer());
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
	}
}
