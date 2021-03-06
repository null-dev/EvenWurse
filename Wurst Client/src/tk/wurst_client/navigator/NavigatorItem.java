/*
 * Copyright � 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.navigator;

import tk.wurst_client.navigator.settings.NavigatorSetting;

import java.util.ArrayList;

public interface NavigatorItem {
    String getName();

    String getType();

    String getDescription();

    boolean isEnabled();

    boolean isBlocked();

    String getTags();

    ArrayList<NavigatorSetting> getSettings();

    ArrayList<PossibleKeybind> getPossibleKeybinds();

    String getPrimaryAction();

    void doPrimaryAction();

    String getTutorialPage();

    public NavigatorItem[] getSeeAlso();
}
