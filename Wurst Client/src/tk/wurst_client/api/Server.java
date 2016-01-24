package tk.wurst_client.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

/**
 * Project: EvenWurse
 * Created: 24/12/15
 * Author: nulldev
 */

/**
 * Very basic server API
 */
public class Server {

    public static String getIP() {
        if (!inServer()) return null;
        return getServerData().serverIP;
    }

    public static String getName() {
        if (!inServer()) return null;
        return getServerData().serverName;
    }

    public static String getMOTD() {
        if (!inServer()) return null;
        return getServerData().serverMOTD;
    }

    public static long getPing() {
        if (!inServer()) return -1;
        return getServerData().pingToServer;
    }

    public static ServerData getServerData() {
        return Minecraft.getMinecraft().getCurrentServerData();
    }

    public static boolean inServer() {
        return Minecraft.getMinecraft().getCurrentServerData() != null;
    }
}
