/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Module;
import tk.wurst_client.commands.Cmd.SyntaxError;
import tk.wurst_client.events.ChatOutputEvent;
import tk.wurst_client.events.listeners.ChatOutputListener;
import tk.wurst_client.utils.F;
import tk.wurst_client.utils.ModuleUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CmdManager implements ChatOutputListener {
    public static Class<? extends Cmd>[] KNOWN_CMDS = null;
    private final TreeMap<String, Cmd> cmds = new TreeMap<>(String::compareToIgnoreCase);
    private final HashMap<Class<? extends Cmd>, Cmd> cmdClasses = new HashMap<>();
    ArrayList<Cmd> customCommands = new ArrayList<>();

    public CmdManager() {
        //Scan for cmds
        CmdManager.scanForCommands();
        //Load all cmds into memory
        cmds.clear();
        cmdClasses.clear();
        loadAllCommands();
    }

    public static void handleModuleLoadException(Module.ModuleLoadException e, String name) {
        if (e instanceof Module.InvalidVersionException) {
            Module.InvalidVersionException e1 = (Module.InvalidVersionException) e;
            System.out.println(
                    "[EvenWurse] Error loading command: '" + e1.getName() + "'! The current EvenWurse version (" +
                            WurstClient.EW_VERSION_CODE + ") is not inside range: " + e1.getMinVersion() + " - " +
                            e1.getMaxVersion() + ". Please update EvenWurse and/or the command to use it!");
        } else {
            System.out.println("[EvenWurse] Exception loading command: '" + name + "', skipping!");
            e.printStackTrace();
        }
    }

    public static void scanForCommands() {
        //Populate all the cmds
        System.out.println("[EvenWurse] Reloading cmd list...");
        ArrayList<ClassLoader> classLoadersList = new ArrayList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setScanners(new SubTypesScanner(), new ResourcesScanner()).setUrls(
                        ClasspathHelper
                                .forClassLoader(classLoadersList.toArray(new ClassLoader[classLoadersList.size()]))));
        Set<Class<? extends Cmd>> classes = reflections.getSubTypesOf(Cmd.class);
        KNOWN_CMDS = new Class[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            KNOWN_CMDS[i] = (Class<? extends Cmd>) classes.toArray()[i];
            System.out.println("[EvenWurse] Found cmd: " + KNOWN_CMDS[i].getSimpleName() + "!");
        }
        System.out.println("[EvenWurse] Found " + KNOWN_CMDS.length + " cmds!");
    }

    @Override
    public void onSentMessage(ChatOutputEvent event) {
        String message = event.getMessage();
        if (message.startsWith(".")) {
            event.cancel();
            boolean inDoubleQuotes = false;
            boolean nextCharEscaped = false;
            String input = message.substring(1);
            String commandName = input.split(" ")[0];

            Cmd cmd = getCommandByName(commandName);
            if (cmd == null) {
                WurstClient.INSTANCE.chat.error("\"." + commandName + "\" is not a valid command.");
                return;
            }

            ArrayList<String> args = new ArrayList<>();
            StringBuilder curArg = new StringBuilder();
            if (input.contains(" ")) {
                for (char c : input.substring(input.indexOf(" ") + 1).toCharArray()) {
                    if (!nextCharEscaped) {
                        if (c == '\\') {
                            nextCharEscaped = true;
                        } else if (c == '"') {
                            inDoubleQuotes = !inDoubleQuotes;
                        } else if (c == ' ') {
                            if (inDoubleQuotes) {
                                curArg.append(' ');
                            } else {
                                if (curArg.length() > 0) {
                                    args.add(curArg.toString());
                                    curArg.setLength(0);
                                }
                            }
                        } else {
                            curArg.append(c);
                        }
                    } else {
                        curArg.append(c);
                        nextCharEscaped = false;
                    }
                }
                if (curArg.length() > 0) {
                    args.add(curArg.toString());
                }
            }
            try {
                cmd.execute(args.toArray(new String[args.size()]));
            } catch (SyntaxError e) {
                if (e.getMessage() != null) {
                    WurstClient.INSTANCE.chat.message(F.DARK_RED + "Syntax error:" + F.RESET + " " + e.getMessage());
                } else {
                    WurstClient.INSTANCE.chat.message(F.DARK_RED + "Syntax error!" + F.RESET);
                }
                cmd.printSyntax();
            } catch (Cmd.Error e) {
                WurstClient.INSTANCE.chat.error(e.getMessage());
            } catch (Exception e) {
                WurstClient.INSTANCE.events
                        .handleException(e, cmd, "executing", "Exact input: `" + event.getMessage() + "`");
            }
        }
    }

    void loadAllCommands() {
        System.out.println("[EvenWurse] Loading commands into memory...");
        int loaded = 0;
        for (Class<? extends Cmd> CMD : KNOWN_CMDS) {
            try {
                loadCommand(CMD, false);
                loaded++;
            } catch (Module.ModuleLoadException e) {
                handleModuleLoadException(e, CMD.getSimpleName());
            }
        }
        System.out.println("[EvenWurse] Loaded " + loaded + " commands!");
    }

    public void unloadCommands(Cmd... cmdsToRemove) {
        for (Cmd cmd : cmdsToRemove) {
            try {
                cmd.onUnload();
            } catch (Throwable t) {
                System.out.println("[EvenWurse] Module in class '" + cmd.getClass().getSimpleName() +
                        "' threw exception in onUnload(), ignoring!");
            }
            removeCmdFromMaps(cmd);
            customCommands.remove(cmd);
            WurstClient.INSTANCE.navigator.getNavigatorList().remove(cmd);
        }
    }

    void removeCmdFromMaps(Cmd cmd) {
        Iterator<Map.Entry<String, Cmd>> stringEntries = cmds.entrySet().iterator();
        while (stringEntries.hasNext()) {
            if (stringEntries.next().getValue().getClass().equals(cmd.getClass())) {
                stringEntries.remove();
            }
        }
        Iterator<Map.Entry<Class<? extends Cmd>, Cmd>> classEntries = cmdClasses.entrySet().iterator();
        while (classEntries.hasNext()) {
            if (classEntries.next().getValue().getClass().equals(cmd.getClass())) {
                classEntries.remove();
            }
        }
    }

    /**
     * Load a command into memory.
     *
     * @param clazz The class of the command to load
     * @return The loaded command object
     * @throws Module.ModuleLoadException Failed to load command
     */
    public Cmd loadCommand(Class<? extends Cmd> clazz, boolean custom) throws Module.ModuleLoadException {
        System.out.println("[EvenWurse] Loading cmd from class: " + clazz.getSimpleName());
        Cmd cmd;
        try {
            cmd = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException
                e) {
            throw new Module.ModuleLoadException("Unknown error loading cmd!", e);
        }
        //Don't load cmds that require a higher version than us
        ModuleUtils.validateModule(cmd);
        //TODO Better way to do this
        //We have to put this here or cmds can't use the configuration in their onload method :/
        if (cmds.containsKey(cmd.getCmdName())) {
            System.out.println("[EvenWurse] Command '" + cmd.getCmdName() +
                    "' attempted to register it's name but failed as a command has already registered this " +
                    "name/alias!");
        } else {
            cmds.put(cmd.getCmdName(), cmd);
        }
        for (String alias : cmd.getAliases()) {
            if (cmds.containsKey(alias)) {
                System.out.println(
                        "[EvenWurse] Command '" + cmd.getCmdName() + "' attempted to register alias '" + alias +
                                "' but failed as a command has already registered this name/alias!");
            } else {
                cmds.put(alias, cmd);
            }
        }
        WurstClient.INSTANCE.navigator.getNavigatorList().add(cmd);
        cmdClasses.put(cmd.getClass(), cmd);
        if (custom) customCommands.add(cmd);
        try {
            cmd.onLoad();
        } catch (Throwable t) {
            removeCmdFromMaps(cmd);
            customCommands.remove(cmd);
            WurstClient.INSTANCE.navigator.getNavigatorList().remove(cmd);
            throw new Module.ModuleLoadException("Module '" + cmd.getCmdName() + "' threw exception in onLoad()!", t);
        }
        return cmd;
    }

    public ArrayList<Cmd> getCustomCommands() {
        return customCommands;
    }

    public Cmd getCommandByName(String name) {
        return cmds.get(name);
    }

    public <T> T getCmdByClass(Class<T> theClass) {
        return (T) cmdClasses.get(theClass);
    }

    public Collection<Cmd> getAllCommands() {
        return cmds.values();
    }

    public int countCommands() {
        return cmds.size();
    }
}
