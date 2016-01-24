/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package xyz.nulldev.ew.mods;

import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Module;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.RenderUtils;
import xyz.nulldev.mcpwrapper.bukkit.Location;
import xyz.nulldev.mcpwrapper.bukkit.MCPWorld;
import xyz.nulldev.mcpwrapper.bukkit.World;

import java.awt.*;

@Module.ModuleInfo(version = 1.01f)
@Info(category = Category.RENDER,
        description = "Draws a line to the world's spawn location.",
        name = "SpawnTracer")
public class SpawnTracerMod extends Mod implements RenderListener {
    //TODO Don't give seizures
    static int colorFromTime() {
        return Color.HSBtoRGB((float) (0.25 + 0.5 * Math.sin(System.currentTimeMillis())), 1f, 1f);
    }

    @Override
    public void onEnable() {
        WurstClient.INSTANCE.events.add(RenderListener.class, this);
    }

    @Override
    public void onRender() {
        World world = MCPWorld.getInstace();
        Location spawnLoc = world.getSpawnLocation();
        RenderUtils.tracerLine(spawnLoc.getBlockX(), spawnLoc.getBlockY(), spawnLoc.getBlockZ(),
                new Color(colorFromTime()));
    }

    @Override
    public void onDisable() {
        WurstClient.INSTANCE.events.remove(RenderListener.class, this);
    }
}
