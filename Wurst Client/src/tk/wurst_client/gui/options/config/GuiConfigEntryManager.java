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
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Module;
import tk.wurst_client.api.ModuleConfiguration;
import tk.wurst_client.utils.ModuleUtils;

import java.io.IOException;

public class GuiConfigEntryManager extends GuiScreen
{
	private GuiScreen prevMenu;
	public GuiConfigEntryList configList;
	private Module module;

	public GuiConfigEntryManager(GuiScreen par1GuiScreen, Module m)
	{
		this.module = m;
		prevMenu = par1GuiScreen;
	}

	void reloadList() {
		configList = new GuiConfigEntryList(mc, module,this);
		configList.registerScrollButtons(7, 8);
		configList.elementClicked(-1, false, 0, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		reloadList();
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height - 52, 98, 20,
				"Edit"));
		buttonList.add(new GuiButton(1, width / 2 + 2, height - 52, 98, 20,
				"Remove"));
		buttonList.add(new GuiButton(2, width / 2 - 100, height - 28, 98, 20,
				"Add"));
		buttonList.add(new GuiButton(3, width / 2 + 2, height - 28, 98, 20,
				"Back"));
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		boolean selected = configList.getSelectedSlot() != -1 && !ModuleConfiguration.CONFIGURATION.isEmpty();
		((GuiButton)buttonList.get(0)).enabled = selected;
		((GuiButton)buttonList.get(1)).enabled = selected;
	}

	String getSelectedKey() {
		return configList.elements.get(configList.getSelectedSlot()).getKey();
	}

	String getSelectedValue() {
		return configList.elements.get(configList.getSelectedSlot()).getValue();
	}

	@Override
	protected void actionPerformed(GuiButton clickedButton)
	{
		if(clickedButton.enabled) {
			if(clickedButton.id == 0)
				mc.displayGuiScreen(new GuiConfigEdit(this, module, getSelectedKey(), getSelectedValue()));
			else if(clickedButton.id == 1)
			{
				ModuleConfiguration.forModule(module).getConfig().remove(getSelectedKey());
				WurstClient.INSTANCE.files.saveModuleConfigs();
				reloadList();
			}
			else if(clickedButton.id == 2) {
				mc.displayGuiScreen(new GuiConfigAdd(this, module));
			}
			else if(clickedButton.id == 3)
				mc.displayGuiScreen(prevMenu);
		}
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
		drawCenteredString(fontRendererObj, ModuleUtils.getModuleName(module) + " Configuration", width / 2,
				8, 16777215);
		drawCenteredString(fontRendererObj, "Total Configurable Entries: "
						+ ModuleConfiguration.forModule(module).getConfig().size(),
				width / 2, 20, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
