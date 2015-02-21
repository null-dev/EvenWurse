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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import tk.wurst_client.Client;
import tk.wurst_client.alts.Alt;
import tk.wurst_client.alts.LoginManager;
import tk.wurst_client.alts.NameGenerator;
import tk.wurst_client.alts.SkinStealer;
import tk.wurst_client.utils.MiscUtils;

public class GuiAltAdd extends GuiScreen
{
	private GuiScreen prevMenu;
	private GuiTextField emailBox;
	private GuiPasswordField passwordBox;
	private String displayText = "";
	private int errorTimer;
	
	public GuiAltAdd(GuiScreen par1GuiScreen)
	{
		prevMenu = par1GuiScreen;
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		emailBox.updateCursorCounter();
		passwordBox.updateCursorCounter();
		((GuiButton)buttonList.get(0)).enabled =
			emailBox.getText().trim().length() > 0
				&& (!emailBox.getText().trim().equalsIgnoreCase("Alexander01998") || passwordBox.getText().length() != 0);
		((GuiButton)buttonList.get(3)).enabled = !emailBox.getText().trim().equalsIgnoreCase("Alexander01998");
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
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 72 + 12, "Add"));
		buttonList.add(new GuiButton(3, width / 2 - 100, height / 4 + 96 + 12, "Random Name"));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, "Cancel"));
		buttonList.add(new GuiButton(4, width - (width / 2 - 100) / 2 - 64, height - 32, 128, 20, "Steal Skin"));
		buttonList.add(new GuiButton(5, (width / 2 - 100) / 2 - 64, height - 32, 128, 20, "Open Skin Folder"));
		emailBox = new GuiTextField(0, fontRendererObj, width / 2 - 100, 60, 200, 20);
		emailBox.setMaxStringLength(48);
		emailBox.setFocused(true);
		emailBox.setText(Minecraft.getMinecraft().session.getUsername());
		passwordBox = new GuiPasswordField(fontRendererObj, width / 2 - 100, 100, 200, 20);
		passwordBox.setFocused(false);
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
			{// Add
				if(passwordBox.getText().length() == 0)
				{// Cracked
					GuiAltList.alts.add(new Alt(emailBox.getText()));
					displayText = "";
				}else
				{// Premium
					displayText = LoginManager.check(emailBox.getText(), passwordBox.getText());
					if(displayText.equals(""))
						GuiAltList.alts.add(new Alt(emailBox.getText(), passwordBox.getText()));
				}
				if(displayText.equals(""))
				{
					GuiAltList.sortAlts();
					Client.wurst.fileManager.saveAlts();
					mc.displayGuiScreen(prevMenu);
				}else
					errorTimer = 8;
			}else if(clickedButton.id == 3)
				emailBox.setText(NameGenerator.generateName());
			else if(clickedButton.id == 4)
				displayText = SkinStealer.stealSkin(emailBox.getText());
			else if(clickedButton.id == 5)
				MiscUtils.openFile(Client.wurst.fileManager.skinDir);
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{
		emailBox.textboxKeyTyped(par1, par2);
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
		emailBox.mouseClicked(par1, par2, par3);
		passwordBox.mouseClicked(par1, par2, par3);
		if(emailBox.isFocused() || passwordBox.isFocused())
			displayText = "";
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		AltRenderer.drawAltBack(emailBox.getText(), (width / 2 - 100) / 2 - 64, height / 2 - 128, 128, 256);
		AltRenderer.drawAltBody(emailBox.getText(), width - (width / 2 - 100) / 2 - 64, height / 2 - 128, 128, 256);
		drawCenteredString(fontRendererObj, "Add an Alt", width / 2, 20, 16777215);
		drawString(fontRendererObj, "Name or E-Mail", width / 2 - 100, 47, 10526880);
		drawString(fontRendererObj, "Password", width / 2 - 100, 87, 10526880);
		drawCenteredString(fontRendererObj, displayText, width / 2, 142, 16777215);
		emailBox.drawTextBox();
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
