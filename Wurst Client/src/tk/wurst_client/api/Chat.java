package tk.wurst_client.api;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import tk.wurst_client.WurstClient;

/**
 * Project: EvenWurse
 * Created: 21/12/15
 * Author: nulldev
 */

/**
 * It's annoying having to get an instance of EvenWurse every time I want to send a chat message
 */
public class Chat {
    /**
     * Send a message (chat message or command if it starts with '/')
     *
     * @param message The message to send.
     */
    public static void sendMessage(String message) {
        Minecraft.getMinecraft().thePlayer.sendAutomaticChatMessage(message);
    }

    /**
     * Send client only message.
     * Overrides recording mode!
     *
     * @param message The message to send
     */
    public static void sendClientOnlyMessage(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
    }

    public static void clearChat() {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
    }

    public static void sendDebug(String debug) {
        WurstClient.INSTANCE.chat.debug(debug);
    }

    public static void sendInfo(String info) {
        WurstClient.INSTANCE.chat.info(info);
    }

    public static void sendWarning(String warning) {
        WurstClient.INSTANCE.chat.warning(warning);
    }

    public static void sendSuccess(String success) {
        WurstClient.INSTANCE.chat.success(success);
    }

    public static void sendError(String message) {
        WurstClient.INSTANCE.chat.error(message);
    }

    public static void sendFailure(String failure) {
        WurstClient.INSTANCE.chat.failure(failure);
    }

    public static void sendCmd(String cmd) {
        WurstClient.INSTANCE.chat.cmd(cmd);
    }

    public static void sendClientWurstMessage(String message) {
        WurstClient.INSTANCE.chat.message(message);
    }
}
