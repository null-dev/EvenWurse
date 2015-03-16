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
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.Mod.Category;
import tk.wurst_client.mod.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Automatically respawns you whenever you die.",
	name = "AutoRespawn")
public class AutoRespawn extends Mod
{
	@Override
	public void onDeath()
	{
		if(isEnabled())
		{
			Minecraft.getMinecraft().thePlayer.respawnPlayer();
			GuiScreen.mc.displayGuiScreen((GuiScreen)null);
		}
	}
}
