/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.BLOCKS,
	description = "Allows you to break blocks faster.\n"
		+ "Tip: This works with Nuker.",
	name = "FastBreak")
public class FastBreakMod extends Mod
{
	public static float speed = 2;
	
	@Override
	public void initSliders()
	{
		sliders.add(new BasicSlider("FastBreak speed", speed, 1, 5, 0.05,
			ValueDisplay.DECIMAL));
	}
	
	@Override
	public void updateSettings()
	{
		speed = (int)sliders.get(0).getValue();
	}
}
