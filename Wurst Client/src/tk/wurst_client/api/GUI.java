package tk.wurst_client.api;

/**
 * Project: EvenWurse
 * Created: 01/01/16
 * Author: nulldev
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.ClientTickListener;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * GUI Utils
 */
public class GUI {

    private static GUIScreenDisplayer SCREEN_DISPLAYER = null;
    /**
     * CORRECT way to display a GUI screen. Displays the screen on the next tick.
     *
     * Always use this method to display a GUI screen as the GUI screen will not be displayed
     * if run from other threads.
     * @param screen The GUIScreen to display.
     */
    public static synchronized void displayGuiScreen(GuiScreen screen) {
        if(SCREEN_DISPLAYER == null) {
            SCREEN_DISPLAYER = new GUIScreenDisplayer();
            WurstClient.INSTANCE.events.add(ClientTickListener.class, SCREEN_DISPLAYER);
        }
        SCREEN_DISPLAYER.queue(screen);
    }
}
class GUIScreenDisplayer implements ClientTickListener {

    private ConcurrentLinkedQueue<GuiScreen> screenQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void onTick() {
        if(!screenQueue.isEmpty()) {
            Iterator<GuiScreen> screenIterable = screenQueue.iterator();
            while (screenIterable.hasNext()) {
                Minecraft.getMinecraft().displayGuiScreen(screenIterable.next());
                screenIterable.remove();
            }
        }
    }

    public void queue(GuiScreen screen) {
        screenQueue.add(screen);
    }
}