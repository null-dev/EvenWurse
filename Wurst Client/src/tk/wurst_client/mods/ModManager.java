/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

/**
 * Rewritten mod manager based on my dynamically loaded plugin system in my AI.
 */
public class ModManager
{
    //Load in default conversations
    public static Class<? extends Mod>[] DEFAULT_MODS;
    public static String MOD_PACKAGE = "tk.wurst_client.mods";

    Mod[] loadedMods;

    public ModManager() {
        //Load all mods into memory
        System.out.println("[EvenWurse] Loading mods into memory...");
        loadedMods = new Mod[DEFAULT_MODS.length];
        int loaded = 0;
        for (int i = 0; i < DEFAULT_MODS.length; i++) {
            try {
                System.out.println("[EvenWurse] Loading mod '" + DEFAULT_MODS[i].getSimpleName() + "'...");
                Mod mod = DEFAULT_MODS[i].getConstructor().newInstance(this);
                mods.put(mod.getName(), mod);
                mod.initSliders();
                loadedMods[i] = mod;
                loaded++;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                System.out.println("[EvenWurse] Exception loading mod '" + DEFAULT_MODS[i].getSimpleName() + "', skipping!");
                e.printStackTrace();
            }
        }
        System.out.println("[EvenWurse] Loaded " + loaded + " mods!");
    }

    public static void reloadMods() {
        //Populate all the mods
        System.out.println("[EvenWurse] Reloading mod list...");
        ArrayList<ClassLoader> classLoadersList = new ArrayList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[classLoadersList.size()])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(MOD_PACKAGE))));
        Set<Class<? extends Mod>> classes = reflections.getSubTypesOf(Mod.class);
        DEFAULT_MODS = new Class[classes.size()];
        for(int i = 0; i < classes.size(); i++) {
            DEFAULT_MODS[i] = (Class<? extends Mod>) classes.toArray()[i];
            System.out.println("[EvenWurse] Found mod: " + DEFAULT_MODS[i].getSimpleName() + "!");
        }
        System.out.println("[EvenWurse] Found " + DEFAULT_MODS.length + " mods!");
    }

    private final TreeMap<String, Mod> mods = new TreeMap<>(
            String::compareToIgnoreCase);

    public Mod getModByName(String name)
    {
        return mods.get(name);
    }

    public Collection<Mod> getAllMods()
    {
        return mods.values();
    }

    public int countMods()
    {
        return mods.size();
    }
}
