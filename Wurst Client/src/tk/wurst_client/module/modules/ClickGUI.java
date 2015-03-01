/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;

import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;

import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class ClickGUI extends Module
{
	
	public ClickGUI()
	{
		super(
			"ClickGUI",
			"",
			Category.HIDDEN);
	}
	
	@Override
	public void onToggle()
	{
		if(!(Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen))
			Minecraft.getMinecraft().displayGuiScreen(new GuiManagerDisplayScreen(Client.wurst.guiManager));
	}
	
	@Override
	public void onUpdate()
	{
		Client.wurst.guiManager.update();
	}
}
