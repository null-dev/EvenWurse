package xyz.nulldev.mcpwrapper.bukkit;

import xyz.nulldev.mcpwrapper.bukkit.block.Block;
import xyz.nulldev.mcpwrapper.bukkit.entity.Entity;

/**
 * Represents a chunk of blocks
 */
public interface Chunk {

    /**
     * Gets the X-coordinate of this chunk
     *
     * @return X-coordinate
     */
    int getX();

    /**
     * Gets the Z-coordinate of this chunk
     *
     * @return Z-coordinate
     */
    int getZ();

    /**
     * Gets the world containing this chunk
     *
     * @return Parent World
     */
    World getWorld();

    /**
     * Gets a block from this chunk
     *
     * @param x 0-15
     * @param y 0-127
     * @param z 0-15
     * @return the Block
     */
    Block getBlock(int x, int y, int z);

    /**
     * Get a list of all entities in the chunk.
     *
     * @return The entities.
     */
    Entity[] getEntities();

    /**
     * Checks if the chunk is loaded.
     *
     * @return True if it is loaded.
     */
    boolean isLoaded();

    /**
     * Loads the chunk.
     *
     * @param generate Whether or not to generate a chunk if it doesn't
     *                 already exist
     * @return true if the chunk has loaded successfully, otherwise false
     */
    boolean load(boolean generate);

    /**
     * Loads the chunk.
     *
     * @return true if the chunk has loaded successfully, otherwise false
     */
    boolean load();

    /**
     * Unloads and optionally saves the Chunk
     *
     * @param save Controls whether the chunk is saved
     * @param safe Controls whether to unload the chunk when players are
     *             nearby
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    boolean unload(boolean save, boolean safe);

    /**
     * Unloads and optionally saves the Chunk
     *
     * @param save Controls whether the chunk is saved
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    boolean unload(boolean save);

    /**
     * Unloads and optionally saves the Chunk
     *
     * @return true if the chunk has unloaded successfully, otherwise false
     */
    boolean unload();
}
