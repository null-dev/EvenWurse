/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import tk.wurst_client.events.EventManager;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MOVEMENT,
	description = "Makes it harder for other players to see where you are.\n"
		+ "They will think you are lagging badly, because your\n"
		+ "position will only be updated every 3 seconds.",
	name = "Blink")
public class BlinkMod extends Mod implements UpdateListener
{
	private static ArrayList<Packet> packets = new ArrayList<Packet>();
	
	@Override
	public void onEnable()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		if(hasTimePassedM(3000))
		{
			for(Packet packet : packets)
				Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(packet);
			packets.clear();
			updateLastMS();
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
	
	public static void addToBlinkQueue(Packet packet)
	{
		packets.add(packet);
	}
}
