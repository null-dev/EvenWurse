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
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.BuildUtils;
import tk.wurst_client.utils.RenderUtils;

import java.util.ArrayList;

@Info(category = Category.AUTOBUILD,
        description = "Automatically builds the selected template whenever\n" +
                "you place a block. Use the combo box below to select\n" + "a template.\n" +
                "This mod can bypass NoCheat+ while YesCheat+ is\n" + "enabled.",
        name = "AutoBuild")
public class AutoBuildMod extends Mod implements UpdateListener, RenderListener {
    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<int[][]> templates = new ArrayList<>();
    private float speed = 5;
    private int blockIndex;
    private boolean shouldBuild;
    private float playerYaw;
    private MovingObjectPosition mouseOver;

    @Override
    public String getRenderName() {
        return getName() + " [" + names.get(WurstClient.INSTANCE.options.autobuildMode) + "]";
    }

    @Override
    public void onEnable() {
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
        WurstClient.INSTANCE.events.add(RenderListener.class, this);
    }

    @Override
    public void onRender() {
        if (templates.get(WurstClient.INSTANCE.options.autobuildMode)[0].length == 4) {
            renderAdvanced();
        } else {
            renderSimple();
        }
    }

    @Override
    public void onUpdate() {
        if (templates.get(WurstClient.INSTANCE.options.autobuildMode)[0].length == 4) {
            buildAdvanced();
        } else {
            buildSimple();
        }
    }

    @Override
    public void onDisable() {
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
        WurstClient.INSTANCE.events.remove(RenderListener.class, this);
        shouldBuild = false;
    }

    private void renderAdvanced() {
        if (shouldBuild && blockIndex < templates.get(WurstClient.INSTANCE.options.autobuildMode).length &&
                blockIndex >= 0) {
            if (playerYaw > -45 && playerYaw <= 45) {// F: 0 South
                double renderX = BuildUtils.convertPosNext(1, mouseOver) + BuildUtils
                        .convertPosInAdvancedBuiling(1, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                        .convertPosInAdvancedBuiling(2, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                double renderZ = BuildUtils.convertPosNext(3, mouseOver) + BuildUtils
                        .convertPosInAdvancedBuiling(3, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
            } else if (playerYaw > 45 && playerYaw <= 135) {// F: 1 West
                double renderX = BuildUtils.convertPosNext(1, mouseOver) - BuildUtils
                        .convertPosInAdvancedBuiling(3, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                        .convertPosInAdvancedBuiling(2, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                double renderZ = BuildUtils.convertPosNext(3, mouseOver) + BuildUtils
                        .convertPosInAdvancedBuiling(1, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
            } else if (playerYaw > 135 || playerYaw <= -135) {// F: 2 North
                double renderX = BuildUtils.convertPosNext(1, mouseOver) - BuildUtils
                        .convertPosInAdvancedBuiling(1, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                        .convertPosInAdvancedBuiling(2, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                double renderZ = BuildUtils.convertPosNext(3, mouseOver) - BuildUtils
                        .convertPosInAdvancedBuiling(3, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
            } else if (playerYaw > -135 && playerYaw <= -45) {// F: 3 East
                double renderX = BuildUtils.convertPosNext(1, mouseOver) + BuildUtils
                        .convertPosInAdvancedBuiling(3, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                        .convertPosInAdvancedBuiling(2, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                double renderZ = BuildUtils.convertPosNext(3, mouseOver) - BuildUtils
                        .convertPosInAdvancedBuiling(1, blockIndex,
                                templates.get(WurstClient.INSTANCE.options.autobuildMode));
                RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
            }
        }
        if (shouldBuild && mouseOver != null) {
            double renderX = BuildUtils.convertPosNext(1, mouseOver);
            double renderY = BuildUtils.convertPosNext(2, mouseOver) + 1;
            double renderZ = BuildUtils.convertPosNext(3, mouseOver);
            RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
        }
        for (int i = 0; i < templates.get(WurstClient.INSTANCE.options.autobuildMode).length; i++) {
            if (shouldBuild && mouseOver != null) {
                if (playerYaw > -45 && playerYaw <= 45) {// F: 0 South
                    double renderX = BuildUtils.convertPosNext(1, mouseOver) + BuildUtils
                            .convertPosInAdvancedBuiling(1, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                            .convertPosInAdvancedBuiling(2, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    double renderZ = BuildUtils.convertPosNext(3, mouseOver) + BuildUtils
                            .convertPosInAdvancedBuiling(3, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
                } else if (playerYaw > 45 && playerYaw <= 135) {// F: 1 West
                    double renderX = BuildUtils.convertPosNext(1, mouseOver) - BuildUtils
                            .convertPosInAdvancedBuiling(3, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                            .convertPosInAdvancedBuiling(2, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    double renderZ = BuildUtils.convertPosNext(3, mouseOver) + BuildUtils
                            .convertPosInAdvancedBuiling(1, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
                } else if (playerYaw > 135 || playerYaw <= -135) {// F: 2 North
                    double renderX = BuildUtils.convertPosNext(1, mouseOver) - BuildUtils
                            .convertPosInAdvancedBuiling(1, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                            .convertPosInAdvancedBuiling(2, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    double renderZ = BuildUtils.convertPosNext(3, mouseOver) - BuildUtils
                            .convertPosInAdvancedBuiling(3, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
                } else if (playerYaw > -135 && playerYaw <= -45) {// F: 3 East
                    double renderX = BuildUtils.convertPosNext(1, mouseOver) + BuildUtils
                            .convertPosInAdvancedBuiling(3, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    double renderY = BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                            .convertPosInAdvancedBuiling(2, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    double renderZ = BuildUtils.convertPosNext(3, mouseOver) - BuildUtils
                            .convertPosInAdvancedBuiling(1, i,
                                    templates.get(WurstClient.INSTANCE.options.autobuildMode));
                    RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
                }
            }
        }
    }

    private void renderSimple() {
        if (shouldBuild && blockIndex < templates.get(WurstClient.INSTANCE.options.autobuildMode).length &&
                blockIndex >= 0) {
            if (playerYaw > -45 && playerYaw <= 45) {// F: 0 South
                double renderX = mouseOver.getBlockPos().getX() + BuildUtils
                        .convertPosInBuiling(1, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                double renderY = mouseOver.getBlockPos().getY() + BuildUtils
                        .convertPosInBuiling(2, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                double renderZ = mouseOver.getBlockPos().getZ() + BuildUtils
                        .convertPosInBuiling(3, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
            } else if (playerYaw > 45 && playerYaw <= 135) {// F: 1 West
                double renderX = mouseOver.getBlockPos().getX() - BuildUtils
                        .convertPosInBuiling(3, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                double renderY = mouseOver.getBlockPos().getY() + BuildUtils
                        .convertPosInBuiling(2, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                double renderZ = mouseOver.getBlockPos().getZ() + BuildUtils
                        .convertPosInBuiling(1, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
            } else if (playerYaw > 135 || playerYaw <= -135) {// F: 2 North
                double renderX = mouseOver.getBlockPos().getX() - BuildUtils
                        .convertPosInBuiling(1, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                double renderY = mouseOver.getBlockPos().getY() + BuildUtils
                        .convertPosInBuiling(2, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                double renderZ = mouseOver.getBlockPos().getZ() - BuildUtils
                        .convertPosInBuiling(3, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
            } else if (playerYaw > -135 && playerYaw <= -45) {// F: 3 East
                double renderX = mouseOver.getBlockPos().getX() + BuildUtils
                        .convertPosInBuiling(3, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                double renderY = mouseOver.getBlockPos().getY() + BuildUtils
                        .convertPosInBuiling(2, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                double renderZ = mouseOver.getBlockPos().getZ() - BuildUtils
                        .convertPosInBuiling(1, blockIndex, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                mouseOver);
                RenderUtils.blockESPBox(new BlockPos(renderX, renderY, renderZ));
            }
        }
        if (shouldBuild && mouseOver != null) {
            double renderX = BuildUtils.convertPosNext(1, mouseOver);
            double renderY = BuildUtils.convertPosNext(2, mouseOver) + 1;
            double renderZ = BuildUtils.convertPosNext(3, mouseOver);
            RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
        }
        for (int i = 0; i < templates.get(WurstClient.INSTANCE.options.autobuildMode).length; i++) {
            if (shouldBuild && mouseOver != null) {
                if (playerYaw > -45 && playerYaw <= 45) {// F: 0 South
                    double renderX = mouseOver.getBlockPos().getX() + BuildUtils
                            .convertPosInBuiling(1, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    double renderY = mouseOver.getBlockPos().getY() + BuildUtils
                            .convertPosInBuiling(2, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    double renderZ = mouseOver.getBlockPos().getZ() + BuildUtils
                            .convertPosInBuiling(3, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
                } else if (playerYaw > 45 && playerYaw <= 135) {// F: 1 West
                    double renderX = mouseOver.getBlockPos().getX() - BuildUtils
                            .convertPosInBuiling(3, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    double renderY = mouseOver.getBlockPos().getY() + BuildUtils
                            .convertPosInBuiling(2, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    double renderZ = mouseOver.getBlockPos().getZ() + BuildUtils
                            .convertPosInBuiling(1, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
                } else if (playerYaw > 135 || playerYaw <= -135) {// F: 2 North
                    double renderX = mouseOver.getBlockPos().getX() - BuildUtils
                            .convertPosInBuiling(1, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    double renderY = mouseOver.getBlockPos().getY() + BuildUtils
                            .convertPosInBuiling(2, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    double renderZ = mouseOver.getBlockPos().getZ() - BuildUtils
                            .convertPosInBuiling(3, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
                } else if (playerYaw > -135 && playerYaw <= -45) {// F: 3 East
                    double renderX = mouseOver.getBlockPos().getX() + BuildUtils
                            .convertPosInBuiling(3, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    double renderY = mouseOver.getBlockPos().getY() + BuildUtils
                            .convertPosInBuiling(2, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    double renderZ = mouseOver.getBlockPos().getZ() - BuildUtils
                            .convertPosInBuiling(1, i, templates.get(WurstClient.INSTANCE.options.autobuildMode),
                                    mouseOver);
                    RenderUtils.emptyBlockESPBox(new BlockPos(renderX, renderY, renderZ));
                }
            }
        }
    }

    private void buildAdvanced() {
        updateMS();
        if (!shouldBuild && (Minecraft.getMinecraft().rightClickDelayTimer == 4 ||
                WurstClient.INSTANCE.mods.getModByClass(FastPlaceMod.class).isActive()) &&
                Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed &&
                Minecraft.getMinecraft().objectMouseOver != null &&
                Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null &&
                Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos())
                        .getBlock().getMaterial() != Material.air) {
            if (WurstClient.INSTANCE.mods.getModByClass(FastPlaceMod.class).isActive()) {
                speed = 1000000000;
            } else {
                speed = 5;
            }
            if (WurstClient.INSTANCE.mods.getModByClass(YesCheatMod.class).isActive()) {
                blockIndex = 0;
                shouldBuild = true;
                mouseOver = Minecraft.getMinecraft().objectMouseOver;
                playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
                while (playerYaw > 180) playerYaw -= 360;
                while (playerYaw < -180) playerYaw += 360;
            } else {
                BuildUtils.advancedBuild(templates.get(WurstClient.INSTANCE.options.autobuildMode));
            }
            updateLastMS();
            return;
        }
        if (shouldBuild) {
            if ((hasTimePassedS(speed) || WurstClient.INSTANCE.mods.getModByClass(FastPlaceMod.class).isActive()) &&
                    blockIndex < templates.get(WurstClient.INSTANCE.options.autobuildMode).length) {
                BuildUtils.advancedBuildNext(templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver,
                        playerYaw, blockIndex);
                if (playerYaw > -45 && playerYaw <= 45) {
                    try {
                        if (Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
                                BuildUtils.convertPosNext(1, mouseOver) + BuildUtils
                                        .convertPosInAdvancedBuiling(1, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode)),
                                BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                                        .convertPosInAdvancedBuiling(2, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode)),
                                BuildUtils.convertPosNext(3, mouseOver) + BuildUtils
                                        .convertPosInAdvancedBuiling(3, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode))))
                                .getBlock()) != 0) {
                            blockIndex += 1;
                        }
                    } catch (NullPointerException ignored) {
                    }// If the current item is null.
                } else if (playerYaw > 45 && playerYaw <= 135) {
                    try {
                        if (Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
                                BuildUtils.convertPosNext(1, mouseOver) - BuildUtils
                                        .convertPosInAdvancedBuiling(3, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode)),
                                BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                                        .convertPosInAdvancedBuiling(2, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode)),
                                BuildUtils.convertPosNext(3, mouseOver) + BuildUtils
                                        .convertPosInAdvancedBuiling(1, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode))))
                                .getBlock()) != 0) {
                            blockIndex += 1;
                        }
                    } catch (NullPointerException ignored) {
                    }// If the current item is null.
                } else if (playerYaw > 135 || playerYaw <= -135) {
                    try {
                        if (Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
                                BuildUtils.convertPosNext(1, mouseOver) - BuildUtils
                                        .convertPosInAdvancedBuiling(1, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode)),
                                BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                                        .convertPosInAdvancedBuiling(2, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode)),
                                BuildUtils.convertPosNext(3, mouseOver) - BuildUtils
                                        .convertPosInAdvancedBuiling(3, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode))))
                                .getBlock()) != 0) {
                            blockIndex += 1;
                        }
                    } catch (NullPointerException ignored) {
                    }// If the current item is null.
                } else if (playerYaw > -135 && playerYaw <= -45) {
                    try {
                        if (Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
                                BuildUtils.convertPosNext(1, mouseOver) + BuildUtils
                                        .convertPosInAdvancedBuiling(3, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode)),
                                BuildUtils.convertPosNext(2, mouseOver) + BuildUtils
                                        .convertPosInAdvancedBuiling(2, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode)),
                                BuildUtils.convertPosNext(3, mouseOver) - BuildUtils
                                        .convertPosInAdvancedBuiling(1, blockIndex,
                                                templates.get(WurstClient.INSTANCE.options.autobuildMode))))
                                .getBlock()) != 0) {
                            blockIndex += 1;
                        }
                    } catch (NullPointerException ignored) {
                    }// If the current item is null.
                }
                updateLastMS();
            } else if (blockIndex == templates.get(WurstClient.INSTANCE.options.autobuildMode).length) {
                shouldBuild = false;
            }
        }
    }

    private void buildSimple() {
        updateMS();
        if (!shouldBuild && (Minecraft.getMinecraft().rightClickDelayTimer == 4 ||
                WurstClient.INSTANCE.mods.getModByClass(FastPlaceMod.class).isActive()) &&
                Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed &&
                Minecraft.getMinecraft().objectMouseOver != null &&
                Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null &&
                Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos())
                        .getBlock().getMaterial() != Material.air) {
            if (WurstClient.INSTANCE.mods.getModByClass(FastPlaceMod.class).isActive()) {
                speed = 1000000000;
            } else {
                speed = 5;
            }
            if (WurstClient.INSTANCE.mods.getModByClass(YesCheatMod.class).isActive()) {
                blockIndex = 0;
                shouldBuild = true;
                mouseOver = Minecraft.getMinecraft().objectMouseOver;
                playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
                while (playerYaw > 180) playerYaw -= 360;
                while (playerYaw < -180) playerYaw += 360;
            } else {
                BuildUtils.build(templates.get(WurstClient.INSTANCE.options.autobuildMode));
            }
            updateLastMS();
            return;
        }
        if (shouldBuild) {
            if ((hasTimePassedS(speed) || WurstClient.INSTANCE.mods.getModByClass(FastPlaceMod.class).isActive()) &&
                    blockIndex < templates.get(WurstClient.INSTANCE.options.autobuildMode).length) {
                BuildUtils.buildNext(templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver, playerYaw,
                        blockIndex);
                if (playerYaw > -45 && playerYaw <= 45) {
                    try {
                        if (Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
                                mouseOver.getBlockPos().getX() + BuildUtils.convertPosInBuiling(1, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver),
                                mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver),
                                mouseOver.getBlockPos().getZ() + BuildUtils.convertPosInBuiling(3, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver)))
                                .getBlock()) != 0) {
                            blockIndex += 1;
                        }
                    } catch (NullPointerException ignored) {
                    }// If the current item is null.
                } else if (playerYaw > 45 && playerYaw <= 135) {
                    try {
                        if (Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
                                mouseOver.getBlockPos().getX() - BuildUtils.convertPosInBuiling(3, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver),
                                mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver),
                                mouseOver.getBlockPos().getZ() + BuildUtils.convertPosInBuiling(1, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver)))
                                .getBlock()) != 0) {
                            blockIndex += 1;
                        }
                    } catch (NullPointerException ignored) {
                    }// If the current item is null.
                } else if (playerYaw > 135 || playerYaw <= -135) {
                    try {
                        if (Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
                                mouseOver.getBlockPos().getX() - BuildUtils.convertPosInBuiling(1, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver),
                                mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver),
                                mouseOver.getBlockPos().getZ() - BuildUtils.convertPosInBuiling(3, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver)))
                                .getBlock()) != 0) {
                            blockIndex += 1;
                        }
                    } catch (NullPointerException ignored) {
                    }// If the current item is null.
                } else if (playerYaw > -135 && playerYaw <= -45) {
                    try {
                        if (Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(
                                mouseOver.getBlockPos().getX() + BuildUtils.convertPosInBuiling(3, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver),
                                mouseOver.getBlockPos().getY() + BuildUtils.convertPosInBuiling(2, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver),
                                mouseOver.getBlockPos().getZ() - BuildUtils.convertPosInBuiling(1, blockIndex,
                                        templates.get(WurstClient.INSTANCE.options.autobuildMode), mouseOver)))
                                .getBlock()) != 0) {
                            blockIndex += 1;
                        }
                    } catch (NullPointerException ignored) {
                    }// If the current item is null.
                }
                updateLastMS();
            } else if (blockIndex == templates.get(WurstClient.INSTANCE.options.autobuildMode).length) {
                shouldBuild = false;
            }
        }
    }
}
