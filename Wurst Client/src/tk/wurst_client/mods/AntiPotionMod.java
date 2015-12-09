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
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Blocks bad potion effects.",
	name = "AntiPotion",
	noCheatCompatible = false)
public class AntiPotionMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.events.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		if(!player.capabilities.isCreativeMode && player.onGround
			&& !player.getActivePotionEffects().isEmpty())
			if(player.isPotionActive(Potion.hunger)
				|| player.isPotionActive(Potion.moveSlowdown)
				|| player.isPotionActive(Potion.digSlowdown)
				|| player.isPotionActive(Potion.harm)
				|| player.isPotionActive(Potion.confusion)
				|| player.isPotionActive(Potion.blindness)
				|| player.isPotionActive(Potion.weakness)
				|| player.isPotionActive(Potion.wither)
				|| player.isPotionActive(Potion.poison))
				for(int i = 0; i < 1000; i++)
					player.sendQueue.addToSendQueue(new C03PacketPlayer());
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.removeUpdateListener(this);
	}
}
