package xyz.nulldev.mcpwrapper.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class BlockUtils {
    public static int getID(BlockPos pos) {
        return Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock());
    }

    public static Block getBlockFromId(int id) {
        return Block.getBlockById(id);
    }

    public static BlockPos getRelative(BlockPos pos, int x, int y, int z) {
        return pos.add(x, y, z);
    }

    public static IBlockState fromPos(BlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos);
    }

    public static void setBlockId(BlockPos pos, int id) {
        Minecraft.getMinecraft().theWorld.setBlockState(pos, BlockUtils.getBlockFromId(id).getDefaultState());
    }
}
