/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import tk.wurst_client.WurstClient;

@Mod.Info(category = Mod.Category.BLOCKS,
	description = "Allows you to reach specific blocks through walls.\n"
		+ "Use .ghosthand id <block id> or .ghosthand name <block name>\n"
		+ "to specify it.",
	name = "GhostHand")
public class GhostHandMod extends Mod
{
	@Override
	public String getRenderName()
	{
		return getName() + " [" + WurstClient.INSTANCE.options.ghostHandID
			+ "]";
	}
}
