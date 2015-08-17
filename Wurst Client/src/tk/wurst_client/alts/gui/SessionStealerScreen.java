/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors | All rights
 * reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.alts.gui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.utils.MiscUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class SessionStealerScreen extends GuiScreen
{
	protected GuiScreen prevMenu;
	protected GuiTextField tokenBox;
	
	protected String errorText = "";
	protected String helpText = "";
	
	public SessionStealerScreen(GuiScreen par1GuiScreen)
	{
		prevMenu = par1GuiScreen;
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		tokenBox.updateCursorCounter();
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
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 72 + 12,
			"Login to session"));
		buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96 + 12,
			"Search for tokens on Google"));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12,
			"Cancel"));
		tokenBox =
			new GuiTextField(1, fontRendererObj, width / 2 - 100, 60, 200, 20);
		tokenBox.setMaxStringLength(65);
		tokenBox.setFocused(true);
		
		// TODO: Remove
		if(mc.session.getToken() != null)
			tokenBox.setText(mc.session.getSessionID().substring(6));
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
	protected void actionPerformed(GuiButton button)
	{
		if(button.enabled)
			if(button.id == 1)
				mc.displayGuiScreen(prevMenu);
			else if(button.id == 0)
			{
				// validate input
				String input = tokenBox.getText();
				if(input.length() != 65 || !input.substring(32, 33).equals(":")
					|| input.split(":").length != 2)
				{
					errorText = "That is not a session token!";
					return;
				}
				
				String uuid = input.split(":")[1];
				
				// fetch name history
				JsonElement rawJson;
				try
				{
					rawJson =
						new JsonParser().parse(new InputStreamReader(new URL(
							"https://api.mojang.com/user/profiles/" + uuid
								+ "/names").openConnection().getInputStream()));
				}catch(JsonIOException | JsonSyntaxException | IOException e)
				{
					e.printStackTrace();
					errorText = "An error occurred";
					helpText = "Mojang servers might be down.";
					return;
				}
				
				// validate UUID
				if(!rawJson.isJsonArray())
				{
					errorText = "Invalid UUID";
					helpText =
						"This account is immune to session stealing (or fake).";
					return;
				}
				
				// get latest name
				JsonArray json = rawJson.getAsJsonArray();
				String name =
					json.get(json.size() - 1).getAsJsonObject().get("name")
						.getAsString();
				
				// login
				mc.session =
					new Session(name, uuid, input.split(":")[0], "mojang");
				mc.displayGuiScreen(prevMenu);
				
			}else if(button.id == 2)
				MiscUtils
					.openLink("https://www.google.com/search?q=%22session+id+is+token%22&tbs=qdr:m");
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{
		tokenBox.textboxKeyTyped(par1, par2);
		
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
		super.mouseClicked(par1, par2, par3);
		tokenBox.mouseClicked(par1, par2, par3);
		if(tokenBox.isFocused())
		{
			errorText = "";
			helpText = "";
		}
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		
		drawCenteredString(fontRendererObj, "Session Stealer", width / 2, 20,
			16777215);
		drawString(fontRendererObj, "Session ID is token:", width / 2 - 100,
			47, 10526880);
		drawCenteredString(fontRendererObj, "§c" + errorText, width / 2, 96,
			0xFFFFFF);
		drawCenteredString(fontRendererObj, "§7" + helpText, width / 2, 112,
			0xFFFFFF);
		
		tokenBox.drawTextBox();
		
		super.drawScreen(par1, par2, par3);
	}
}
