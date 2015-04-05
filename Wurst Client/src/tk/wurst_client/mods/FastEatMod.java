/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.Client;
import tk.wurst_client.events.EventManager;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Allows you to eat food much faster.\n" + "OM! NOM! NOM!",
	name = "FastEat")
public class FastEatMod extends Mod implements UpdateListener
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
		if(Minecraft.getMinecraft().thePlayer.getHealth() > 0
			&& Minecraft.getMinecraft().thePlayer.onGround
			&& Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null
			&& Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem()
				.getItem() instanceof ItemFood
			&& Minecraft.getMinecraft().thePlayer.getFoodStats().needFood()
			&& Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed)
			for(int i = 0; i < 100; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer(false));
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
}
