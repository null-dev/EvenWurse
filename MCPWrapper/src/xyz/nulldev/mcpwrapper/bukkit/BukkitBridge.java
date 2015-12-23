package xyz.nulldev.mcpwrapper.bukkit;

/**
 * Project: EvenWurse
 * Created: 20/12/15
 * Author: nulldev
 */

import net.minecraft.util.BlockPos;
import xyz.nulldev.mcpwrapper.bukkit.block.Block;
import xyz.nulldev.mcpwrapper.util.BlockUtils;

/**
 * Converts between Bukkit and MCPWrapper classes
 */
public class BukkitBridge {
    public static BlockPos fromBukkit(Location location) {
        return new BlockPos(location.getX(), location.getY(), location.getZ());
    }

    public static Location toBukkit(BlockPos pos) {
        return new Location(pos.getX(), pos.getY(), pos.getZ());
    }

    public static net.minecraft.block.Block fromBukkit(Block block) {
        return BlockUtils.fromPos(BukkitBridge.fromBukkit(block.getLocation())).getBlock();
    }
}
