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
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Invisibility extends Module
{
	
	public Invisibility()
	{
		super(
			"Invisibility",
			"Makes you invisible and invincible.\n"
				+ "If you die and respawn near a certain player while\n"
				+ "this mod is enabled, that player will be unable to see\n"
				+ "you. Only works on vanilla servers!",
			Category.COMBAT);
	}
	
	private boolean isInvisible;
	
	@Override
	public void oldOnUpdate()
	{
		if(getToggled()
			&& Client.wurst.moduleManager.getModuleFromClass(YesCheat.class)
				.getToggled())
		{
			noCheatMessage();
			setToggled(false);
			return;
		}
		if(Minecraft.getMinecraft().thePlayer.getHealth() <= 0)
			if(getToggled())
			{
				Minecraft.getMinecraft().thePlayer.respawnPlayer();// This line
				// makes you
				// completely
				// invisible
				// to other
				// people!!!
				isInvisible = true;
			}else
				isInvisible = false;
		if(isInvisible)
		{
			Minecraft.getMinecraft().thePlayer.setInvisible(true);// This is
			// just so
			// you know
			// you are
			// invisible.
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
