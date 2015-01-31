/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Panic extends Module
{
	public Panic()
	{
		super(
			"Panic",
			"Instantly turns off all enabled mods.\n"
				+ "Be careful with this!",
				0,
				Category.MISC);
	}

	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		for(int i = 0; i < Client.Wurst.moduleManager.activeModules.size(); i++)
			if(!Client.Wurst.moduleManager.activeModules.get(i).equals(this) && Client.Wurst.moduleManager.activeModules.get(i).getCategory() != Category.HIDDEN && Client.Wurst.moduleManager.activeModules.get(i).getToggled())
				Client.Wurst.moduleManager.activeModules.get(i).setToggled(false);
		setToggled(false);
	}
}
