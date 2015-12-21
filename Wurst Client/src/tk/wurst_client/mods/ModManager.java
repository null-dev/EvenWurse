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
import tk.wurst_client.Module;
import tk.wurst_client.WurstClient;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Rewritten mod manager based on my dynamically loaded plugin system in my AI.
 */
public class ModManager
{
    public static Class<? extends Mod>[] KNOWN_MODS = null;

    public ModManager() {
        //Scan for mods
        ModManager.scanForMods();
        //Load all mods into memory
        mods.clear();
        modClasses.clear();
        loadAllMods();
    }

    void loadAllMods() {
        System.out.println("[EvenWurse] Loading mods into memory...");
        int loaded = 0;
        for (Class<? extends Mod> MOD : KNOWN_MODS) {
            try {
                loadMod(MOD);
                loaded++;
            } catch (Module.ModuleLoadException e) {
                handleModuleLoadException(e, MOD.getSimpleName());
            }
        }
        System.out.println("[EvenWurse] Loaded " + loaded + " mods!");
    }

    public static void handleModuleLoadException(Module.ModuleLoadException e, String name) {
        if(e instanceof Module.OutdatedClientException) {
            Module.OutdatedClientException e1 = (Module.OutdatedClientException) e;
            System.out.println("[EvenWurse] Error loading mod: '"
                    + e1.getModName()
                    + "'! This mod requires EvenWurse version: '"
                    + e1.getMinVersion()
                    + "' but you are running EvenWurse version: '"
                    + WurstClient.EW_VERSION_CODE
                    + "'. Please update EvenWurse to use this mod!");
        } else {
            System.out.println("[EvenWurse] Exception loading mod: '" + name + "', skipping!");
            e.printStackTrace();
        }
    }

    /**
     * Load a mod into memory.
     * @param clazz The class of the mod to load
     * @return The loaded mod object
     * @throws Module.ModuleLoadException Failed to load mod
     */
    public Mod loadMod(Class<? extends Mod> clazz) throws Module.ModuleLoadException {
        System.out.println("[EvenWurse] Loading mod from class: " + clazz.getSimpleName());
        Mod mod;
        try {
            mod = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new Module.ModuleLoadException("Unknown error loading mod!", e);
        }
        //Don't load mods that require a higher version than us
        if(mod.getMinVersion() > WurstClient.EW_VERSION_CODE) {
            throw new Module.OutdatedClientException(mod.getName(), mod.getMinVersion(), WurstClient.EW_VERSION_CODE);
        }
        mods.put(mod.getName(), mod);
        modClasses.put(mod.getClass(), mod);
        mod.initSliders();
        return mod;
    }

    public static void scanForMods() {
        //Populate all the mods
        System.out.println("[EvenWurse] Reloading mod list...");
        ArrayList<ClassLoader> classLoadersList = new ArrayList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[classLoadersList.size()]))));
        Set<Class<? extends Mod>> classes = reflections.getSubTypesOf(Mod.class);
        KNOWN_MODS = new Class[classes.size()];
        for(int i = 0; i < classes.size(); i++) {
            KNOWN_MODS[i] = (Class<? extends Mod>) classes.toArray()[i];
            System.out.println("[EvenWurse] Found mod: " + KNOWN_MODS[i].getSimpleName() + "!");
        }
        System.out.println("[EvenWurse] Found " + KNOWN_MODS.length + " mods!");
    }

    private final TreeMap<String, Mod> mods = new TreeMap<>(String::compareToIgnoreCase);

    private final HashMap<Class<? extends Mod>, Mod> modClasses = new HashMap<>();

    //Really really stupid way of getting mods :/
    @Deprecated
    public Mod getModByName(String name)
    {
        return mods.get(name);
    }

    public <T> T getModByClass(Class<T> theClass) {
        return (T) modClasses.get(theClass);
    }

    @SafeVarargs
    public final void disableModsByClass(Class<? extends Mod>... classes) {
        for(Class<? extends Mod> clazz : classes) {
            Mod mod = getModByClass(clazz);
            if(mod.isEnabled()) {
                mod.setEnabled(false);
            }
        }
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
