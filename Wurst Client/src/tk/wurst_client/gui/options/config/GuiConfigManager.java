/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.options.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.api.Module;
import tk.wurst_client.api.ModuleConfiguration;

import java.io.IOException;

public class GuiConfigManager extends GuiScreen
{
	private GuiScreen prevMenu;
	public GuiConfigList configList;

	public GuiConfigManager(GuiScreen par1GuiScreen)
	{
		prevMenu = par1GuiScreen;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		configList = new GuiConfigList(mc, this);
		configList.registerScrollButtons(7, 8);
		configList.elementClicked(-1, false, 0, 0);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height - 52, 200, 20,
			"Edit"));
		buttonList.add(new GuiButton(1, width / 2 - 100, height - 28, 200, 20,
			"Back"));
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		((GuiButton)buttonList.get(0)).enabled =
			configList.getSelectedSlot() != -1 && !ModuleConfiguration.CONFIGURATION.isEmpty();
	}

	@Override
	protected void actionPerformed(GuiButton clickedButton)
	{
		if(clickedButton.enabled)
			if(clickedButton.id == 0) {
				int s = configList.getSelectedSlot();
				Module module;
				String name = configList.elements.get(s).getKey();
				try {
					module = configList.moduleFromClassName(name);
					if(module == null)
						throw new ClassNotFoundException();
				} catch (ClassNotFoundException e) {
					System.out.println("[EvenWurse] Could not find module associated with config class: '" + name + "'!");
					return;
				}
				mc.displayGuiScreen(new GuiConfigEntryManager(this, module));
			} else if(clickedButton.id == 1)
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
				configList.elementClicked(-1, false, 0, 0);
		super.mouseClicked(par1, par2, par3);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		configList.drawScreen(par1, par2, par3);
		drawCenteredString(fontRendererObj, "Module Configuration Manager", width / 2,
			8, 16777215);
		drawCenteredString(fontRendererObj, "Total Configurable Modules: "
				+ ModuleConfiguration.CONFIGURATION.size(),
			width / 2, 20, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
