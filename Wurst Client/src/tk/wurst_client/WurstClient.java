/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client;

import net.minecraft.client.gui.ServerListEntryNormal;

import org.darkstorm.minecraft.gui.theme.wurst.WurstTheme;

import tk.wurst_client.analytics.Analytics;
import tk.wurst_client.chat.ChatMessenger;
import tk.wurst_client.commands.CmdManager;
import tk.wurst_client.events.EventManager;
import tk.wurst_client.files.FileManager;
import tk.wurst_client.gui.GuiManager;
import tk.wurst_client.mods.ModManager;
import tk.wurst_client.options.Friends;
import tk.wurst_client.options.Keybinds;
import tk.wurst_client.options.Options;
import tk.wurst_client.update.Updater;

public enum WurstClient
{
	INSTANCE;
	
	public static final String VERSION = "2.3.1";
	public String currentServerIP = "127.0.0.1:25565";
	public ServerListEntryNormal lastServer;
	public boolean startupMessageDisabled = false;
	
	public ChatMessenger chat;
	public CmdManager cmdManager;
	public EventManager eventManager;
	public FileManager fileManager;
	public Friends friends;
	public GuiManager guiManager;
	public ModManager modManager;
	public Keybinds keybinds;
	public Options options;
	public Updater updater;
	public Analytics analytics;
	
	public void startClient()
	{
		eventManager = new EventManager();
		modManager = new ModManager();
		guiManager = new GuiManager();
		cmdManager = new CmdManager();
		fileManager = new FileManager();
		updater = new Updater();
		chat = new ChatMessenger();
		keybinds = new Keybinds();
		options = new Options();
		friends = new Friends();
		
		fileManager.init();
		guiManager.setTheme(new WurstTheme());
		guiManager.setup();
		updater.checkForUpdate();
		analytics = new Analytics("UA-52838431-5", "client.wurst-client.tk");
	}
}
