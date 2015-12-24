package xyz.nulldev.mcpwrapper.bukkit;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import xyz.nulldev.mcpwrapper.bukkit.block.Biome;
import xyz.nulldev.mcpwrapper.bukkit.block.Block;
import xyz.nulldev.mcpwrapper.bukkit.block.MCPBlock;
import xyz.nulldev.mcpwrapper.bukkit.entity.Entity;
import xyz.nulldev.mcpwrapper.util.BlockUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Project: EvenWurse
 * Created: 22/12/15
 * Author: nulldev
 */
public class MCPWorld implements World {

    static MCPWorld INSTANCE = new MCPWorld();
    public static MCPWorld getInstace() {
        if(INSTANCE == null) INSTANCE = new MCPWorld();
        return INSTANCE;
    }

    //Internal chunk stuff
    net.minecraft.world.chunk.Chunk asMCChunk(Chunk chunk) {
        return asMCChunk(chunk.getX(), chunk.getZ());
    }
    net.minecraft.world.chunk.Chunk asMCChunk(int x, int z) {
        return Minecraft.getMinecraft().theWorld.getChunkFromChunkCoords(x, z);
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return new MCPBlock(x, y, z);
    }

    @Override
    public Block getBlockAt(Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public int getBlockTypeIdAt(int x, int y, int z) {
        return BlockUtils.getID(new BlockPos(x, y, z));
    }

    @Override
    public int getBlockTypeIdAt(Location location) {
        return getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        if (!isChunkLoaded(x >> 4, z >> 4)) {
            loadChunk(x >> 4, z >> 4);
        }
        //Iterate all blocks top down
        int highest = 0;
        for(int i = 255; i >= 0; i--) {
            if(!Minecraft.getMinecraft().theWorld.isAirBlock(new BlockPos(x, i, z))) {
                highest = i;
                break;
            }
        }
        return highest;
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public Block getHighestBlockAt(int x, int z) {
        return getBlockAt(x, getHighestBlockYAt(x, z), z);
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return new MCPChunk(x, z);
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    @Override
    public Chunk getChunkAt(Block block) {
        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        return chunk.isLoaded();
    }

    @Override
    public Chunk[] getLoadedChunks() {
        return new Chunk[0];
    }

    @Override
    public void loadChunk(Chunk chunk) {

    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        return new MCPChunk(x, z).isLoaded();
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        throw new NotImplementedException();
    }

    @Override
    public void loadChunk(int x, int z) {
        throw new NotImplementedException();
    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        throw new NotImplementedException();
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        throw new NotImplementedException();
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        throw new NotImplementedException();
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        throw new NotImplementedException();
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
        throw new NotImplementedException();
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        throw new NotImplementedException();
    }

    @Override
    public boolean unloadChunkRequest(int x, int z, boolean safe) {
        throw new NotImplementedException();
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        throw new NotImplementedException();
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        throw new NotImplementedException();
    }

    @Override
    public List<Entity> getEntities() {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        throw new NotImplementedException();
        //TODO
    }

    @Override
    public String getName() {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().getWorldName();
    }

    @Override
    public UUID getUID() {
        //FIXME Not sure if correct
        return UUID.fromString(Minecraft.getMinecraft().theWorld.getWorldInfo().getWorldName());
    }

    @Override
    public Location getSpawnLocation() {
        WorldInfo worldInfo = Minecraft.getMinecraft().theWorld.getWorldInfo();
        return new Location(worldInfo.getSpawnX(), worldInfo.getSpawnY(), worldInfo.getSpawnZ());
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        try {
            Minecraft.getMinecraft().theWorld.getWorldInfo().setSpawn(pos);
        } catch(Throwable t) {
            System.out.println("[MCPWrapper] Exception setting spawnpoint to: '" + BukkitBridge.toBukkit(pos).toString() + "'!");
            t.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public long getTime() {
        return Minecraft.getMinecraft().theWorld.getWorldTime();
    }

    @Override
    public void setTime(long time) {
        Minecraft.getMinecraft().theWorld.setWorldTime(time);
    }

    @Override
    public long getFullTime() {
        return Minecraft.getMinecraft().theWorld.getTotalWorldTime();
    }

    @Override
    public void setFullTime(long time) {
        //Misleading as the increment just sets the value
        Minecraft.getMinecraft().theWorld.getWorldInfo().incrementTotalWorldTime(time);
    }

    @Override
    public boolean hasStorm() {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public int getWeatherDuration() {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().getRainTime();
    }

    @Override
    public void setWeatherDuration(int duration) {
        Minecraft.getMinecraft().theWorld.getWorldInfo().setRainTime(duration);
    }

    @Override
    public boolean isThundering() {
        return Minecraft.getMinecraft().theWorld.isThundering();
    }

    @Override
    public void setThundering(boolean thundering) {
        Minecraft.getMinecraft().theWorld.getWorldInfo().setThundering(thundering);
    }

    @Override
    public int getThunderDuration() {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().getThunderTime();
    }

    @Override
    public void setThunderDuration(int duration) {
        Minecraft.getMinecraft().theWorld.getWorldInfo().setThunderTime(duration);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public boolean createExplosion(Location loc, float power) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public boolean createExplosion(Location loc, float power, boolean setFire) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public Environment getEnvironment() {
        switch(Minecraft.getMinecraft().theWorld.provider.getDimensionId()) {
            case -1:
                return Environment.NETHER;
            case 0:
                return Environment.NORMAL;
            case 1:
                return Environment.THE_END;
            default:
                return null;
        }
    }

    @Override
    public long getSeed() {
        return Minecraft.getMinecraft().theWorld.getSeed();
    }

    @Override
    public boolean getPVP() {
        //TODO
        return true;
    }

    @Override
    public void setPVP(boolean pvp) {
        //TODO
    }

    @Override
    public void save() {
        Minecraft.getMinecraft().theWorld.getSaveHandler().flush();
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        //TODO
    }

    @Override
    public boolean getAllowAnimals() {
        //TODO
        return true;
    }

    @Override
    public boolean getAllowMonsters() {
        //TODO
        return true;
    }

    @Override
    public Biome getBiome(int x, int z) {
        //TODO Too lazy, do later
        throw new NotImplementedException();
    }

    @Override
    public void setBiome(int x, int z, Biome bio) {
        //TODO Too lazy, do later
        throw new NotImplementedException();
    }

    @Override
    public double getTemperature(int x, int z) {
        //TODO Too lazy, do later
        throw new NotImplementedException();
    }

    @Override
    public double getHumidity(int x, int z) {
        //TODO Too lazy, do later
        throw new NotImplementedException();
    }

    @Override
    public int getMaxHeight() {
        return Minecraft.getMinecraft().theWorld.getHeight();
    }

    @Override
    public int getSeaLevel() {
        //TODO YOLO
        return 64;
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        //TODO
        return true;
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        //TODO
    }

    @Override
    public boolean isAutoSave() {
        //TODO
        return true;
    }

    @Override
    public void setAutoSave(boolean value) {
        //TODO
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        Minecraft.getMinecraft().theWorld.getWorldInfo().setDifficulty(Difficulty.asMCDifficulty(difficulty));
    }

    @Override
    public Difficulty getDifficulty() {
        return Difficulty.fromMCDifficulty(Minecraft.getMinecraft().theWorld.getWorldInfo().getDifficulty());
    }

    @Override
    public File getWorldFolder() {
        return Minecraft.getMinecraft().theWorld.getSaveHandler().getWorldDirectory();
    }

    @Override
    public WorldType getWorldType() {
        return Minecraft.getMinecraft().theWorld.getWorldType();
    }

    @Override
    public boolean canGenerateStructures() {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().isMapFeaturesEnabled();
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        //TODO
        throw new NotImplementedException();    }

    @Override
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public int getMonsterSpawnLimit() {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public void setMonsterSpawnLimit(int limit) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public int getAnimalSpawnLimit() {
        //TODO
        throw new NotImplementedException();    }

    @Override
    public void setAnimalSpawnLimit(int limit) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public void setWaterAnimalSpawnLimit(int limit) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public int getAmbientSpawnLimit() {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public void setAmbientSpawnLimit(int limit) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public String[] getGameRules() {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().getGameRulesInstance().getRules();
    }

    @Override
    public String getGameRuleValue(String rule) {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().getGameRulesInstance().getGameRuleStringValue(rule);
    }

    @Override
    public boolean setGameRuleValue(String rule, String value) {
        Minecraft.getMinecraft().theWorld.getWorldInfo().getGameRulesInstance().setOrCreateGameRule(rule, value);
        return true;
    }

    @Override
    public boolean isGameRule(String rule) {
        //TODO Maybe correct?
        return Minecraft.getMinecraft().theWorld.getWorldInfo().getGameRulesInstance().hasRule(rule);
    }
}
