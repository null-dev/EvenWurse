/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mod.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.Session;
import tk.wurst_client.Client;
import tk.wurst_client.alts.NameGenerator;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.Mod.Category;
import tk.wurst_client.mod.Mod.Info;
import tk.wurst_client.servers.ServerConnector;
import tk.wurst_client.servers.ServerConnector.Connection;
import tk.wurst_client.utils.EmptyFutureListener;

@Info(category = Category.WIP,
	description = "Spawns a ton of random players that spam chat\n"
		+ "messages.",
	name = "Pwnage")
public class Pwnage extends Mod implements UpdateListener
{
	private ServerConnector connector;
	
	@Override
	public void onEnable()
	{
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(connector != null && connector.connection == null)
			return;
		if(connector != null && connector.connection == Connection.SUCCESSFUL)
			connector.networkManager.sendPacket(new C01PacketChatMessage(
				"Wurst Client!"), new EmptyFutureListener());
		connector = new ServerConnector
			(
				Minecraft.getMinecraft().currentScreen,
				Minecraft.getMinecraft()
			);
		connector.connect
			(
				Client.wurst.currentServerIP.split(":")[0],
				Integer.valueOf(Client.wurst.currentServerIP.split(":")[1]),
				new Session(NameGenerator.generateName(), "", "", "mojang")
			);
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
	}
}
