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

@Info(category = Category.MISC,
	description = "Uses an item multiple times.\n"
		+ "This can cause a lot of lag and even crash a server.\n"
		+ "Works best with snowballs or eggs.\n"
		+ "Use the .throw command to change the amount of uses per click.",
	name = "Throw")
public class ThrowMod extends Mod implements UpdateListener
{
	@Override
	public String getRenderName()
	{
		return getName() + " [" + WurstClient.INSTANCE.options.throwAmount + "]";
	}
	
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if((Minecraft.getMinecraft().rightClickDelayTimer == 4 || WurstClient.INSTANCE.modManager
			.getModByClass(FastPlaceMod.class).isEnabled())
			&& Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed)
		{
			if(Minecraft.getMinecraft().objectMouseOver == null
				|| Minecraft.getMinecraft().thePlayer.inventory
					.getCurrentItem() == null)
				return;
			for(int i = 0; i < WurstClient.INSTANCE.options.throwAmount - 1; i++)
				Minecraft.getMinecraft().rightClickMouse();
		}
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
	}
}
