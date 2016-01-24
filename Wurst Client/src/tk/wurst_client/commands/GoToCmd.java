/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import tk.wurst_client.WurstClient;
import tk.wurst_client.ai.PathFinder;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.mods.GoToCmdMod;

@Info(help = "Walks or flies you to a specific location.",
        name = "goto",
        syntax = {"<x> <y> <z>", "<entity>"})
public class GoToCmd extends Cmd {
    @Override
    public void execute(String[] args) throws Error {
        int[] pos = argsToPos(args);
        if (Math.abs(pos[0] - Minecraft.getMinecraft().thePlayer.posX) > 256 ||
                Math.abs(pos[2] - Minecraft.getMinecraft().thePlayer.posZ) > 256) {
            WurstClient.INSTANCE.chat.error("Goal is out of range!");
            WurstClient.INSTANCE.chat.message("Maximum range is 256 blocks.");
            return;
        }
        tk.wurst_client.mods.GoToCmdMod.setGoal(new BlockPos(pos[0], pos[1], pos[2]));
        Thread thread = new Thread(() -> {
            System.out.println("Finding path");
            long startTime = System.nanoTime();
            PathFinder pathFinder = new PathFinder(tk.wurst_client.mods.GoToCmdMod.getGoal());
            if (pathFinder.find()) {
                tk.wurst_client.mods.GoToCmdMod.setPath(pathFinder.formatPath());
                WurstClient.INSTANCE.mods.getModByClass(GoToCmdMod.class).setEnabled(true);
            } else {
                WurstClient.INSTANCE.chat.error("Could not find a path.");
            }
            System.out.println("Done after " + (System.nanoTime() - startTime) / 1e6 + "ms");
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
}
