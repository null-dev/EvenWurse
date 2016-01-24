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
import tk.wurst_client.navigator.gui.NavigatorFeatureScreen;

public abstract class CheckboxSetting implements NavigatorSetting {
    private String name;
    private boolean checked;

    public CheckboxSetting(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    @Override
    public void addToFeatureScreen(NavigatorFeatureScreen featureScreen) {
        featureScreen.addText("\n");
        featureScreen.addCheckbox(featureScreen.new CheckboxData(name, checked, 60 + featureScreen.getTextHeight()) {
            @Override
            public void toggle() {
                update();
            }
        });
    }

    @Override
    public void save(JsonObject json) {
        json.addProperty(name, checked);
    }

    @Override
    public void load(JsonObject json) {
        checked = json.get(name).getAsBoolean();
    }
}
