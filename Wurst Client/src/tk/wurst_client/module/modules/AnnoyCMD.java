/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class AnnoyCMD extends Module
{
	public AnnoyCMD()
	{
		super(
			"Annoy",
			"",
			0,
			Category.HIDDEN);
	}
	
	private static boolean toggled;
	private static String name;
	
	@Override
	public String getRenderName()
	{
		if(name != null)
			return "Annoying " + name;
		else
			return "Annoy";
	}
	
	public static void onToggledByCommand(String name)
	{
		if(name == null)
		{
			if(toggled)
				toggled = false;
			else
				Client.wurst.chat.message("\"Annoy\" is already turned off. Type \".annoy <name>\" to annoy someone.");
			return;
		}
		toggled = !toggled;
		AnnoyCMD.name = name;
	}
	
	@Override
	public void onEnable()
	{
		if(name == null)
		{
			Client.wurst.chat.message("\"Annoy\" is already turned off. Type \".annoy <name>\" to annoy someone.");
			toggled = false;
			return;
		}
		Client.wurst.chat.message("Now annoying " + name + ".");
		if(name.equals(Minecraft.getMinecraft().thePlayer.getName()))
			Client.wurst.chat.warning("Annoying yourself is a bad idea!");
	}
	
	@Override
	public void onUpdate()
	{
		if(getToggled() != toggled)
			setToggled(toggled);
	}
	
	@Override
	public void onReceivedMessage(String message)
	{
		if(!getToggled() || message.startsWith("§c[§6Wurst§c]§f "))
			return;
		if(message.startsWith("<" + name + ">") || message.contains(name + ">"))
		{
			String repeatMessage = message.substring(message.indexOf(">") + 1);
			Minecraft.getMinecraft().thePlayer.sendChatMessage(repeatMessage);
		}else if(message.contains("] " + name + ":") || message.contains("]" + name + ":"))
		{
			String repeatMessage = message.substring(message.indexOf(":") + 1);
			Minecraft.getMinecraft().thePlayer.sendChatMessage(repeatMessage);
		}
	}
	
	@Override
	public void onDisable()
	{
		if(name != null)
		{
			Client.wurst.chat.message("No longer annoying " + name + ".");
			name = null;
		}
		toggled = false;
	}
}
