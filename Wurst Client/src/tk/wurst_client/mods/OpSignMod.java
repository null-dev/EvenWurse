/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import tk.wurst_client.WurstClient;
import tk.wurst_client.gui.mods.GuiOpSign;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.EXPLOITS,
	description = "Enable this mod, place a sign and click it to get OP.\n"
		+ "Can also be used to run any other command.\n"
		+ "Only works on servers running Minecraft 1.8 - 1.8.5 without Spigot!\n"
		+ "Type .sv to check the server version.",
	name = "OP-Sign")
public class OpSignMod extends Mod
{
	public String command;
	
	@Override
	public void onEnable()
	{
		Minecraft.getMinecraft().displayGuiScreen(
			new GuiOpSign(this, Minecraft.getMinecraft().currentScreen));
	}
	
	public void setCommand(String cmd)
	{
		command = cmd.replace("\"", "\\\\\"");
		WurstClient.INSTANCE.chat
			.message("Command set. Place & right click a sign to run it.");
	}
}
