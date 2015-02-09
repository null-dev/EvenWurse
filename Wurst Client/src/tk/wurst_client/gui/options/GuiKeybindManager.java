/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.options;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.Client;
import tk.wurst_client.module.Module;

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
		GuiKeybindList.sortModules();
		bindList.elementClicked(-1, false, 0, 0);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height - 52, 98, 20, "Change Bind"));
		buttonList.add(new GuiButton(1, width / 2 + 2, height - 52, 98, 20, "Clear Bind"));
		buttonList.add(new GuiButton(2, width / 2 - 100, height - 28, 200, 20, "Back"));
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		((GuiButton)buttonList.get(0)).enabled = bindList.getSelectedSlot() != -1;
		((GuiButton)buttonList.get(1)).enabled = bindList.getSelectedSlot() != -1 && Client.wurst.moduleManager.activeModules.get(Client.wurst.moduleManager.activeModules.indexOf(GuiKeybindList.modules.get(bindList.getSelectedSlot()))).getBind() != 0;
	}
	
	@Override
	protected void actionPerformed(GuiButton clickedButton)
	{
		if(clickedButton.enabled)
			if(clickedButton.id == 0)
			{// Change Bind
				Module module = Client.wurst.moduleManager.activeModules.get(Client.wurst.moduleManager.activeModules.indexOf(GuiKeybindList.modules.get(bindList.getSelectedSlot())));
				mc.displayGuiScreen(new GuiKeybindChange(this, module));
			}else if(clickedButton.id == 1)
			{// Clear Bind
				Module module = Client.wurst.moduleManager.activeModules.get(Client.wurst.moduleManager.activeModules.indexOf(GuiKeybindList.modules.get(bindList.getSelectedSlot())));
				module.setBind(0);
				Client.wurst.fileManager.saveModules();
				GuiKeybindList.sortModules();
			}else if(clickedButton.id == 2)
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
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
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
		drawCenteredString(fontRendererObj, "Keybind Manager", width / 2, 8, 16777215);
		int totalBinds = 0;
		for(int i = 0; i < GuiKeybindList.modules.size(); i++)
			if(GuiKeybindList.modules.get(i).getBind() != 0)
				totalBinds++;
		drawCenteredString(fontRendererObj, "Keybinds: " + totalBinds + ", Mods: " + Client.wurst.moduleManager.activeModules.size(), width / 2, 20, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
