package tk.wurst_client.events.listeners;

/**
 * Project: EvenWurse
 * Created: 01/01/16
 * Author: nulldev
 */

/**
 * Client tick, not a game tick!
 */
public interface ClientTickListener extends Listener {
    void onTick();
}
