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
import net.minecraft.client.gui.GuiYesNo;

import org.lwjgl.opengl.GL11;

import tk.wurst_client.Client;
import tk.wurst_client.alts.Alt;
import tk.wurst_client.alts.AltUtils;

public class GuiAlts extends GuiScreen
{
	private GuiScreen prevMenu;
	private boolean shouldAsk = true;
	private int errorTimer;
	public static GuiAltList altList;
	
	public GuiAlts(GuiScreen par1GuiScreen)
	{
		prevMenu = par1GuiScreen;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		altList = new GuiAltList(mc, this);
		altList.registerScrollButtons(7, 8);
		altList.elementClicked(-1, false, 0, 0);
		if(GuiAltList.alts.isEmpty() && shouldAsk)
			mc.displayGuiScreen(new GuiYesNo(this, "Your alt list is empty.", "Would you like some random alts to get started?", 0));
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 154, height - 52, 100, 20, "Use"));
		buttonList.add(new GuiButton(1, width / 2 - 50, height - 52, 100, 20, "Direct Login"));
		buttonList.add(new GuiButton(2, width / 2 + 54, height - 52, 100, 20, "Add"));
		buttonList.add(new GuiButton(3, width / 2 - 154, height - 28, 100, 20, "Edit"));
		buttonList.add(new GuiButton(4, width / 2 - 50, height - 28, 100, 20, "Delete"));
		buttonList.add(new GuiButton(5, width / 2 + 54, height - 28, 100, 20, "Cancel"));
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		((GuiButton)buttonList.get(0)).enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
		((GuiButton)buttonList.get(3)).enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
		((GuiButton)buttonList.get(4)).enabled = !GuiAltList.alts.isEmpty() && altList.getSelectedSlot() != -1;
	}
	
	@Override
	protected void actionPerformed(GuiButton clickedButton)
	{
		if(clickedButton.enabled)
			if(clickedButton.id == 0)
			{// Use
				Alt alt = GuiAltList.alts.get(altList.getSelectedSlot());
				if(alt.cracked)
				{// Cracked
					AltUtils.changeCrackedName(alt.name);
					mc.displayGuiScreen(prevMenu);
				}else
				{// Premium
					String reply = AltUtils.login(alt.name, alt.password);
					if(reply.equals(""))
						mc.displayGuiScreen(prevMenu);
					else
					{
						errorTimer = 8;
						if(reply.equals("§4§lWrong password!"))
						{
							GuiAltList.alts.remove(GuiAltList.alts.indexOf(alt));
							Client.wurst.fileManager.saveAlts();
						}
					}
				}
			}else if(clickedButton.id == 1)
				mc.displayGuiScreen(new GuiAltLogin(this));
			else if(clickedButton.id == 2)
				mc.displayGuiScreen(new GuiAltAdd(this));
			else if(clickedButton.id == 3)
			{// Edit
				Alt alt = GuiAltList.alts.get(altList.getSelectedSlot());
				mc.displayGuiScreen(new GuiAltEdit(this, alt));
			}else if(clickedButton.id == 4)
			{// Delete
				Alt alt = GuiAltList.alts.get(altList.getSelectedSlot());
				String deleteQuestion = "Are you sure you want to remove this alt?";
				String deleteWarning = "\"" + alt.name + "\" will be lost forever! (A long time!)";
				mc.displayGuiScreen(new GuiYesNo(this, deleteQuestion, deleteWarning, "Delete", "Cancel", 1));
			}else if(clickedButton.id == 5)
				mc.displayGuiScreen(prevMenu);
	}
	
	@Override
	public void confirmClicked(boolean par1, int par2)
	{
		if(par2 == 0)
		{
			if(par1)
			{
				for(int i = 0; i < 8; i++)
					GuiAltList.alts.add(new Alt(AltUtils.generateName(), null));
				GuiAltList.sortAlts();
				Client.wurst.fileManager.saveAlts();
			}
			shouldAsk = false;
		}else if(par2 == 1)
		{
			Alt alt = GuiAltList.alts.get(altList.getSelectedSlot());
			if(par1)
			{
				GuiAltList.alts.remove(GuiAltList.alts.indexOf(alt));
				Client.wurst.fileManager.saveAlts();
			}
		}
		mc.displayGuiScreen(this);
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
				altList.elementClicked(-1, false, 0, 0);
		super.mouseClicked(par1, par2, par3);
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		altList.drawScreen(par1, par2, par3);
		if(altList.getSelectedSlot() != -1)
		{
			Alt alt = GuiAltList.alts.get(altList.getSelectedSlot());
			AltRenderer.drawAltBack(alt.name, (width / 2 - 125) / 2 - 32, height / 2 - 64 - 9, 64, 128);
			AltRenderer.drawAltBody(alt.name, width - (width / 2 - 140) / 2 - 32, height / 2 - 64 - 9, 64, 128);
		}
		drawCenteredString(fontRendererObj, "Alt Manager", width / 2, 4, 16777215);
		drawCenteredString(fontRendererObj, "Alts: " + GuiAltList.alts.size(), width / 2, 14, 10526880);
		drawCenteredString(fontRendererObj, "premium: " + GuiAltList.premiumAlts + ", cracked: " + GuiAltList.crackedAlts, width / 2, 24, 10526880);
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
