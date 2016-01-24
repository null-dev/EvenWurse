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
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.LeftClickListener;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.RenderUtils;

import java.util.HashSet;
import java.util.LinkedList;

@Info(category = Category.BLOCKS,
        description = "Slower Nuker that bypasses any cheat prevention\n" +
                "PlugIn. Not required on most NoCheat+ servers!",
        name = "NukerLegit")
public class NukerLegitMod extends Mod implements LeftClickListener, RenderListener, UpdateListener {
    private static Block currentBlock;
    private float currentDamage;
    private EnumFacing side = EnumFacing.UP;
    private byte blockHitDelay = 0;
    private BlockPos pos;
    private boolean shouldRenderESP;
    private int oldSlot = -1;

    @Override
    public String getRenderName() {
        return WurstClient.INSTANCE.mods.getModByClass(NukerMod.class).getRenderName() + "Legit";
    }

    @Override
    public void onEnable() {
        WurstClient.INSTANCE.mods.disableModsByClass(NukerMod.class, SpeedNukerMod.class, TunnellerMod.class);
        WurstClient.INSTANCE.events.add(LeftClickListener.class, this);
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
        WurstClient.INSTANCE.events.add(RenderListener.class, this);
    }

    @Override
    public void onRender() {
        if (blockHitDelay == 0 && shouldRenderESP) {
            if (!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode && currentBlock
                    .getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer,
                            Minecraft.getMinecraft().theWorld, pos) < 1) {
                RenderUtils.nukerBox(pos, currentDamage);
            } else {
                RenderUtils.nukerBox(pos, 1);
            }
        }
    }

    @Override
    public void onUpdate() {
        shouldRenderESP = false;
        BlockPos newPos = find();
        if (newPos == null) {
            if (oldSlot != -1) {
                Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
                oldSlot = -1;
            }
            return;
        }
        if (pos == null || !pos.equals(newPos)) currentDamage = 0;
        pos = newPos;
        currentBlock = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        if (blockHitDelay > 0) {
            blockHitDelay--;
            return;
        }
        BlockUtils.faceBlockClient(pos);
        if (currentDamage == 0) {
            Minecraft.getMinecraft().thePlayer.sendQueue
                    .addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, side));
            if (WurstClient.INSTANCE.mods.getModByClass(AutoToolMod.class).isActive() && oldSlot == -1) {
                oldSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
            }
            if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode || currentBlock
                    .getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer,
                            Minecraft.getMinecraft().theWorld, pos) >= 1) {
                currentDamage = 0;
                shouldRenderESP = true;
                Minecraft.getMinecraft().thePlayer.swingItem();
                Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(pos, side);
                blockHitDelay = (byte) 4;
                return;
            }
        }
        if (WurstClient.INSTANCE.mods.getModByClass(AutoToolMod.class).isActive()) AutoToolMod.setSlot(pos);
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        shouldRenderESP = true;
        currentDamage += currentBlock
                .getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld,
                        pos);
        Minecraft.getMinecraft().theWorld.sendBlockBreakProgress(Minecraft.getMinecraft().thePlayer.getEntityId(), pos,
                (int) (currentDamage * 10.0F) - 1);
        if (currentDamage >= 1) {
            Minecraft.getMinecraft().thePlayer.sendQueue
                    .addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, side));
            Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(pos, side);
            blockHitDelay = (byte) 4;
            currentDamage = 0;
        }
    }

    @Override
    public void onDisable() {
        WurstClient.INSTANCE.events.remove(LeftClickListener.class, this);
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
        WurstClient.INSTANCE.events.remove(RenderListener.class, this);
        if (oldSlot != -1) {
            Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
            oldSlot = -1;
        }
        currentDamage = 0;
        shouldRenderESP = false;
        NukerMod.id = 0;
        WurstClient.INSTANCE.files.saveOptions();
    }

    @Override
    public void onLeftClick() {
        if (Minecraft.getMinecraft().objectMouseOver == null ||
                Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null) {
            return;
        }
        if (WurstClient.INSTANCE.mods.getModByClass(NukerMod.class).getMode() == 1 &&
                Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos())
                        .getBlock().getMaterial() != Material.air) {
            NukerMod.id = Block.getIdFromBlock(Minecraft.getMinecraft().theWorld
                    .getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock());
            WurstClient.INSTANCE.files.saveOptions();
        }
    }

    private BlockPos find() {
        LinkedList<BlockPos> queue = new LinkedList<>();
        HashSet<BlockPos> alreadyProcessed = new HashSet<>();
        queue.add(new BlockPos(Minecraft.getMinecraft().thePlayer));
        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            if (alreadyProcessed.contains(currentPos)) continue;
            alreadyProcessed.add(currentPos);
            if (BlockUtils.getPlayerBlockDistance(currentPos) >
                    WurstClient.INSTANCE.mods.getModByClass(NukerMod.class).yesCheatRange) {
                continue;
            }
            int currentID =
                    Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(currentPos).getBlock());
            if (currentID != 0) {
                switch (WurstClient.INSTANCE.mods.getModByClass(NukerMod.class).getMode()) {
                    case 1:
                        if (currentID == NukerMod.id) return currentPos;
                        break;
                    case 2:
                        if (currentPos.getY() >= Minecraft.getMinecraft().thePlayer.posY) return currentPos;
                        break;
                    case 3:
                        if (Minecraft.getMinecraft().theWorld.getBlockState(currentPos).getBlock()
                                .getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer,
                                        Minecraft.getMinecraft().theWorld, currentPos) >= 1) {
                            return currentPos;
                        }
                        break;
                    default:
                        return currentPos;
                }
            }
            if (!Minecraft.getMinecraft().theWorld.getBlockState(currentPos).getBlock().getMaterial()
                    .blocksMovement()) {
                queue.add(currentPos.add(0, 0, -1));// north
                queue.add(currentPos.add(0, 0, 1));// south
                queue.add(currentPos.add(-1, 0, 0));// west
                queue.add(currentPos.add(1, 0, 0));// east
                queue.add(currentPos.add(0, -1, 0));// down
                queue.add(currentPos.add(0, 1, 0));// up
            }
        }
        return null;

    }
}
