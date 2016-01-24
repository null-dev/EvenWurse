/*
 * Copyright � 2014 - 2016 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.special;

import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.PossibleKeybind;
import tk.wurst_client.navigator.settings.NavigatorSetting;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

//TODO Look through and consider adding API to it
// STOPSHIP: 24/01/16 NEEDS API
/*
Let's think about this a little, it looks like almost all the functionality in special features
is covered in a regular module. Maybe migrate to that?
 */
public abstract class SpecialFeature implements NavigatorItem {
    private final String name = getClass().getAnnotation(Info.class).name();
    private final String description = getClass().getAnnotation(Info.class).description();
    private final String[] tags = getClass().getAnnotation(Info.class).tags();
    private final String tutorial = getClass().getAnnotation(Info.class).tutorial();
    protected ArrayList<NavigatorSetting> settings = new ArrayList<>();

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();

        String description();

        String[] tags() default {};

        String tutorial() default "";
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getType() {
        return "Special Feature";
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isBlocked() {
        return false;
    }

    @Override
    public final String[] getTags() {
        return tags;
    }

    @Override
    public final ArrayList<NavigatorSetting> getSettings() {
        return settings;
    }

    @Override
    public ArrayList<PossibleKeybind> getPossibleKeybinds() {
        return new ArrayList<PossibleKeybind>();
    }

    @Override
    public final String getTutorialPage() {
        return tutorial;
    }
}
