/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import tk.wurst_client.WurstClient;
import tk.wurst_client.ai.PathUtils;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.BlockUtils;

import java.util.ArrayList;

@Info(category = Category.HIDDEN,
        description = "",
        name = "GoTo")
public class GoToCmdMod extends Mod implements UpdateListener {
    private static ArrayList<BlockPos> path;
    private static BlockPos goal;
    private int index;

    public static void setPath(ArrayList<BlockPos> path) {
        GoToCmdMod.path = path;
    }

    public static BlockPos getGoal() {
        return goal;
    }

    public static void setGoal(BlockPos goal) {
        GoToCmdMod.goal = goal;
    }

    @Override
    public String getRenderName() {
        if (goal != null) {
            return "Go to " + goal.getX() + " " + goal.getY() + " " + goal.getZ();
        } else {
            return "GoTo";
        }
    }

    @Override
    public void onEnable() {
        index = 0;
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        if (path == null || goal == null) {
            setEnabled(false);
            return;
        }
        BlockPos currentPos = new BlockPos(Minecraft.getMinecraft().thePlayer);
        BlockPos nextPos = path.get(index);
        float dist = BlockUtils.getPlayerBlockDistance(nextPos);
        float hDist = BlockUtils.getHorizontalPlayerBlockDistance(nextPos);
        double vDist = Math.abs(Minecraft.getMinecraft().thePlayer.posY - nextPos.getY());
        Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindBack.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindRight.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = false;
        Minecraft.getMinecraft().thePlayer.rotationPitch = 10;
        BlockUtils.faceBlockClientHorizontally(nextPos);

        if (hDist > 0.25) Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = true;
        if (vDist > 0.75) {
            if (PathUtils.isFlyable(currentPos)) {
                if (currentPos.getY() > nextPos.getY()) {
                    Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = true;
                } else {
                    Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = true;
                }
            } else if (PathUtils.isClimbable(currentPos) && currentPos.getY() < nextPos.getY()) {
                BlockPos[] neighbors =
                        new BlockPos[]{currentPos.add(0, 0, -1), currentPos.add(0, 0, 1), currentPos.add(1, 0, 0),
                                currentPos.add(-1, 0, 0)};
                for (BlockPos neigbor : neighbors) {
                    if (!PathUtils.isSolid(neigbor)) continue;
                    BlockUtils.faceBlockClientHorizontally(neigbor);
                    Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = true;
                    break;
                }
            }
        }

        if (dist < 1) index++;
        if (index >= path.size()) setEnabled(false);
    }

    @Override
    public void onDisable() {
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
        path = null;
        goal = null;
        Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindBack.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindRight.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
        Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = false;
    }
}
