package xyz.nulldev.mcpwrapper.bukkit;

import net.minecraft.client.Minecraft;
import xyz.nulldev.mcpwrapper.bukkit.block.Block;
import xyz.nulldev.mcpwrapper.bukkit.block.MCPBlock;
import xyz.nulldev.mcpwrapper.bukkit.entity.Entity;

/**
 * Project: EvenWurse
 * Created: 22/12/15
 * Author: nulldev
 */
public class MCPChunk implements Chunk {

    int x;
    int z;

    public MCPChunk(int x, int z) {
        this.x = x;
        this.z = z;
    }

    net.minecraft.world.chunk.Chunk asMCChunk() {
        return Minecraft.getMinecraft().theWorld.getChunkFromChunkCoords(x, z);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public World getWorld() {
        //TODO
        return null;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        //Magic!!!
        return new MCPBlock((getX() << 4) | (x & 0xF), y & 0xFF, (getZ() << 4) | (z & 0xF));
    }

    @Override
    public Entity[] getEntities() {
        //TODO
        return new Entity[0];
    }

    @Override
    public boolean isLoaded() {
        return asMCChunk().isLoaded();
    }

    @Override
    public boolean load(boolean generate) {
        return getWorld().loadChunk(getX(), getZ(), generate);
    }

    @Override
    public boolean load() {
        return getWorld().loadChunk(getX(), getZ(), true);
    }

    @Override
    public boolean unload(boolean save, boolean safe) {
        return getWorld().unloadChunk(getX(), getZ(), save, safe);
    }

    @Override
    public boolean unload(boolean save) {
        return getWorld().unloadChunk(getX(), getZ(), save);
    }

    @Override
    public boolean unload() {
        return getWorld().unloadChunk(getX(), getZ());
    }
}
