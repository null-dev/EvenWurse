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
import net.minecraft.util.MovingObjectPosition;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.LeftClickListener;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.settings.ModeSetting;
import tk.wurst_client.navigator.settings.SliderSetting;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.RenderUtils;

import java.util.HashSet;
import java.util.LinkedList;

@Info(category = Category.BLOCKS,
        description = "Destroys blocks around you.\n" + "Use .nuker mode <mode> to change the mode.", name = "Nuker")
public class NukerMod extends Mod implements LeftClickListener, RenderListener, UpdateListener {
    public static int id = 0;
    private static Block currentBlock;
    public float normalRange = 5F;
    public float yesCheatRange = 4.25F;
    private float realRange;
    private float currentDamage;
    private EnumFacing side = EnumFacing.UP;
    private byte blockHitDelay = 0;
    private BlockPos pos;
    private boolean shouldRenderESP;
    private int oldSlot = -1;
    private int mode = 0;
    private String[] modes = new String[]{"Normal", "ID", "Flat", "Smash"};

    @Override
    public String getRenderName() {
        switch (mode) {
            case 0:
                return "Nuker";
            case 1:
                return "IDNuker [" + id + "]";
            default:
                return modes[mode] + "Nuker";
        }
    }

    @Override
    public void initSettings() {
        settings.add(new SliderSetting("Range", normalRange, 1, 6, 0.05, ValueDisplay.DECIMAL));
        settings.add(new ModeSetting("Mode", modes, mode) {
            @Override
            public void update() {
                mode = getSelected();
            }
        });
    }

    @Override
    public void updateSliders() {
        normalRange = (float) ((SliderSetting) settings.get(0)).getValue();
        yesCheatRange = Math.min(normalRange, 4.25F);
    }

    @Override
    public void onEnable() {
        WurstClient.INSTANCE.mods.disableModsByClass(NukerLegitMod.class, SpeedNukerMod.class, TunnellerMod.class);
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
        if (WurstClient.INSTANCE.mods.getModByClass(YesCheatMod.class).isActive()) {
            realRange = yesCheatRange;
        } else {
            realRange = normalRange;
        }
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
        BlockUtils.faceBlockPacket(pos);
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
                if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode &&
                        !WurstClient.INSTANCE.mods.getModByClass(YesCheatMod.class).isActive()) {
                    nukeAll();
                } else {
                    shouldRenderESP = true;
                    Minecraft.getMinecraft().thePlayer.swingItem();
                    Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(pos, side);
                }
                return;
            }
        }
        if (WurstClient.INSTANCE.mods.getModByClass(AutoToolMod.class).isActive()) AutoToolMod.setSlot(pos);
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        shouldRenderESP = true;
        BlockUtils.faceBlockPacket(pos);
        currentDamage += currentBlock
                .getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld,
                        pos) * (WurstClient.INSTANCE.mods.getModByClass(FastBreakMod.class).isActive() &&
                WurstClient.INSTANCE.options.fastbreakMode == 0 ?
                (WurstClient.INSTANCE.mods.getModByClass(FastBreakMod.class)).speed : 1);
        Minecraft.getMinecraft().theWorld.sendBlockBreakProgress(Minecraft.getMinecraft().thePlayer.getEntityId(), pos,
                (int) (currentDamage * 10.0F) - 1);
        if (currentDamage >= 1) {
            Minecraft.getMinecraft().thePlayer.sendQueue
                    .addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, side));
            Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(pos, side);
            blockHitDelay = (byte) 4;
            currentDamage = 0;
        } else if (WurstClient.INSTANCE.mods.getModByClass(FastBreakMod.class).isActive() &&
                WurstClient.INSTANCE.options.fastbreakMode == 1) {
            Minecraft.getMinecraft().thePlayer.sendQueue
                    .addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, side));
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
        id = 0;
        WurstClient.INSTANCE.files.saveOptions();
    }

    @Override
    public void onLeftClick() {
        if (Minecraft.getMinecraft().objectMouseOver == null ||
                Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null) {
            return;
        }
        if (mode == 1 &&
                Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos())
                        .getBlock().getMaterial() != Material.air) {
            id = Block.getIdFromBlock(Minecraft.getMinecraft().theWorld
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
            if (BlockUtils.getPlayerBlockDistance(currentPos) > realRange) continue;
            int currentID =
                    Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(currentPos).getBlock());
            if (currentID != 0) {
                switch (mode) {
                    case 1:
                        if (currentID == id) return currentPos;
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
            if (!WurstClient.INSTANCE.mods.getModByClass(YesCheatMod.class).isActive() ||
                    !Minecraft.getMinecraft().theWorld.getBlockState(currentPos).getBlock().getMaterial()
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

    private void nukeAll() {
        for (int y = (int) realRange; y >= (mode == 2 ? 0 : -realRange); y--) {
            for (int x = (int) realRange; x >= -realRange - 1; x--) {
                for (int z = (int) realRange; z >= -realRange; z--) {
                    int posX = (int) (Math.floor(Minecraft.getMinecraft().thePlayer.posX) + x);
                    int posY = (int) (Math.floor(Minecraft.getMinecraft().thePlayer.posY) + y);
                    int posZ = (int) (Math.floor(Minecraft.getMinecraft().thePlayer.posZ) + z);
                    BlockPos blockPos = new BlockPos(posX, posY, posZ);
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
                    float xDiff = (float) (Minecraft.getMinecraft().thePlayer.posX - posX);
                    float yDiff = (float) (Minecraft.getMinecraft().thePlayer.posY - posY);
                    float zDiff = (float) (Minecraft.getMinecraft().thePlayer.posZ - posZ);
                    float currentDistance = BlockUtils.getBlockDistance(xDiff, yDiff, zDiff);
                    MovingObjectPosition fakeObjectMouseOver = Minecraft.getMinecraft().objectMouseOver;
                    if (fakeObjectMouseOver == null) return;
                    fakeObjectMouseOver.setBlockPos(blockPos);
                    if (Block.getIdFromBlock(block) != 0 && posY >= 0 && currentDistance <= realRange) {
                        if (mode == 1 && Block.getIdFromBlock(block) != id) continue;
                        if (mode == 3 && block.getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer,
                                Minecraft.getMinecraft().theWorld, blockPos) < 1) {
                            continue;
                        }
                        side = fakeObjectMouseOver.sideHit;
                        shouldRenderESP = true;
                        BlockUtils.faceBlockPacket(pos);
                        Minecraft.getMinecraft().thePlayer.sendQueue
                                .addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockPos, side));
                        block.onBlockDestroyedByPlayer(Minecraft.getMinecraft().theWorld, blockPos,
                                Minecraft.getMinecraft().theWorld.getBlockState(blockPos));
                    }
                }
            }
        }
    }
}
