/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client;

import net.minecraft.client.gui.ServerListEntryNormal;

import org.darkstorm.minecraft.gui.theme.wurst.WurstTheme;

import tk.wurst_client.command.ChatMessenger;
import tk.wurst_client.command.CommandManager;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.files.FileManager;
import tk.wurst_client.gui.GuiManager;
import tk.wurst_client.mod.ModManager;
import tk.wurst_client.options.Friends;
import tk.wurst_client.options.Keybinds;
import tk.wurst_client.options.Options;
import tk.wurst_client.update.Updater;

public class Client
{
	public final String CLIENT_VERSION = "1.11";
	public String currentServerIP = "127.0.0.1:25565";
	public ServerListEntryNormal lastServer;
	public boolean startupMessageDisabled = false;
	
	public ChatMessenger chat;
	public CommandManager commandManager;
	public FileManager fileManager;
	public Friends friends;
	public GuiManager guiManager;
	public ModManager modManager;
	public Keybinds keybinds;
	public Options options;
	public Updater updater;
	
	public static final Client wurst = new Client();
	
	public void startClient()
	{
		modManager = new ModManager();
		guiManager = new GuiManager();
		commandManager = new CommandManager();
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
		EventManager.init();
	}
}
