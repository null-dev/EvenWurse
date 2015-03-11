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

public class HighJump extends Module
{
	public HighJump()
	{
		super(
			"HighJump",
			"Makes you jump six times higher.",
			Category.MOVEMENT);
	}
	
	public static double jumpHeight = 0.41999998688697815D * 6;
	
	@Override
	public void oldOnUpdate()
	{
		if(!getToggled())
			return;
		if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class)
			.getToggled())
		{
			noCheatMessage();
			setToggled(false);
		}
	}
}
