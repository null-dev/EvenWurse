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

public class Jesus extends Module
{
	public Jesus()
	{
		super(
			"Jesus",
			"Allows you to walk on water.\n"
				+ "The real Jesus used this hack ~2000 years ago.\n"
				+ "The Christians might get mad at me for this joke, but\n"
				+ "who cares?",
			Category.MOVEMENT);
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class)
			.getToggled())
		{
			noCheatMessage();
			setToggled(false);
			return;
		}
	}
}
