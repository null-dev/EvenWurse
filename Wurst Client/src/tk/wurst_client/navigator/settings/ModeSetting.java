/*
 * Copyright � 2014 - 2016 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.navigator.settings;

import com.google.gson.JsonObject;
import tk.wurst_client.WurstClient;
import tk.wurst_client.navigator.gui.NavigatorFeatureScreen;
import tk.wurst_client.navigator.gui.NavigatorFeatureScreen.ButtonData;

import java.awt.*;

public abstract class ModeSetting implements NavigatorSetting {
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
                    featureScreen.addText("\n\n");
                    y = 60 + featureScreen.getTextHeight() - 2;
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
                    featureScreen.new ButtonData(x, y, 50, 16, modes[i], i == selected ? 0x00ff00 : 0x404040) {
                        @Override
                        public void press() {
                            buttons[selected].color = new Color(0x404040);
                            color = new Color(0x00ff00);
                            setSelected(iFinal);
                            WurstClient.INSTANCE.files.saveNavigatorData();
                        }
                    };
            buttons[i] = button;
            featureScreen.addButton(button);
        }
    }

    protected int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
        update();
    }

    @Override
    public void save(JsonObject json) {
        json.addProperty(name, selected);
    }

    @Override
    public void load(JsonObject json) {
        setSelected(json.get(name).getAsInt());
    }
}
