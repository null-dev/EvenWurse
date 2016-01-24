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
import tk.wurst_client.navigator.settings.ColorsSetting;

//TODO Refactor
@SpecialFeature.Info(description = "Controls what entities are targeted by " + "other features (e.g. Killaura).",
        name = "Target")
public class TargetFeature extends SpecialFeature {
    public final CheckboxSetting players = new CheckboxSetting("Players", true);
    public final CheckboxSetting animals = new CheckboxSetting("Animals", true);
    public final CheckboxSetting monsters = new CheckboxSetting("Monsters", true);
    public final CheckboxSetting golems = new CheckboxSetting("Golems", true);

    public final CheckboxSetting sleepingPlayers = new CheckboxSetting("Sleeping players", false);
    public final CheckboxSetting invisiblePlayers = new CheckboxSetting("Invisible players", false);
    public final CheckboxSetting invisibleMobs = new CheckboxSetting("Invisible mobs", false);

    public final CheckboxSetting teams = new CheckboxSetting("Teams", false);
    public final ColorsSetting teamColors = new ColorsSetting("Team Colors",
            new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
                    true});

    public TargetFeature() {
        settings.add(players);
        settings.add(animals);
        settings.add(monsters);
        settings.add(golems);

        settings.add(sleepingPlayers);
        settings.add(invisiblePlayers);
        settings.add(invisibleMobs);

        settings.add(teams);
        settings.add(teamColors);
    }

    @Override
    public String getPrimaryAction() {
        return "";
    }

    @Override
    public void doPrimaryAction() {

    }
}
