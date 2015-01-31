/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.Session;
import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.servers.ServerConnector;
import tk.wurst_client.servers.ServerConnector.Connection;
import tk.wurst_client.utils.AltUtils;
import tk.wurst_client.utils.EmptyFutureListener;

public class Pwnage extends Module
{
	public Pwnage()
	{
		super(
			"Pwnage",
			"Spawns a ton of random players that spam chat\n"
				+ "messages.",
				0,
				Category.WIP);
	}

	private ServerConnector connector;

	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(connector != null && connector.connection == null)
			return;
		if(connector != null && connector.connection == Connection.SUCCESSFUL)
			connector.networkManager.sendPacket(new C01PacketChatMessage("Wurst Client!"), new EmptyFutureListener());
		connector = new ServerConnector
			(
				Minecraft.getMinecraft().currentScreen,
				Minecraft.getMinecraft()
			);
		connector.connect
			(
				Client.Wurst.currentServerIP.split(":")[0],
				Integer.valueOf(Client.Wurst.currentServerIP.split(":")[1]),
				new Session(AltUtils.generateName(), "", "", "mojang")
			);
	}
}
