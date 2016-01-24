package xyz.nulldev.ew.mods;

import net.minecraft.util.BlockPos;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Chat;
import tk.wurst_client.api.Module;
import tk.wurst_client.events.ChatOutputEvent;
import tk.wurst_client.events.listeners.ChatOutputListener;
import tk.wurst_client.mods.Mod;

/**
 * Project: EvenWurse
 * Created: 19/12/15
 * Author: nulldev
 */

@Module.ModuleInfo(version = 1.02f,
        minVersion = 120)
@Mod.Info(category = Mod.Category./*RENDER*/HIDDEN,
        description = "Faster, enhanced version of .path and .goto!",
        name = "GPS")
//TODO WIP (Temporarily on hold...)
public class GPSMod extends Mod implements ChatOutputListener {

    float x;
    float z;

    @Override
    public void onEnable() {
        WurstClient.INSTANCE.chat.info("Initializing GPS...");
        WurstClient.INSTANCE.chat.info("Please enter the coordinates to go to: [x] [z]");
        WurstClient.INSTANCE.events.add(ChatOutputListener.class, this);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        WurstClient.INSTANCE.events.remove(ChatOutputListener.class, this);
        super.onDisable();
    }

    @Override
    public void initSettings() {
        super.initSettings();
    }

    @Override
    public void updateSettings() {
        super.updateSettings();
    }

    @Override
    public void onSentMessage(ChatOutputEvent event) {
        event.cancel();
        String[] split = event.getMessage().split(" ");
        if (split.length != 3) {
            Chat.sendError("Invalid coordinate format!");
            return;
        }
        try {
            x = Integer.parseInt(split[0]);
            z = Integer.parseInt(split[2]);
        } catch (NumberFormatException e) {
            Chat.sendError("Invalid coordinates!");
            return;
        }
        WurstClient.INSTANCE.events.remove(ChatOutputListener.class, this);
        Chat.sendInfo("Attempting to route to: (" + x + ", " + z + ")...");

    }

    enum Direction {
        NORTH(0, -1),
        WEST(-1, 0),
        SOUTH(0, 1),
        EAST(1, 0),
        HERE(0, 0);

        private int modX;
        private int modZ;
        Direction(int modX, int modZ) {
            this.modX = modX;
            this.modZ = modZ;
        }

        static Direction directionFromDiff(BlockPos origin, BlockPos target) {
            int xDiff = origin.getX() - target.getX();
            int zDiff = origin.getZ() - target.getZ();
            if (xDiff == 0 && zDiff == 0) {
                return HERE;
            }
            int absXDiff = Math.abs(xDiff);
            int absZDiff = Math.abs(zDiff);
            if (absXDiff == absZDiff) {
                if (xDiff < 0) {
                    return WEST;
                } else {
                    return EAST;
                }
            } else if (absXDiff > absZDiff) {
                if (xDiff > 0) {
                    return EAST;
                } else {
                    return WEST;
                }
            } else {
                if (zDiff > 0) {
                    return SOUTH;
                } else {
                    return NORTH;
                }
            }
        }

        public int getModX() {
            return modX;
        }

        public int getModZ() {
            return modZ;
        }
    }
}
