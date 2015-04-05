/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import tk.wurst_client.Client;
import tk.wurst_client.events.EventManager;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Blocks bad potion effects.",
	name = "AntiPotion")
public class AntiPotionMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getModByClass(YesCheatMod.class).isEnabled())
		{
			noCheatMessage();
			setEnabled(false);
			return;
		}
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
			&& Minecraft.getMinecraft().thePlayer.onGround
			&& !Minecraft.getMinecraft().thePlayer.getActivePotionEffects()
				.isEmpty())
			if(Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.hunger)
				|| Minecraft.getMinecraft().thePlayer
					.isPotionActive(Potion.poison))
				for(int i = 0; i < 1000; i++)
					Minecraft.getMinecraft().thePlayer.sendQueue
						.addToSendQueue(new C03PacketPlayer());
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
}
