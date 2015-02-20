/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.alts;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import tk.wurst_client.Client;
import tk.wurst_client.alts.Alt;
import tk.wurst_client.alts.AltUtils;
import tk.wurst_client.utils.MiscUtils;

public class GuiAltEdit extends GuiScreen
{
	private GuiScreen prevMenu;
	private GuiTextField nameBox;
	private GuiPasswordField passwordBox;
	private String displayText = "";
	private int errorTimer;
	private Alt alt;
	
	public GuiAltEdit(GuiScreen par1GuiScreen, Alt alt)
	{
		prevMenu = par1GuiScreen;
		this.alt = alt;
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		nameBox.updateCursorCounter();
		passwordBox.updateCursorCounter();
		((GuiButton)buttonList.get(0)).enabled =
			nameBox.getText().trim().length() > 0
				&& (!nameBox.getText().trim().equalsIgnoreCase("Alexander01998") || passwordBox.getText().length() != 0);
		((GuiButton)buttonList.get(3)).enabled = !nameBox.getText().trim().equalsIgnoreCase("Alexander01998");
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
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 72 + 12, "Save"));
		buttonList.add(new GuiButton(3, width / 2 - 100, height / 4 + 96 + 12, "Random Name"));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, "Cancel"));
		buttonList.add(new GuiButton(4, width - (width / 2 - 100) / 2 - 64, height - 32, 128, 20, "Steal Skin"));
		buttonList.add(new GuiButton(5, (width / 2 - 100) / 2 - 64, height - 32, 128, 20, "Open Skin Folder"));
		nameBox = new GuiTextField(0, fontRendererObj, width / 2 - 100, 60, 200, 20);
		nameBox.setMaxStringLength(48);
		nameBox.setFocused(true);
		nameBox.setText(alt.name);
		passwordBox = new GuiPasswordField(fontRendererObj, width / 2 - 100, 100, 200, 20);
		passwordBox.setFocused(false);
		passwordBox.setText(alt.password);
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
		{
			Alt newAlt = new Alt(nameBox.getText(), passwordBox.getText());
			if(clickedButton.id == 1)
				mc.displayGuiScreen(prevMenu);
			else if(clickedButton.id == 0)
			{// Save
				if(passwordBox.getText().length() == 0)
				{// Cracked
					GuiAltList.alts.set(GuiAltList.alts.indexOf(alt), newAlt);
					displayText = "";
				}else
				{// Premium
					displayText = AltUtils.check(nameBox.getText(), passwordBox.getText());
					if(displayText.equals(""))
						GuiAltList.alts.set(GuiAltList.alts.indexOf(alt), newAlt);
				}
				if(displayText.equals(""))
				{
					GuiAltList.sortAlts();
					Client.wurst.fileManager.saveAlts();
					mc.displayGuiScreen(prevMenu);
					GuiAlts.altList.elementClicked(GuiAltList.alts.indexOf(newAlt), false, 0, 0);
				}else
					errorTimer = 8;
			}else if(clickedButton.id == 3)
				nameBox.setText(AltUtils.generateName());
			else if(clickedButton.id == 4)
				displayText = AltUtils.stealSkin(newAlt.name);
			else if(clickedButton.id == 5)
				MiscUtils.openFile(Client.wurst.fileManager.skinDir);
		}
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{
		nameBox.textboxKeyTyped(par1, par2);
		passwordBox.textboxKeyTyped(par1, par2);
		
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
		nameBox.mouseClicked(par1, par2, par3);
		passwordBox.mouseClicked(par1, par2, par3);
		if(nameBox.isFocused() || passwordBox.isFocused())
			displayText = "";
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		AltUtils.drawAltBack(nameBox.getText(), (width / 2 - 100) / 2 - 64, height / 2 - 128, 128, 256);
		AltUtils.drawAltBody(nameBox.getText(), width - (width / 2 - 100) / 2 - 64, height / 2 - 128, 128, 256);
		drawCenteredString(fontRendererObj, "Edit this Alt", width / 2, 20, 16777215);
		drawString(fontRendererObj, "Name or E-Mail", width / 2 - 100, 47, 10526880);
		drawString(fontRendererObj, "Password", width / 2 - 100, 87, 10526880);
		drawCenteredString(fontRendererObj, displayText, width / 2, 142, 16777215);
		nameBox.drawTextBox();
		passwordBox.drawTextBox();
		if(errorTimer > 0)
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL_CULL_FACE);
			GL11.glEnable(GL_BLEND);
			GL11.glColor4f(1.0F, 0.0F, 0.0F, (float)errorTimer / 16);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(0, 0);
				GL11.glVertex2d(width, 0);
				GL11.glVertex2d(width, height);
				GL11.glVertex2d(0, height);
			}
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL_CULL_FACE);
			GL11.glDisable(GL_BLEND);
			errorTimer--;
		}
		super.drawScreen(par1, par2, par3);
	}
}
