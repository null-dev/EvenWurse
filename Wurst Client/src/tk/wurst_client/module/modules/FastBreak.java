/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class FastBreak extends Module
{
	public FastBreak()
	{
		super(
			"FastBreak",
			"Allows you to break blocks faster.\n"
				+ "Tip: This works with Nuker.",
			Category.BLOCKS);
	}
	
	public static float speed = 2;
	
	@Override
	public void initSliders()
	{
		moduleSliders.add(new BasicSlider("FastBreak speed", speed, 1, 5, 0.05,
			ValueDisplay.DECIMAL));
	}
	
	@Override
	public void updateSettings()
	{
		speed = (int)moduleSliders.get(0).getValue();
	}
}
