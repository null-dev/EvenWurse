package xyz.nulldev.ew.waypoints;

import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.ModuleConfiguration;
import tk.wurst_client.utils.GsonUtils;
import xyz.nulldev.ew.mods.WaypointsMod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Project: EvenWurse
 * Created: 23/12/15
 * Author: nulldev
 */

/**
 * Waypoint from colony hacked client
 */
public class Waypoint {

    public static final Type TYPE = new TypeToken<List<Waypoint>>() {
    }.getType();
    private static final String CONFIG_KEY = "Waypoints";
    public static List<Waypoint> WAYPOINTS = new ArrayList<>();
    static Random RANDOM = null;
    public final String name;
    private final double posX;
    private final double posY;
    private final double posZ;
    private final String world;
    public transient double dX;
    public transient double dY;
    public transient double dZ;
    public float red, green, blue;

    public Waypoint(String name, double x, double y, double z) {
        this(name, x, y, z, null);
    }

    public Waypoint(String name, double x, double y, double z, String world) {
        this.name = name;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        if (getConfig().getBoolean("Per World/Server", true)) {
            this.world = world;
        } else {
            this.world = null;
        }
        this.red = (initAndGetRandom().nextInt(255)) / 255F;
        this.green = (initAndGetRandom().nextInt(255)) / 255F;
        this.blue = (initAndGetRandom().nextInt(255)) / 255F;
        update();
        saveAll();
    }

    static Random initAndGetRandom() {
        if (RANDOM == null) RANDOM = new Random();
        return RANDOM;
    }

    public static ModuleConfiguration getConfig() {
        return ModuleConfiguration.forModule(WurstClient.INSTANCE.mods.getModByClass(WaypointsMod.class));
    }

    public static void saveAll() {
        getConfig().putString(CONFIG_KEY, GsonUtils.getGson().toJson(WAYPOINTS, TYPE));
    }

    public static void loadAll() {
        ModuleConfiguration configuration = getConfig();
        if (configuration.contains(CONFIG_KEY)) {
            WAYPOINTS = GsonUtils.getGson().fromJson(configuration.getString(CONFIG_KEY, ""), TYPE);
        }
    }

    public void update() {
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        dX = (int) posX - manager.renderPosX;
        dY = (int) posY - manager.renderPosY;
        dZ = (int) posZ - manager.renderPosZ;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return posX;
    }

    public double getY() {
        return posY;
    }

    public double getZ() {
        return posZ;
    }

    public String getWorld() {
        return world;
    }
}
