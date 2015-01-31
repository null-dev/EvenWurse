/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class NameProtect extends Module
{
	public NameProtect()
	{
		super(
			"NameProtect",
			"Hides all player names.\n"
				+ "Some YouTubers like to censor out all names in their\n"
				+ "videos.",
				0,
				Category.RENDER);
	}
}
