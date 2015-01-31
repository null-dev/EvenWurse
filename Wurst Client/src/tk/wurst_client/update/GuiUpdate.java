/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.update;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.Client;

public class GuiUpdate extends GuiScreen
{
	public GuiUpdate()
	{	
		
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{	
		
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, "Close Minecraft"));
		Client.Wurst.updater.openUpdateLink();
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
			if(clickedButton.id == 0)
				mc.shutdown();
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
		super.mouseClicked(par1, par2, par3);
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "The link to the new version of Wurst", width / 2, height / 2 - 64, 16777215);
		drawCenteredString(fontRendererObj, "should have openened in your browser.", width / 2, height / 2 - 52, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
