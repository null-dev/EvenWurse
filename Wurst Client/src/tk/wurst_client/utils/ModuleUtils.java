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
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Project: EvenWurse
 * Created: 20/12/15
 * Author: nulldev
 */
public class ModuleUtils {
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
                            WurstClient.INSTANCE.mods.loadMod(c);
                        } catch (Module.ModuleLoadException e1) {
                            ModManager.handleModuleLoadException(e1, c.getSimpleName());
                        }
                    } else if (Cmd.class.isAssignableFrom(c)) {
                        try {
                            WurstClient.INSTANCE.commands.loadCommand(c);
                        } catch (Module.ModuleLoadException e1) {
                            CmdManager.handleModuleLoadException(e1, c.getSimpleName());
                        }
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("[EvenWurse] Exception loading module from file: '" + jar.getName() + "'!");
            e.printStackTrace();
        }
    }
}
