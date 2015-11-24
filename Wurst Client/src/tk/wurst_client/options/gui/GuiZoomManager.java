/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.options.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.WurstClient;
import tk.wurst_client.options.Options;

public class GuiZoomManager extends GuiScreen implements GuiPressAKeyCallback
{
	private GuiScreen prevMenu;
	
	public GuiZoomManager(GuiScreen par1GuiScreen)
	{
		prevMenu = par1GuiScreen;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 144 - 16,
			200, 20, "Back"));
		buttonList.add(new GuiButton(1, width / 2 - 79, height / 4 + 24 - 16,
			158, 20, "Zoom Key: "
				+ Keyboard
					.getKeyName(WurstClient.INSTANCE.options.zoom.keybind)));
		buttonList.add(new GuiButton(2, width / 2 - 79, height / 4 + 72 - 16,
			50, 20, "More"));
		buttonList.add(new GuiButton(3, width / 2 - 25, height / 4 + 72 - 16,
			50, 20, "Less"));
		buttonList.add(new GuiButton(4, width / 2 + 29, height / 4 + 72 - 16,
			50, 20, "Default"));
		WurstClient.INSTANCE.analytics.trackPageView(
			"/options/keybind-manager", "Keybind Manager");
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{	
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.enabled)
			switch(button.id)
			{
				case 0:
					// Back
					mc.displayGuiScreen(prevMenu);
					break;
				case 1:
					// Zoom Key
					mc.displayGuiScreen(new GuiPressAKey(this));
					break;
				case 2:
					// Zoom Level More
					WurstClient.INSTANCE.options.zoom.level =
						Math.min(
							Math.round(WurstClient.INSTANCE.options.zoom.level * 10F + 1F) / 10F,
							10F);
					WurstClient.INSTANCE.fileManager.saveOptions();
					break;
				case 3:
					// Zoom Level Less
					WurstClient.INSTANCE.options.zoom.level =
						Math.max(
							Math.round(WurstClient.INSTANCE.options.zoom.level * 10F - 1F) / 10F,
							1F);
					WurstClient.INSTANCE.fileManager.saveOptions();
					break;
				case 4:
					// Zoom Level Default
					WurstClient.INSTANCE.options.zoom.level =
						new Options().zoom.level;
					WurstClient.INSTANCE.fileManager.saveOptions();
					break;
			}
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{	
		
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawBackground(0);
		drawCenteredString(fontRendererObj, "Zoom Manager", width / 2, 40,
			0xffffff);
		drawString(fontRendererObj, "Zoom Level: "
			+ WurstClient.INSTANCE.options.zoom.level, width / 2 - 75,
			height / 4 + 44, 0xcccccc);
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	public void setKey(String key)
	{
		WurstClient.INSTANCE.options.zoom.keybind = Keyboard.getKeyIndex(key);
		WurstClient.INSTANCE.fileManager.saveOptions();
		((GuiButton)buttonList.get(1)).displayString = "Zoom Key: " + key;
	}
}
