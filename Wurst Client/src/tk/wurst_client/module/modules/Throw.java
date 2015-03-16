/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Mod;
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;

@Info(category = Category.MISC,
	description = "Uses an item multiple times.\n"
		+ "This can cause a lot of lag and even crash a server.\n"
		+ "Works best with snowballs or eggs.\n"
		+ "Use the .throw command to change the amount of uses per click.",
	name = "Throw")
public class Throw extends Mod implements UpdateListener
{
	@Override
	public String getRenderName()
	{
		return getName() + " [" + Client.wurst.options.throwAmount + "]";
	}
	
	@Override
	public void onEnable()
	{
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if((Minecraft.getMinecraft().rightClickDelayTimer == 4 || Client.wurst.modManager
			.getMod(FastPlace.class).getToggled())
			&& Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed)
		{
			if(Minecraft.getMinecraft().objectMouseOver == null
				|| Minecraft.getMinecraft().thePlayer.inventory
					.getCurrentItem() == null)
				return;
			for(int i = 0; i < Client.wurst.options.throwAmount - 1; i++)
				Minecraft.getMinecraft().rightClickMouse();
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
	}
}
