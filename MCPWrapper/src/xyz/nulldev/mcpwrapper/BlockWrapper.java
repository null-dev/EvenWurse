package xyz.nulldev.mcpwrapper;

/**
 * Project: EvenWurse
 * Created: 20/12/15
 * Author: nulldev
 */

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

/**
 * Easier to use block implementation
 */
public class BlockWrapper {

    BlockPos pos;

    public BlockWrapper(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public int getId() {
        return Block.getIdFromBlock(Minecraft.getMinecraft().theWorld
                .getBlockState(pos).getBlock());
    }

    public BlockWrapper getRelative(int x, int y, int z) {
        return new BlockWrapper(pos.add(x, y, z));
    }

    public IBlockState getBlockState() {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos);
    }
}
