/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.EXPLOITS,
	description = "Makes you invisible and invincible.\n"
		+ "If you die and respawn near a certain player while\n"
		+ "this mod is enabled, that player will be unable to see\n"
		+ "you. Only works on vanilla servers!",
	name = "Invisibility",
	noCheatCompatible = false)
public class InvisibilityMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().thePlayer.getHealth() <= 0)
			if(isEnabled())
			{
				// Respawning too early for server-side invisibility
				Minecraft.getMinecraft().thePlayer.respawnPlayer();
				WurstClient.INSTANCE.chat
					.message("You should now be invisible.");
			}else
			{
				WurstClient.INSTANCE.chat
					.message("You are no longer invisible.");
				WurstClient.INSTANCE.eventManager.remove(UpdateListener.class,
					this);
			}
	}
}
