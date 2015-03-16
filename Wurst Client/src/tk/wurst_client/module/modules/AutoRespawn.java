/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.module.Mod;

public class AutoRespawn extends Mod
{
	public AutoRespawn()
	{
		super(
			"AutoRespawn",
			"Automatically respawns you whenever you die.",
			Category.COMBAT);
	}
	
	@Override
	public void onDeath()
	{
		if(getToggled())
		{
			Minecraft.getMinecraft().thePlayer.respawnPlayer();
			GuiScreen.mc.displayGuiScreen((GuiScreen)null);
		}
	}
}
