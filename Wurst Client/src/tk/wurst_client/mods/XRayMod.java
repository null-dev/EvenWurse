/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

import java.util.ArrayList;

@Info(category = Category.RENDER,
        description = "Allows you to see ores through walls.",
        name = "X-Ray")
public class XRayMod extends Mod {
    public static ArrayList<Block> xrayBlocks = new ArrayList<>();

    @Override
    public String getRenderName() {
        return "X-Wurst";
    }

    @Override
    public void onToggle() {
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }
}
