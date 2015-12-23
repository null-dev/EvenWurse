package xyz.nulldev.mcpwrapper.bukkit.block;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import xyz.nulldev.mcpwrapper.bukkit.BukkitBridge;
import xyz.nulldev.mcpwrapper.bukkit.Chunk;
import xyz.nulldev.mcpwrapper.bukkit.Location;
import xyz.nulldev.mcpwrapper.bukkit.World;
import xyz.nulldev.mcpwrapper.util.BlockUtils;

/**
 * Project: EvenWurse
 * Created: 22/12/15
 * Author: nulldev
 */

/**
 * Very limited implementation of Bukkit's Block in MCP
 */
public class MCPBlock implements Block {

    Location location;

    public MCPBlock(int x, int y, int z) {
        this(new Location(x, y, z));
    }

    public MCPBlock(Location location) {
        this.location = location;
    }

    @Override
    public Block getRelative(int modX, int modY, int modZ) {
        BlockPos pos = BlockUtils.getRelative(BukkitBridge.fromBukkit(location), modX, modY, modZ);
        return new MCPBlock(BukkitBridge.toBukkit(pos));
    }

    @Override
    public Block getRelative(BlockFace face) {
        return getRelative(face.getModX(), face.getModY(), face.getModZ());
    }

    @Override
    public Block getRelative(BlockFace face, int distance) {
        return getRelative(face.getModX() * distance,
                face.getModY() * distance,
                face.getModZ() * distance);
    }

    @Override
    public int getTypeId() {
        return BlockUtils.getID(BukkitBridge.fromBukkit(location));
    }

    @Override
    public byte getLightLevel() {
        return (byte) BlockUtils.fromPos(BukkitBridge.fromBukkit(location)).getBlock().getLightValue();
    }

    @Override
    public World getWorld() {
        //TODO
        return null;
    }

    @Override
    public int getX() {
        return location.getBlockX();
    }

    @Override
    public int getY() {
        return location.getBlockY();
    }

    @Override
    public int getZ() {
        return location.getBlockZ();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Location getLocation(Location loc) {
        if(loc == null) return null;
        loc.setX(getX());
        loc.setY(getY());
        loc.setZ(getZ());
        return loc;
    }

    @Override
    public Chunk getChunk() {
        return null;
    }

    @Override
    public boolean setTypeId(int type) {
        BlockUtils.setBlockId(BukkitBridge.fromBukkit(location), type);
        return false;
    }

    @Override
    public BlockFace getFace(Block block) {
        Location first = block.getLocation();
        Location diff = first.subtract(location);
        int x = diff.getBlockX();
        int y = diff.getBlockY();
        int z = diff.getBlockZ();
        if(Math.abs(x) > 1 || Math.abs(y) > 1 || Math.abs(z) > 1) {
            throw null;
        }
        return BlockFace.fromMod(x, y, z);
    }

    @Override
    public Biome getBiome() {
        return getWorld().getBiome(location.getBlockX(), location.getBlockY());
    }

    @Override
    public void setBiome(Biome bio) {
        getWorld().setBiome(location.getBlockX(), location.getBlockY(), bio);
    }

    @Override
    public boolean isEmpty() {
        return Minecraft.getMinecraft().theWorld.isAirBlock(BukkitBridge.fromBukkit(location));
    }

    @Override
    public boolean isLiquid() {
        return BlockUtils.fromPos(BukkitBridge.fromBukkit(location)).getBlock().getMaterial().isLiquid();
    }

    @Override
    public double getTemperature() {
        return getWorld().getTemperature(location.getBlockX(), location.getBlockY());
    }

    @Override
    public double getHumidity() {
        return getWorld().getHumidity(location.getBlockX(), location.getBlockY());
    }

    @Override
    public boolean breakNaturally() {
        Minecraft.getMinecraft().theWorld.destroyBlock(BukkitBridge.fromBukkit(location), true);
        return false;
    }
}
