package xyz.nulldev.ew.mods;

import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Chat;
import tk.wurst_client.api.Module;
import tk.wurst_client.api.ModuleConfiguration;
import tk.wurst_client.api.World;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.utils.F;
import xyz.nulldev.ew.GLHelper;
import xyz.nulldev.ew.waypoints.Waypoint;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Project: EvenWurse
 * Created: 23/12/15
 * Author: nulldev
 */

/**
 * Waypoints mod from Colony hacked client
 */
@Module.ModuleInfo(version = 1.02f, usesConfig = true, minVersion = 133)
@Mod.Info(name = "Waypoints", description = "Highlights specified positions.", category = Mod.Category.RENDER)
public class WaypointsMod extends Mod implements RenderListener {

    @Override
    public void onUnload() {
        super.onUnload();
        Waypoint.saveAll();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Waypoint.loadAll();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        WurstClient.INSTANCE.events.add(RenderListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        WurstClient.INSTANCE.events.remove(RenderListener.class, this);
    }

    @Override
    public void onRender() {
        float scale = ModuleConfiguration.forModule(this).getFloat("Label Scale", 0.5f);
        if (scale < 0) {
            Chat.sendError("Label scale was negative, changing back to default!");
            ModuleConfiguration.forModule(this).putString("Label Scale", String.valueOf(0.5f));
        }
        boolean renderBolt = ModuleConfiguration.forModule(this).getBoolean("Render Lightning Bolt", true);
        boolean renderESP = ModuleConfiguration.forModule(this).getBoolean("Render ESP", true);
        boolean renderLabel = ModuleConfiguration.forModule(this).getBoolean("Render Label", true);
        boolean renderTracer = ModuleConfiguration.forModule(this).getBoolean("Render Tracer", true);
        boolean perWorld = ModuleConfiguration.forModule(this).getBoolean("Per World/Server", true);
        String curWorld;
        if (perWorld) {
            curWorld = World.getUUID().toString();
        } else {
            curWorld = null;
        }
        List<Waypoint> possibleWaypoints =
                Waypoint.WAYPOINTS.parallelStream().filter(w -> Objects.equals(curWorld, w.getWorld()))
                        .collect(Collectors.toList());
        //I render lightning bolts by themselves as they seem to bug out if rendered with the others...
        if (renderBolt) {
            possibleWaypoints.stream().forEach(w -> GLHelper.drawLightning(w.dX, w.dZ, w.red, w.green, w.blue));
        }
        for (Waypoint w : possibleWaypoints) {
            w.update();
            //            GL11.glDisable(2896 /*GL_LIGHTING*/);
            if (renderESP) GLHelper.drawESP(w.dX, w.dY, w.dZ, w.red, w.green, w.blue);
            if (renderLabel) GLHelper.drawTag(F.f(w.getName()), w.dX, w.dY, w.dZ, scale);
            //            GL11.glEnable(2896 /*GL_LIGHTING*/);
            if (renderTracer) GLHelper.drawWayPointTracer(w);
        }
    }
}
