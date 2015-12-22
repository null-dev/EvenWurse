package tk.wurst_client.api;

import net.minecraft.client.Minecraft;

/**
 * Project: EvenWurse
 * Created: 21/12/15
 * Author: nulldev
 */
public class Chat {
    /**
     * Send a message (chat message or command if it starts with '/')
     * @param message The message to send.
     */
    public static void sendMessage(String message) {
        Minecraft.getMinecraft().thePlayer.sendAutomaticChatMessage(message);
    }
}
