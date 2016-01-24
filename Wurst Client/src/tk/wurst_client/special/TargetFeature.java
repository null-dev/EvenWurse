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

import java.lang.reflect.Field;

@SpecialFeature.Info(description = "Controls what entities are targeted by " + "other features (e.g. Killaura).",
        name = "Target")
public class TargetFeature extends SpecialFeature {
    private boolean players = true;
    private boolean animals = true;
    private boolean monsters = true;
    private boolean golems = true;

    //Please note that these variables have been renamed to follow the official Java codestyle
    private boolean sleepingPlayers = false;
    private boolean invisiblePlayers = false;
    private boolean invisibleMobs = false;

    private boolean teams = false;

    @Override
    public String getPrimaryAction() {
        return "";
    }

    @Override
    public void doPrimaryAction() {

    }

    public TargetFeature() {
        //FIXME Migrate to configuration API
        for (Field field : TargetFeature.class.getFields()) {
            if (!field.getType().equals(boolean.class)) continue;

            //FIXME Fix prettyfication to be compatible with Java code style (or just migrate this to the config api again)
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
    }

    public boolean isPlayers() {
        return players;
    }

    public void setPlayers(boolean players) {
        this.players = players;
    }

    public boolean isAnimals() {
        return animals;
    }

    public void setAnimals(boolean animals) {
        this.animals = animals;
    }

    public boolean isMonsters() {
        return monsters;
    }

    public void setMonsters(boolean monsters) {
        this.monsters = monsters;
    }

    public boolean isGolems() {
        return golems;
    }

    public void setGolems(boolean golems) {
        this.golems = golems;
    }

    public boolean isSleepingPlayers() {
        return sleepingPlayers;
    }

    public void setSleepingPlayers(boolean sleepingPlayers) {
        this.sleepingPlayers = sleepingPlayers;
    }

    public boolean isInvisiblePlayers() {
        return invisiblePlayers;
    }

    public void setInvisiblePlayers(boolean invisiblePlayers) {
        this.invisiblePlayers = invisiblePlayers;
    }

    public boolean isInvisibleMobs() {
        return invisibleMobs;
    }

    public void setInvisibleMobs(boolean invisibleMobs) {
        this.invisibleMobs = invisibleMobs;
    }

    public boolean isTeams() {
        return teams;
    }

    public void setTeams(boolean teams) {
        this.teams = teams;
    }
}
