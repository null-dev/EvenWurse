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
			Client.Wurst.chat.message("Features in this release of Wurst:");
			double wurstMods = Client.Wurst.moduleManager.activeModules.size();
			int hiddenMods = 0;
			for(Module module : Client.Wurst.moduleManager.activeModules)
				if(module.getCategory() == Category.HIDDEN || module.getCategory() == Category.WIP)
					hiddenMods++;
			Client.Wurst.chat.message(">" + (int)wurstMods + " mods (" + hiddenMods + " of them are hidden)");
			int wurstBinds = 0;
			for(int i = 0; i < Client.Wurst.moduleManager.activeModules.size(); i++)
				if(Client.Wurst.moduleManager.activeModules.get(i).getBind() != 0)
					wurstBinds++;
			Client.Wurst.chat.message(">" + wurstBinds + " keybinds in your current configuration");
			int wurstCommands = Client.Wurst.commandManager.activeCommands.size();
			Client.Wurst.chat.message(">" + wurstCommands + " commands");
			ArrayList<BasicSlider> wurstSliders = new ArrayList<BasicSlider>();
			for(Module module : Client.Wurst.moduleManager.activeModules)
				for(BasicSlider slider : module.getSliders())
					wurstSliders.add(slider);
			Client.Wurst.chat.message(">" + wurstSliders.size() + " values that can be changed through sliders");
		}
		else
			commandError();
	}
}
