package xyz.nulldev.ew.modules;

import net.minecraft.client.Minecraft;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Chat;
import tk.wurst_client.api.Module;
import tk.wurst_client.api.ModuleConfiguration;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.utils.EntityUtils;
import tk.wurst_client.utils.F;

import java.util.Map;
import java.util.Objects;

/**
 * Project: EvenWurse
 * Created: 23/12/15
 * Author: nulldev
 */

/**
 * Extension to the teams feature by autoseting-up teams from the player's nametag
 */
//FIXME Migrate to new targeting
// STOPSHIP: 24/01/16 MIGRATE TO NEW TARGETING
@Module.ModuleInfo(version = 1.00f, minVersion = 131, usesConfig = true)
public class AutoTeamSetupModule extends Module implements UpdateListener {

    String previousTag = "";

    @Override
    public void onUnload() {
        super.onUnload();
        WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        WurstClient.INSTANCE.events.add(UpdateListener.class, this);
        //Init notify config entry
        ModuleConfiguration.forModule(this).getBoolean("Notify on Team Change", true);
    }

    @Override
    public void onUpdate() {
        if (!ModuleConfiguration.forModule(this).getBoolean("Enabled", true)) return;
        String curTag = Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText();
        if (curTag == null || curTag.isEmpty() || curTag.equals(previousTag)) return;
        boolean nextIsCode = false;
        for (char c : curTag.toCharArray()) {
            if (nextIsCode) {
                String code = F.STRING_SS + c;
                if (F.isColorCode(code)) {
                    if (WurstClient.INSTANCE.options.target.teams) {
                        boolean[] teamColors = WurstClient.INSTANCE.options.target.getTeamColorsSafely();
                        for (int i = 0; i < teamColors.length; i++) {
                            teamColors[i] = !Objects.equals(String.valueOf(c), EntityUtils.COLORS[i]);
                        }
                        teamColors[15] = false;
                        if (ModuleConfiguration.forModule(this).getBoolean("Notify on Team Change", true)) {
                            String english = "COLOR";
                            for (Map.Entry<String, String> e : F.COLOR_MAP.entrySet()) {
                                if (e.getValue().equals(code)) {
                                    english = e.getKey();
                                    break;
                                }
                            }
                            Chat.sendInfo(
                                    "AutoTeamSetup has changed your team targeting settings to: \"Target all colors " +
                                            "except: " +
                                            code + english + F.RESET + " and " + F.WHITE + "WHITE" + F.RESET + "\"!");
                        }
                    }
                    break;
                }
                nextIsCode = false;
            } else if (c == F.SS) {
                nextIsCode = true;
            }
        }
        previousTag = curTag;
    }
}
