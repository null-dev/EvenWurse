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
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Throw extends Module
{
	public Throw()
	{
		super(
			"Throw",
			"Uses an item multiple times.\n"
				+ "This can cause a lot of lag and even crash a server.\n"
				+ "Works best with snowballs or eggs.\n"
				+ "Type .throw <amount> to change the amount of uses per click.",
				0,
				Category.MISC);
	}

	@Override
	public String getRenderName()
	{
		return getName() + " [" + Client.Wurst.options.throwAmount + "]";
	}

	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if((Minecraft.getMinecraft().rightClickDelayTimer == 4 || Client.Wurst.moduleManager.getModuleFromClass(FastPlace.class).getToggled()) && Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed)
		{
			if(Minecraft.getMinecraft().objectMouseOver == null || Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() == null)
				return;
			for(int i = 0; i < Client.Wurst.options.throwAmount - 1; i++)
				Minecraft.getMinecraft().rightClickMouse();
		}
	}
}
