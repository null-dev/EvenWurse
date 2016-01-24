package tk.wurst_client.api;

/**
 * Project: EvenWurse
 * Created: 24/12/15
 * Author: nulldev
 */

import net.minecraft.client.Minecraft;
import net.minecraft.world.EnumDifficulty;

import java.util.UUID;

/**
 * Very basic world API
 */
public class World {
    public static String getName() {
        String dim = Minecraft.getMinecraft().theWorld.provider.getDimensionName();
        if (!Server.inServer()) {
            return Minecraft.getMinecraft().getIntegratedServer().getWorldName() + " (" + dim + ")";
        } else {
            return Minecraft.getMinecraft().theWorld.getWorldInfo().getWorldName() + " (" + dim + ")";
        }
    }

    public static EnumDifficulty getDifficulty() {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().getDifficulty();
    }

    public static UUID getUUID() {
        String ip = "SP";
        if (Server.inServer()) ip = Server.getIP();
        return UUID.nameUUIDFromBytes((ip + ":" + World.getName()).getBytes());
    }
}
