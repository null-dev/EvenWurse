/*
 * Copyright � 2014 - 2016 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.special;

import tk.wurst_client.navigator.settings.CheckboxSetting;
import tk.wurst_client.navigator.settings.NavigatorSetting;

import java.lang.reflect.Field;
import java.util.ArrayList;

@SpecialFeature.Info(description = "Controls what entities are targeted by " + "other features (e.g. Killaura).",
        name = "Target")
public class TargetFeature extends SpecialFeature {
    public boolean players = true;
    public boolean animals = true;
    public boolean monsters = true;
    public boolean golems = true;

    public boolean sleeping_players = false;
    public boolean invisible_players = false;
    public boolean invisible_mobs = false;

    public boolean teams = false;

    @Override
    public String getPrimaryAction() {
        return "";
    }

    @Override
    public void doPrimaryAction() {

    }

    @Override
    public ArrayList<NavigatorSetting> getSettings() {
        ArrayList<NavigatorSetting> settings = super.getSettings();

        //FIXME Needs to switch to API
        // STOPSHIP: 24/01/16 NEEDS TO CALL API
        for (Field field : TargetFeature.class.getFields()) {
            if (!field.getType().equals(boolean.class)) continue;

            String name =
                    field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1).replace("_", " ");

            boolean checked = false;
            try {
                checked = field.getBoolean(this);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

            settings.add(new CheckboxSetting(name, checked) {
                @Override
                public void update() {
                    try {
                        field.setBoolean(TargetFeature.this, isChecked());
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return settings;
    }
}
