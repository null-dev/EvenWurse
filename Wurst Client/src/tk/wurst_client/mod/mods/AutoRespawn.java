/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mod.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.DeathListener;
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.Mod.Category;
import tk.wurst_client.mod.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Automatically respawns you whenever you die.",
	name = "AutoRespawn")
public class AutoRespawn extends Mod implements DeathListener
{
	@Override
	public void onEnable()
	{
		EventManager.death.addListener(this);
	}
	
	@Override
	public void onDeath()
	{
		Minecraft.getMinecraft().thePlayer.respawnPlayer();
		GuiScreen.mc.displayGuiScreen((GuiScreen)null);
	}
	
	@Override
	public void onDisable()
	{
		EventManager.death.removeListener(this);
	}
}
