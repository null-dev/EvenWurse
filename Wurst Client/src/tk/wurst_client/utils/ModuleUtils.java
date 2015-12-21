package tk.wurst_client.utils;

import tk.wurst_client.Module;
import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd;
import tk.wurst_client.commands.CmdManager;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.ModManager;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Project: EvenWurse
 * Created: 20/12/15
 * Author: nulldev
 */
public class ModuleUtils {

    //Misc modules, TODO refactor later
    public static ArrayList<Module> miscModules = new ArrayList<>();

    public static void reloadModules() {
        System.out.println("[EvenWurse] Reloading modules...");
        ModManager modManager = WurstClient.INSTANCE.mods;
        ArrayList<Mod> modsToRemoveList = modManager.getCustomMods();
        Mod[] modsToRemove = modsToRemoveList.toArray(new Mod[modsToRemoveList.size()]);
        CmdManager commandManager = WurstClient.INSTANCE.commands;
        ArrayList<Cmd> cmdsToRemoveList = commandManager.getCustomCommands();
        Cmd[] cmdsToRemove = cmdsToRemoveList.toArray(new Cmd[cmdsToRemoveList.size()]);
        modManager.unloadMods(modsToRemove);
        commandManager.unloadCommands(cmdsToRemove);
        for(Module module : miscModules) {
            try {
                module.onUnload();
            } catch(Throwable t) {
                System.out.println("[EvenWurse] Module in class '"
                        + module.getClass().getSimpleName()
                        + "' threw exception in onUnload(), ignoring!");
            }
        }
        miscModules.clear();
        int mods = modsToRemove.length;
        int cmds = cmdsToRemove.length;
        modsToRemoveList = null;
        modsToRemove = null;
        cmdsToRemoveList = null;
        cmdsToRemove = null;
        System.gc();
        WurstClient.INSTANCE.files.loadModules();
        System.out.println("[EvenWurse] Reloaded " + mods + " mods and " + cmds + " commands!");
    }

    public static void loadModule(File jar) {
        System.out.println("[EvenWurse] Loading module from JAR file: '" + jar.getName() + "'...");
        try {
            JarFile jarFile = new JarFile(jar);
            Enumeration e = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + jar.getPath() + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = (JarEntry) e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                Class c = cl.loadClass(className);
                if (Module.class.isAssignableFrom(c)) {
                    if (Mod.class.isAssignableFrom(c)) {
                        try {
                            WurstClient.INSTANCE.mods.loadMod(c, true);
                        } catch (Module.ModuleLoadException e1) {
                            ModManager.handleModuleLoadException(e1, c.getSimpleName());
                        }
                    } else if (Cmd.class.isAssignableFrom(c)) {
                        try {
                            WurstClient.INSTANCE.commands.loadCommand(c, true);
                        } catch (Module.ModuleLoadException e1) {
                            CmdManager.handleModuleLoadException(e1, c.getSimpleName());
                        }
                    } else {
                        Module module = (Module) c.getConstructor().newInstance();
                        System.out.println("[EvenWurse] Loading misc module from class: '" + c.getSimpleName() + "'...");
                        try {
                            module.onLoad();
                        } catch(Throwable t) {
                            System.out.println("[EvenWurse] Module in class '" + c.getSimpleName() + "' threw exception in onLoad(), skipping!");
                            t.printStackTrace();
                            continue;
                        }
                        miscModules.add(module);
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("[EvenWurse] Exception loading module from file: '" + jar.getName() + "'!");
            e.printStackTrace();
        }
    }
}
