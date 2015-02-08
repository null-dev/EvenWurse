/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command.commands;

import java.util.ArrayList;

import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Features extends Command
{
	private static String[] commandHelp =
	{
		"Counts the features in this release of Wurst.",
		".features"
	};

	public Features()
	{
		super("features", commandHelp);
	}

	@Override
	public void onEnable(String input, String[] args)
	{
		if(args == null)
		{
			Client.wurst.chat.message("Features in this release of Wurst:");
			double wurstMods = Client.wurst.moduleManager.activeModules.size();
			int hiddenMods = 0;
			for(Module module : Client.wurst.moduleManager.activeModules)
				if(module.getCategory() == Category.HIDDEN || module.getCategory() == Category.WIP)
					hiddenMods++;
			Client.wurst.chat.message(">" + (int)wurstMods + " mods (" + hiddenMods + " of them are hidden)");
			int wurstBinds = 0;
			for(int i = 0; i < Client.wurst.moduleManager.activeModules.size(); i++)
				if(Client.wurst.moduleManager.activeModules.get(i).getBind() != 0)
					wurstBinds++;
			Client.wurst.chat.message(">" + wurstBinds + " keybinds in your current configuration");
			int wurstCommands = Client.wurst.commandManager.activeCommands.size();
			Client.wurst.chat.message(">" + wurstCommands + " commands");
			ArrayList<BasicSlider> wurstSliders = new ArrayList<BasicSlider>();
			for(Module module : Client.wurst.moduleManager.activeModules)
				for(BasicSlider slider : module.getSliders())
					wurstSliders.add(slider);
			Client.wurst.chat.message(">" + wurstSliders.size() + " values that can be changed through sliders");
		}
		else
			commandError();
	}
}
