/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Mod;
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Makes you invisible and invincible.\n"
		+ "If you die and respawn near a certain player while\n"
		+ "this mod is enabled, that player will be unable to see\n"
		+ "you. Only works on vanilla servers!",
	name = "Invisibility")
public class Invisibility extends Mod implements UpdateListener
{
	private boolean isInvisible;
	
	public Invisibility()
	{
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(getToggled()
			&& Client.wurst.modManager.getMod(YesCheat.class)
				.getToggled())
		{
			noCheatMessage();
			setToggled(false);
			return;
		}
		if(Minecraft.getMinecraft().thePlayer.getHealth() <= 0)
			if(getToggled())
			{
				// Respawning too early for server-side invisibility
				Minecraft.getMinecraft().thePlayer.respawnPlayer();
				isInvisible = true;
			}else
				isInvisible = false;
		if(isInvisible)
		{
			// Potion effect for client-side invisibility
			Minecraft.getMinecraft().thePlayer.setInvisible(true);
			Minecraft.getMinecraft().thePlayer
				.addPotionEffect(new PotionEffect(Potion.invisibility.getId(),
					10801220));
		}else
		{
			Minecraft.getMinecraft().thePlayer.setInvisible(false);
			Minecraft.getMinecraft().thePlayer
				.removePotionEffect(Potion.invisibility.getId());
		}
	}
}
