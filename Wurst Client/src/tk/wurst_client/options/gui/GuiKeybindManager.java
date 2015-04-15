/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.options.gui;

import java.io.IOException;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.Client;

public class GuiKeybindManager extends GuiScreen
{
	private GuiScreen prevMenu;
	public static GuiKeybindList bindList;
	
	public GuiKeybindManager(GuiScreen par1GuiScreen)
	{
		prevMenu = par1GuiScreen;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		bindList = new GuiKeybindList(mc, this);
		bindList.registerScrollButtons(7, 8);
		bindList.elementClicked(-1, false, 0, 0);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 102, height - 52, 100, 20,
			"Add"));
		buttonList.add(new GuiButton(1, width / 2 + 2, height - 52, 100, 20,
			"Edit"));
		buttonList.add(new GuiButton(2, width / 2 - 102, height - 28, 100, 20,
			"Remove"));
		buttonList.add(new GuiButton(3, width / 2 + 2, height - 28, 100, 20,
			"Back"));
		Client.wurst.analytics.trackPageView("/options/keybind-manager",
			"Keybind Manager");
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		((GuiButton)buttonList.get(1)).enabled =
			bindList.getSelectedSlot() != -1;
		((GuiButton)buttonList.get(2)).enabled =
			bindList.getSelectedSlot() != -1;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void actionPerformed(GuiButton clickedButton)
	{
		if(clickedButton.enabled)
			if(clickedButton.id == 0)
				mc.displayGuiScreen(new GuiKeybindChange(this, null));
			else if(clickedButton.id == 1)
			{
				Entry<String, String> entry =
					Client.wurst.keybinds.entrySet().toArray(
						new Entry[Client.wurst.keybinds.size()])[bindList
						.getSelectedSlot()];
				mc.displayGuiScreen(new GuiKeybindChange(this, entry));
			}else if(clickedButton.id == 2)
			{
				Entry<String, String> entry =
					Client.wurst.keybinds.entrySet().toArray(
						new Entry[Client.wurst.keybinds.size()])[bindList
						.getSelectedSlot()];
				Client.wurst.keybinds.remove(entry.getKey());
				Client.wurst.fileManager.saveKeybinds();
				Client.wurst.analytics.trackEvent("keybinds", "remove",
					entry.getKey());
			}else if(clickedButton.id == 3)
				mc.displayGuiScreen(prevMenu);
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{
		if(par2 == 28 || par2 == 156)
			actionPerformed((GuiButton)buttonList.get(0));
	}
	
	/**
	 * Called when the mouse is clicked.
	 *
	 * @throws IOException
	 */
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
		throws IOException
	{
		if(par2 >= 36 && par2 <= height - 57)
			if(par1 >= width / 2 + 140 || par1 <= width / 2 - 126)
				bindList.elementClicked(-1, false, 0, 0);
		super.mouseClicked(par1, par2, par3);
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		bindList.drawScreen(par1, par2, par3);
		drawCenteredString(fontRendererObj, "Keybind Manager", width / 2, 8,
			16777215);
		drawCenteredString(fontRendererObj, "Keybinds: "
			+ Client.wurst.keybinds.size(), width / 2, 20, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
