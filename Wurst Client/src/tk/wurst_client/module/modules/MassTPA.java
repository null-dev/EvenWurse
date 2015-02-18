/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.StringUtils;
import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class MassTPA extends Module
{
	public MassTPA()
	{
		super(
			"MassTPA",
			"Sends a TPA requests to all players.\n"
				+ "Stops if someone accepts.",
			0,
			Category.CHAT);
	}
	
	private float speed = 1F;
	private int i;
	private ArrayList<String> players;
	
	@Override
	public void onEnable()
	{
		i = 0;
		Iterator itr = Minecraft.getMinecraft().getNetHandler().getPlayerInfo().iterator();
		players = new ArrayList<String>();
		while(itr.hasNext())
			players.add(StringUtils.stripControlCodes(((NetworkPlayerInfo)itr.next()).getPlayerNameForReal()));
		players.sort(new Comparator<String>()
		{
			Random random = new Random();
			
			@Override
			public int compare(String o1, String o2)
			{
				return random.nextInt();
			}});
	}
	
	@Override
	public void onReceivedMessage(String message)
	{
		if(!getToggled() || message.startsWith("§c[§6Wurst§c]§f "))
			return;
		if(message.toLowerCase().contains("/help") || message.toLowerCase().contains("permission"))
		{
			Client.wurst.chat.message("§4§lERROR:§f This server doesn't have TPA.");
			setToggled(false);
		}else if(message.toLowerCase().contains("accepted") && message.toLowerCase().contains("request") || message.toLowerCase().contains("akzeptiert") && message.toLowerCase().contains("anfrage"))
		{
			Client.wurst.chat.message("Someone accepted your TPA request. Stopping.");
			setToggled(false);
		}
	}
	
	@Override
	public void onUpdate()
	{
		if(getToggled())
		{
			updateMS();
			if(hasTimePassedS(speed))
			{
				String name = players.get(i);
				if(!name.equals(Minecraft.getMinecraft().thePlayer.getName()))
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/tpa " + name);
				updateLastMS();
				i++;
				if(i == players.size())
					setToggled(false);
			}
		}
	}
}
