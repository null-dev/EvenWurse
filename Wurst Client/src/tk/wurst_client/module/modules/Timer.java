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

import tk.wurst_client.module.Mod;
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;

@Info(category = Category.MOVEMENT, description = "Changes the speed of almost everything.\n"
				+ "Tip: Slow speeds make aiming easier and work well with\n"
				+ "NoCheat+.", name = "Timer")
public class Timer extends Mod
{
	public static float speed = 2.0F;// Minimum: 0.1F, maximum: 10.0F
	
	@Override
	public void initSliders()
	{
		sliders.add(new BasicSlider("Timer speed", speed, 0.1, 10, 0.1,
			ValueDisplay.DECIMAL));
	}
	
	@Override
	public void updateSettings()
	{
		speed = (float)sliders.get(0).getValue();
	}
}
