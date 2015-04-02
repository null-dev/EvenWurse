/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.options;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.Client;
import tk.wurst_client.utils.MiscUtils;

import com.google.common.collect.Lists;

public class GuiWurstOptions extends GuiScreen
{
	private GuiScreen prevMenu;
	private String[] modListModes = {"Auto", "Count", "Hidden"};
	private String[] toolTips =
	{
		"",
		"Manage your friends by clicking them\n"
			+ "with the middle mouse button.",
		"How the mod list under the Wurst logo\n"
			+ "should be displayed.\n"
			+ "§nModes§r:\n"
			+ "§lAuto§r: Renders the whole list if it fits\n"
			+ "onto the screen.\n"
			+ "§lCount§r: Only renders the number of active\n"
			+ "mods.\n"
			+ "§lHidden§r: Renders nothing.",
		"Automatically maximizes the Minecraft window.\n"
			+ "Windows & Linux only!",
		"Whether or not the Wurst news should be\n"
			+ "shown in the main menu",
		"Sends anonymous usage statistics that help us\n"
			+ "improve the Wurst Client.",
		"Manager for the keybinds",
		"Manager for the blocks that X-Ray will\n"
			+ "show",
		"",
		"",
		"",
		"The official Website of the Wurst Client",
		"Frequently asked questions",
		"",
		"",
		""
	};
	private boolean autoMaximize;
	
	public GuiWurstOptions(GuiScreen par1GuiScreen)
	{
		prevMenu = par1GuiScreen;
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		autoMaximize = Client.wurst.fileManager.loadAutoMaximize();
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 144 - 16,
			200, 20, "Back"));
		buttonList.add(new GuiButton(1, width / 2 - 154, height / 4 + 24 - 16,
			100, 20, "Click Friends: "
				+ (Client.wurst.options.middleClickFriends ? "ON" : "OFF")));
		buttonList.add(new GuiButton(2, width / 2 - 154, height / 4 + 48 - 16,
			100, 20, "Mod List: "
				+ modListModes[Client.wurst.options.modListMode]));
		buttonList.add(new GuiButton(3, width / 2 - 154, height / 4 + 72 - 16,
			100, 20, "AutoMaximize: " + (autoMaximize ? "ON" : "OFF")));
		buttonList.add(new GuiButton(4, width / 2 - 154, height / 4 + 96 - 16,
			100, 20, "Wurst news: "
				+ (Client.wurst.options.wurstNews ? "ON" : "OFF")));
		buttonList
			.add(new GuiButton(5, this.width / 2 - 154,
				height / 4 + 120 - 16, 100, 20, "Analytics: "
					+ (Client.wurst.options.google_analytics.enabled ? "ON"
						: "OFF")));
		buttonList.add(new GuiButton(6, width / 2 - 50, height / 4 + 24 - 16,
			100, 20, "Keybinds"));
		buttonList.add(new GuiButton(7, width / 2 - 50, height / 4 + 48 - 16,
			100, 20, "X-Ray Blocks"));
		// this.buttonList.add(new GuiButton(8, this.width / 2 - 50, this.height
		// / 4 + 72 - 16, 100, 20, "???"));
		// this.buttonList.add(new GuiButton(9, this.width / 2 - 50, this.height
		// / 4 + 96 - 16, 100, 20, "???"));
		// this.buttonList.add(new GuiButton(10, this.width / 2 - 50,
		// this.height / 4 + 120 - 16, 100, 20, "???"));
		buttonList.add(new GuiButton(11, width / 2 + 54, height / 4 + 24 - 16,
			100, 20, "Wurst Website"));
		buttonList.add(new GuiButton(12, width / 2 + 54, height / 4 + 48 - 16,
			100, 20, "FAQ"));
		buttonList.add(new GuiButton(13, width / 2 + 54, height / 4 + 72 - 16,
			100, 20, "Report a Bug"));
		buttonList.add(new GuiButton(14, width / 2 + 54, height / 4 + 96 - 16,
			100, 20, "Suggest a Feature"));
		// buttonList.add(new GuiButton(15, width / 2 + 54, height / 4 + 120 -
		// 16, 100, 20, "???"));
		((GuiButton)buttonList.get(4)).enabled = !Minecraft.isRunningOnMac;
	}
	
	@Override
	protected void actionPerformed(GuiButton clickedButton)
	{
		if(clickedButton.enabled)
			if(clickedButton.id == 0)
				mc.displayGuiScreen(prevMenu);
			else if(clickedButton.id == 1)
			{// Middle Click Friends
				Client.wurst.options.middleClickFriends =
					!Client.wurst.options.middleClickFriends;
				clickedButton.displayString =
					"Click Friends: "
						+ (Client.wurst.options.middleClickFriends ? "ON"
							: "OFF");
				Client.wurst.fileManager.saveOptions();
			}else if(clickedButton.id == 2)
			{// Mod List
				Client.wurst.options.modListMode++;
				if(Client.wurst.options.modListMode > 2)
					Client.wurst.options.modListMode = 0;
				clickedButton.displayString =
					"Mod List: "
						+ modListModes[Client.wurst.options.modListMode];
				Client.wurst.fileManager.saveOptions();
			}else if(clickedButton.id == 3)
			{// AutoMaximize
				autoMaximize = !autoMaximize;
				clickedButton.displayString =
					"AutoMaximize: " + (autoMaximize ? "ON" : "OFF");
				Client.wurst.fileManager.saveAutoMaximize(autoMaximize);
			}else if(clickedButton.id == 4)
			{
				Client.wurst.options.wurstNews =
					!Client.wurst.options.wurstNews;
				clickedButton.displayString =
					"Wurst news: "
						+ (Client.wurst.options.wurstNews ? "ON" : "OFF");
				Client.wurst.fileManager.saveOptions();
			}else if(clickedButton.id == 5)
			{// Analytics
				Client.wurst.options.google_analytics.enabled =
					!Client.wurst.options.google_analytics.enabled;
				clickedButton.displayString =
					"Analytics: "
						+ (Client.wurst.options.google_analytics.enabled ? "ON"
							: "OFF");
				Client.wurst.fileManager.saveOptions();
			}else if(clickedButton.id == 6)
				mc.displayGuiScreen(new GuiKeybindManager(this));
			else if(clickedButton.id == 7)
				mc.displayGuiScreen(new GuiXRayBlocksManager(this));
			else if(clickedButton.id == 8)
			{	
				
			}else if(clickedButton.id == 9)
			{	
				
			}else if(clickedButton.id == 10)
			{	
				
			}else if(clickedButton.id == 11)
				MiscUtils.openLink("http://www.wurst-client.tk/");
			else if(clickedButton.id == 12)
				MiscUtils.openLink("http://www.wurst-client.tk/faq");
			else if(clickedButton.id == 13)
				MiscUtils.openLink("http://www.wurst-client.tk/bugs");
			else if(clickedButton.id == 14)
				MiscUtils.openLink("http://www.wurst-client.tk/ideas");
			else if(clickedButton.id == 15)
			{	
				
			}
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Wurst Options", width / 2, 40,
			0xffffff);
		drawCenteredString(fontRendererObj, "Settings", width / 2 - 104,
			height / 4 + 24 - 28, 0xcccccc);
		drawCenteredString(fontRendererObj, "Managers", width / 2,
			height / 4 + 24 - 28, 0xcccccc);
		drawCenteredString(fontRendererObj, "Online", width / 2 + 104,
			height / 4 + 24 - 28, 0xcccccc);
		super.drawScreen(par1, par2, par3);
		for(int i = 0; i < buttonList.size(); i++)
		{
			GuiButton button = (GuiButton)buttonList.get(i);
			if(button.isMouseOver() && !toolTips[button.id].isEmpty())
			{
				ArrayList toolTip =
					Lists.newArrayList(toolTips[button.id].split("\n"));
				drawHoveringText(toolTip, par1, par2);
				break;
			}
		}
	}
}
