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
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.Mod.Category;

public class Features extends Command
{
	public Features()
	{
		super("features",
			"Counts the features in this release of Wurst.",
			"§o.features§r");
	}
	
	@Override
	public void onEnable(String input, String[] args)
	{
		if(args.length == 0)
		{
			Client.wurst.chat.message("Features in this release of Wurst:");
			int mods = Client.wurst.modManager.countMods();
			int hiddenMods = 0;
			for(Mod mod : Client.wurst.modManager.getAllMods())
				if(mod.getCategory() == Category.HIDDEN
					|| mod.getCategory() == Category.WIP)
					hiddenMods++;
			Client.wurst.chat.message(">" + (mods - hiddenMods) + " mods (+"
				+ hiddenMods + " hidden mods)");
			int commands =
				Client.wurst.commandManager.activeCommands.size();
			Client.wurst.chat.message(">" + commands + " commands");
			Client.wurst.chat.message(">" + Client.wurst.keybinds.size()
				+ " keybinds in your current configuration");
			ArrayList<BasicSlider> sliders = new ArrayList<BasicSlider>();
			for(Mod mod : Client.wurst.modManager.getAllMods())
				for(BasicSlider slider : mod.getSliders())
					sliders.add(slider);
			Client.wurst.chat.message(">" + sliders.size()
				+ " sliders in the Settings frame");
		}
		else
			commandError();
	}
}
