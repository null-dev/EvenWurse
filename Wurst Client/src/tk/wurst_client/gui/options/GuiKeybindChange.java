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

import org.lwjgl.input.Keyboard;

import tk.wurst_client.Client;
import tk.wurst_client.module.Module;

public class GuiKeybindChange extends GuiScreen
{
	private GuiScreen prevMenu;
	private Module module;
	private int key;
	
	public GuiKeybindChange(GuiScreen prevMenu, Module module)
	{
		this.prevMenu = prevMenu;
		this.module = module;
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height - 72, "Save"));
		buttonList.add(new GuiButton(1, width / 2 - 100, height - 48, "Cancel"));
		key = module.getBind();
	}
	
	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	protected void actionPerformed(GuiButton clickedButton)
	{
		if(clickedButton.enabled)
			if(clickedButton.id == 1)
				mc.displayGuiScreen(prevMenu);
			else if(clickedButton.id == 0)
			{// Save
				module.setBind(key);
				Client.wurst.fileManager.saveModules();
				GuiKeybindList.sortModules();
				mc.displayGuiScreen(prevMenu);
			}
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{
		key = par2;
	}
	
	/**
	 * Called when the mouse is clicked.
	 * 
	 * @throws IOException
	 */
	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException
	{
		super.mouseClicked(par1, par2, par3);
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawBackground(0);
		drawCenteredString(fontRendererObj, "Change this Keybind", width / 2, 20, 16777215);
		drawCenteredString(fontRendererObj, "Press a key to change the keybind.", width / 2, height / 4 + 48, 10526880);
		String category = module.getCategory().name();
		if(!category.equals("WIP"))
			category = category.charAt(0) + category.substring(1).toLowerCase();
		drawCenteredString(fontRendererObj, "Mod: " + module.getName(), width / 2, height / 4 + 68, 10526880);
		drawCenteredString(fontRendererObj, "Category: " + category, width / 2, height / 4 + 78, 10526880);
		drawCenteredString(fontRendererObj, "Keybind: " + Keyboard.getKeyName(key), width / 2, height / 4 + 88, 10526880);
		super.drawScreen(par1, par2, par3);
	}
}
