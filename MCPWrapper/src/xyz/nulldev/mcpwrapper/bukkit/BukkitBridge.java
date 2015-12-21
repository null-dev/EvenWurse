package xyz.nulldev.mcpwrapper.bukkit;

/**
 * Project: EvenWurse
 * Created: 20/12/15
 * Author: nulldev
 */

import net.minecraft.util.BlockPos;

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
}
