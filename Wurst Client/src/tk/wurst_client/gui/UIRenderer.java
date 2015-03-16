/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui;

import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;

import tk.wurst_client.Client;
import tk.wurst_client.font.Fonts;
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.mods.ClickGUI;

public class UIRenderer
{
	private static void renderModList()
	{
		if(Client.wurst.options.modListMode == 2)
			return;
		LinkedList<String> modList = new LinkedList<String>();
		for(Mod mod : Client.wurst.modManager.getAllMods())
		{
			if(mod instanceof ClickGUI)
				continue;
			if(mod.isEnabled())
				modList.add(mod.getRenderName());
		}
		ScaledResolution sr =
			new ScaledResolution(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight);
		int yCount = 19;
		if(yCount + modList.size() * 9 > sr.getScaledHeight()
			|| Client.wurst.options.modListMode == 1)
		{
			String tooManyMods = "";
			if(modList.isEmpty())
				return;
			else if(modList.size() > 1)
				tooManyMods = modList.size() + " mods active";
			else
				tooManyMods = "1 mod active";
			Fonts.segoe18.drawString(tooManyMods, 3, yCount + 1, 0xFF000000);
			Fonts.segoe18.drawString(tooManyMods, 2, yCount, 0xFFFFFFFF);
		}else
			for(String name; (name = modList.poll()) != null;)
			{
				Fonts.segoe18.drawString(name, 3, yCount + 1, 0xFF000000);
				Fonts.segoe18.drawString(name, 2, yCount, 0xFFFFFFFF);
				yCount += 9;
			}
	}
	
	public static void renderUI()
	{
		Fonts.segoe22.drawString("v" + Client.wurst.CLIENT_VERSION, 74, 4,
			0xFF000000);
		renderModList();
	}
	
	public static void renderPinnedFrames()
	{
		for(Frame moduleFrame : Client.wurst.guiManager.getFrames())
			if(moduleFrame.isPinned()
				&& !(Minecraft.getMinecraft().currentScreen instanceof GuiManagerDisplayScreen))
				moduleFrame.render();
	}
}
