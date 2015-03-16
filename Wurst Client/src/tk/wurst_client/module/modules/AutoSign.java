/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.util.IChatComponent;
import tk.wurst_client.module.Mod;
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;


@Info(category = Category.BLOCKS, description = "Instantly writes whatever text you want on every sign\n"
				+ "you place. Once activated, you can write normally on\n"
				+ "one sign to specify the text for all other signs.", name = "AutoSign")
public class AutoSign extends Mod
{
	public static IChatComponent[] signText;
	
	@Override
	public void onEnable()
	{
		signText = null;
	}
}
