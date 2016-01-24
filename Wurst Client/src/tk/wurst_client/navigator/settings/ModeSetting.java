package tk.wurst_client.navigator.settings;

import com.google.gson.JsonObject;
import tk.wurst_client.navigator.gui.NavigatorFeatureScreen;
import tk.wurst_client.navigator.gui.NavigatorFeatureScreen.ButtonData;

import java.awt.*;

/**
 * Project: EvenWurse
 * Created: 10/01/16
 * Author: nulldev
 */
public class ModeSetting implements NavigatorSetting {
    private String name;
    private String[] modes;
    private int selected;

    public ModeSetting(String name, String[] modes, int selected) {
        this.name = name;
        this.modes = modes;
        this.selected = selected;
    }

    @Override
    public void addToFeatureScreen(NavigatorFeatureScreen featureScreen) {
        // heading
        featureScreen.addText("\n" + name + ":");

        // buttons
        int y = 0;
        ButtonData[] buttons = new ButtonData[modes.length];
        for (int i = 0; i < modes.length; i++) {
            int x = featureScreen.getMiddleX();
            switch (i % 4) {
                case 0:
                    x -= 132;
                    featureScreen.addText("\n");
                    y = 60 + featureScreen.getTextHeight();
                    break;
                case 1:
                    x -= 61;
                    break;
                case 2:
                    x += 11;
                    break;
                case 3:
                    x += 83;
                    break;
            }
            final int iFinal = i;
            ButtonData button =
                    featureScreen.new ButtonData(x, y, 50, 10, modes[i], i == selected ? 0x00ff00 : 0x404040) {
                        @Override
                        public void press() {
                            buttons[selected].color = new Color(0x404040);
                            selected = iFinal;
                            color = new Color(0x00ff00);
                        }
                    };
            buttons[i] = button;
            featureScreen.addButton(button);
        }
    }

    public int getSelected() {
        return selected;
    }

    @Override
    public void save(JsonObject json) {
        json.addProperty(name, selected);
    }

    @Override
    public void load(JsonObject json) {
        selected = json.get(name).getAsInt();
    }
}