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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.utils.RenderUtils;

import java.util.ArrayList;

@Info(category = Category.RENDER,
        description = "Allows you to see chests through walls.\n" + "Tip: This works with the piston crates on HiveMC.",
        name = "ChestESP")
public class ChestEspMod extends Mod implements UpdateListener, RenderListener {
    private static final int RANGE = 50;
    private static final int MAX_CHESTS = 1000;
    public boolean shouldInform = true;
    private ArrayList<BlockPos> matchingBlocks = new ArrayList<>();

    @Override
    public NavigatorItem[] getSeeAlso() {
        WurstClient wurst = WurstClient.INSTANCE;
        return new NavigatorItem[]{wurst.mods.getModByClass(ItemEspMod.class),
                wurst.mods.getModByClass(SearchMod.class), wurst.mods.getModByClass(XRayMod.class)};
    }

    @Override
    public void onEnable() {
        shouldInform = true;
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
        WurstClient.INSTANCE.events.add(RenderListener.class, this);
    }

    @Override
    public void onRender() {
        int i = 0;
        for (Object o : Minecraft.getMinecraft().theWorld.loadedTileEntityList) {
            if (i >= MAX_CHESTS) break;
            if (o instanceof TileEntityChest) {
                i++;
                RenderUtils.blockESPBox(((TileEntityChest) o).getPos());
            } else if (o instanceof TileEntityEnderChest) {
                i++;
                RenderUtils.blockESPBox(((TileEntityEnderChest) o).getPos());
            }
        }
        for (Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (i >= MAX_CHESTS) break;
            if (o instanceof EntityMinecartChest) {
                i++;
                RenderUtils.blockESPBox(((EntityMinecartChest) o).getPosition());
            }
        }
        for (BlockPos blockPos : matchingBlocks) {
            if (i >= MAX_CHESTS) break;
            i++;
            RenderUtils.blockESPBox(blockPos);
        }
        if (i >= MAX_CHESTS && shouldInform) {
            WurstClient.INSTANCE.chat.warning(getName() + " found �lA LOT�r of chests.");
            WurstClient.INSTANCE.chat.message("To prevent lag, it will only show the first " + MAX_CHESTS + " chests.");
            shouldInform = false;
        } else if (i < MAX_CHESTS) shouldInform = true;
    }

    @Override
    public void onUpdate() {
        updateMS();
        if (hasTimePassedM(3000)) {
            matchingBlocks.clear();
            for (int y = RANGE; y >= -RANGE; y--) {
                for (int x = RANGE; x >= -RANGE; x--) {
                    for (int z = RANGE; z >= -RANGE; z--) {
                        int posX = (int) (Minecraft.getMinecraft().thePlayer.posX + x);
                        int posY = (int) (Minecraft.getMinecraft().thePlayer.posY + y);
                        int posZ = (int) (Minecraft.getMinecraft().thePlayer.posZ + z);
                        BlockPos pos = new BlockPos(posX, posY, posZ);
                        IBlockState state = Minecraft.getMinecraft().theWorld.getBlockState(pos);
                        Block block = state.getBlock();
                        int metadata = block.getMetaFromState(state);
                        if (Block.getIdFromBlock(block) == 33 && (metadata == 6 || metadata == 7 || metadata == 15)) {
                            matchingBlocks.add(pos);
                        }
                    }
                }
            }
            updateLastMS();
        }
    }

    @Override
    public void onDisable() {
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
        WurstClient.INSTANCE.events.remove(RenderListener.class, this);
    }
}
