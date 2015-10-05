/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.WurstClient;

public class GuiTeamSettings extends GuiScreen
{
	private GuiScreen prevMenu;
	
	public GuiTeamSettings(GuiScreen prevMenu)
	{
		this.prevMenu = prevMenu;
		WurstClient.INSTANCE.analytics.trackPageView("/team-settings",
			"Team Settings");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		String[] colors =
			{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c",
				"d", "e", "f",};
		for(int i = 0; i < 16; i++)
		{
			int offsetX = -22;
			switch(i % 4)
			{
				case 3:
					offsetX = 26;
					break;
				case 2:
					offsetX = 2;
					break;
				case 0:
					offsetX = -46;
					break;
			}
			int offsetY = 72;
			switch(i % 16 / 4)
			{
				case 2:
					offsetY = 48;
					break;
				case 1:
					offsetY = 24;
					break;
				case 0:
					offsetY = 0;
					break;
			}
			buttonList.add(new GuiButton(i, width / 2 + offsetX, height / 3
				+ offsetY, 20, 20, "§" + colors[i] + colors[i]));
		}
		buttonList.add(new GuiButton(16, width / 2 - 46, height / 3 + 96, 44,
			20, "All On"));
		buttonList.add(new GuiButton(17, width / 2 + 2, height / 3 + 96, 44,
			20, "All Off"));
		buttonList.add(new GuiButton(18, width / 2 - 100, height / 3 + 120,
			200, 20, "Done"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(!button.enabled)
			return;
		switch(button.id)
		{
			case 18:
				Minecraft.getMinecraft().displayGuiScreen(prevMenu);
				WurstClient.INSTANCE.analytics.trackEvent("team settings",
					"done");
				break;
			default:
				break;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Team Settings", width / 2, 20,
			16777215);
		drawCenteredString(fontRendererObj,
			"Target all entities with the following", width / 2,
			height / 3 - 30, 10526880);
		drawCenteredString(fontRendererObj, "color(s) in their name:",
			width / 2, height / 3 - 20, 10526880);
		
		for(int i = 0; i < buttonList.size(); i++)
		{
			GuiButton button = ((GuiButton)buttonList.get(i));
			button.drawButton(mc, mouseX, mouseY);
		}
	}
}
