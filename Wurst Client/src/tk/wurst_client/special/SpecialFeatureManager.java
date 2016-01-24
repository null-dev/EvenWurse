/*
 * Copyright � 2014 - 2016 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.special;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

//TODO Refactor into module based (and add API)
public class SpecialFeatureManager {
    private final TreeMap<String, SpecialFeature> features =
            new TreeMap<String, SpecialFeature>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });

    public SpecialFeatureManager() {
        try {
            for (Field field : SpecialFeatureManager.class.getFields()) {
                if (field.getName().endsWith("Feature")) {
                    SpecialFeature cmd = (SpecialFeature) field.get(this);
                    features.put(cmd.getName(), cmd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SpecialFeature getFeatureByName(String name) {
        return features.get(name);
    }

    public Collection<SpecialFeature> getAllFeatures() {
        return features.values();
    }

    public int countFeatures() {
        return features.size();
    }
}
