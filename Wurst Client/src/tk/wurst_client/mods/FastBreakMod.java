/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import tk.wurst_client.WurstClient;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.settings.SliderSetting;

@Info(category = Category.BLOCKS,
        description = "Allows you to break blocks faster.\n" + "Tip: This works with Nuker.",
        name = "FastBreak")
public class FastBreakMod extends Mod {
    public float speed = 2;

    @Override
    public NavigatorItem[] getSeeAlso() {
        WurstClient wurst = WurstClient.INSTANCE;
        return new NavigatorItem[]{wurst.mods.getModByClass(FastPlaceMod.class),
                wurst.mods.getModByClass(AutoMineMod.class), wurst.mods.getModByClass(NukerMod.class)};
    }

    @Override
    public void initSettings() {
        settings.add(new SliderSetting("Speed", speed, 1, 5, 0.05, ValueDisplay.DECIMAL));
    }

    @Override
    public void updateSliders() {
        speed = (int) ((SliderSetting) settings.get(0)).getValue();
    }
}
