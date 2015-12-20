/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import tk.wurst_client.utils.F;

public class ChatManager
{
	private boolean enabled = true;
	
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
	
	public void message(String message)
	{
		if(enabled)
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
				new ChatComponentText(F.f("<RED>[<GOLD>Wurst</GOLD>]</RED> ") + message));
	}
	
	public void info(String message)
	{
		message(F.f("<DARK-GRAY>[<GRAY><BOLD>INFO</BOLD></GRAY>]</DARK-GRAY> ") + message);
	}
	
	public void debug(String message)
	{
		message(F.f("<DARK-GRAY>[<GRAY><BOLD>DEBUG-INFO</BOLD></GRAY>]</DARK-GRAY> ") + message);
	}
	
	public void warning(String message)
	{
		message(F.f("<RED>[<GOLD><BOLD>WARNING</BOLD></GOLD>]</RED> ") + message);
	}
	
	public void error(String message)
	{
		message(F.f("<RED>[<DARK-RED><BOLD>ERROR</BOLD></DARK-RED>]</RED> ") + message);
	}
	
	public void success(String message)
	{
		message(F.f("<GREEN>[<DARK-GREEN><BOLD>SUCCESS</BOLD></DARK-GREEN>]</GREEN> ") + message);
	}
	
	public void failure(String message)
	{
		message(F.f("<RED>[<DARK-RED><BOLD>INFO</BOLD></DARK-RED>]</RED> ") + message);
	}
	
	public void cmd(String message)
	{
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
			new ChatComponentText(F.f("<RED>[<GOLD>Wurst</GOLD>]</RED> <BLACK><BOLD><GREEN>\\<CMD\\></GREEN></BOLD></BLACK> ")
				+ message));
	}
}
