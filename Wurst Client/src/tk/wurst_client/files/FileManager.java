/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.files;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import tk.wurst_client.WurstClient;
import tk.wurst_client.alts.Alt;
import tk.wurst_client.alts.Encryption;
import tk.wurst_client.api.ModuleConfiguration;
import tk.wurst_client.gui.alts.GuiAltList;
import tk.wurst_client.mods.*;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.options.FriendsList;
import tk.wurst_client.options.OptionsManager;
import tk.wurst_client.utils.*;

import java.io.*;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FileManager {
    public final File wurstDir = new File(Minecraft.getMinecraft().mcDataDir,
            "wurst");
    public final File autobuildDir = new File(wurstDir, "autobuild");
    public final File skinDir = new File(wurstDir, "skins");
    public final File serverlistsDir = new File(wurstDir, "serverlists");
    public final File spamDir = new File(wurstDir, "spam");
    public final File scriptsDir = new File(spamDir, "autorun");
    public final File modulesDir = new File(wurstDir, "modules");

    public final File alts = new File(wurstDir, "alts.json");
    public final File friends = new File(wurstDir, "friends.json");
    public final File gui = new File(wurstDir, "gui.json");
    public final File modules = new File(wurstDir, "modules.json");
    public final File navigatorData = new File(wurstDir, "navigator.json");
    public final File keybinds = new File(wurstDir, "keybinds.json");
    public final File sliders = new File(wurstDir, "sliders.json");
    public final File options = new File(wurstDir, "options.json");
    public final File moduleConfig = new File(wurstDir, "moduleConfig.json");
    public final File autoMaximize = new File(wurstDir, "automaximize.json");
    public final File xray = new File(wurstDir, "xray.json");

    public void init() {
        if(!wurstDir.exists())
            wurstDir.mkdir();
        if(!autobuildDir.exists())
            autobuildDir.mkdir();
        if(!spamDir.exists())
            spamDir.mkdir();
        if(!scriptsDir.exists())
            scriptsDir.mkdir();
        if(!skinDir.exists())
            skinDir.mkdir();
        if(!serverlistsDir.exists())
            serverlistsDir.mkdir();
        if(!modulesDir.exists())
            modulesDir.mkdir();
        if(!options.exists())
            saveOptions();
        else
            loadOptions();
        if(!moduleConfig.exists())
            saveModuleConfigs();
        else
            loadModuleConfigs();
        loadModules();
        if(!modules.exists()) {
            saveMods();
        }
        else {
            loadMods();
        }
        if(!keybinds.exists()) {
            saveKeybinds();
        }
        else {
            loadKeybinds();
        }
        if(!navigatorData.exists())
            saveNavigatorData();
        else
            loadNavigatorData();
        if(!alts.exists()) {
            saveAlts();
        }
        else {
            loadAlts();
        }
        if(!friends.exists()) {
            saveFriends();
        }
        else {
            loadFriends();
        }
        if(!xray.exists()) {
            XRayUtils.initXRayBlocks();
            saveXRayBlocks();
        }else {
            loadXRayBlocks();
        }
        File[] autobuildFiles = autobuildDir.listFiles();
        if(autobuildFiles != null && autobuildFiles.length == 0) {
            createDefaultAutoBuildTemplates();
        }
        loadAutoBuildTemplates();
        if(WurstClient.INSTANCE.options.autobuildMode >= AutoBuildMod.names
                .size()) {
            WurstClient.INSTANCE.options.autobuildMode = 0;
            saveOptions();
        }
    }

    public void saveGUI(Frame[] frames) {
        try {
            JsonObject json = new JsonObject();
            for(Frame frame : frames)
                if(!frame.getTitle().equalsIgnoreCase("ArenaBrawl")) {
                    JsonObject jsonFrame = new JsonObject();
                    jsonFrame.addProperty("minimized", frame.isMinimized());
                    jsonFrame.addProperty("pinned", frame.isPinned());
                    jsonFrame.addProperty("posX", frame.getX());
                    jsonFrame.addProperty("posY", frame.getY());
                    json.add(frame.getTitle(), jsonFrame);
                }
            PrintWriter save = new PrintWriter(new FileWriter(gui));
            save.println(JsonUtils.prettyGson.toJson(json));
            save.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGUI(Frame[] frames) {
        try {
            BufferedReader load = new BufferedReader(new FileReader(gui));
            JsonObject json = (JsonObject)JsonUtils.jsonParser.parse(load);
            load.close();
            for (Entry<String, JsonElement> entry : json.entrySet()) {
                for (Frame frame : frames)
                    if (frame.getTitle().equals(entry.getKey())) {
                        JsonObject jsonFrame = (JsonObject) entry.getValue();
                        frame.setMinimized(jsonFrame.get("minimized")
                                .getAsBoolean());
                        frame.setPinned(jsonFrame.get("pinned").getAsBoolean());
                        frame.setX(jsonFrame.get("posX").getAsInt());
                        frame.setY(jsonFrame.get("posY").getAsInt());
                    }

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMods() {
        try {
            JsonObject json = new JsonObject();
            for(Mod mod : WurstClient.INSTANCE.mods.getAllMods()) {
                JsonObject jsonMod = new JsonObject();
                jsonMod.addProperty("enabled", mod.isEnabled());
                json.add(mod.getName(), jsonMod);
            }
            PrintWriter save = new PrintWriter(new FileWriter(modules));
            save.println(JsonUtils.prettyGson.toJson(json));
            save.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveModuleConfigs() {
        Map<String, ModuleConfiguration> toSaveConfigs = ModuleConfiguration.CONFIGURATION;
        //The code below prevents saving of configs for non-loaded modules, let's ignore it for now :P
//		ArrayList<Module> toSave = ModuleUtils.getAllModules();
//		toSave.stream().filter(module -> module.isUsesConfig()).forEach(module -> toSaveConfigs.put(module.getClass().getName(), ModuleConfiguration.forModule(module)));
        String ser = GsonUtils.getGson().toJson(toSaveConfigs, ModuleConfiguration.TYPE);
        try {
            IOUtils.writeToFile(ser, moduleConfig);
        } catch (IOException e) {
            System.out.println("[EvenWurse] Failed to save module configs!");
            e.printStackTrace();
        }
    }

    public void loadModuleConfigs() {
        try {
            ModuleConfiguration.CONFIGURATION = GsonUtils.getGson().fromJson(IOUtils.readFile(moduleConfig),
                    ModuleConfiguration.TYPE);
        } catch (IOException e) {
            System.out.println("[EvenWurse] Failed to read module config!");
            e.printStackTrace();
        }
    }

    private HashSet<String> modBlacklist = Sets.newHashSet(
            AntiAfkMod.class.getName(), BlinkMod.class.getName(),
            ArenaBrawlMod.class.getName(), AutoBuildMod.class.getName(),
            AutoSignMod.class.getName(), FightBotMod.class.getName(),
            FollowMod.class.getName(), ForceOpMod.class.getName(),
            FreecamMod.class.getName(), InvisibilityMod.class.getName(),
            LsdMod.class.getName(), MassTpaMod.class.getName(),
            OpSignMod.class.getName(), ProtectMod.class.getName(),
            RemoteViewMod.class.getName(), SpammerMod.class.getName());

    public boolean isModBlacklisted(Mod mod)
    {
        return modBlacklist.contains(mod.getClass().getName());
    }

    public void loadMods() {
        try {
            BufferedReader load = new BufferedReader(new FileReader(modules));
            JsonObject json = (JsonObject)JsonUtils.jsonParser.parse(load);
            load.close();
            for (Entry<String, JsonElement> entry : json.entrySet()) {
                Mod mod =
                        WurstClient.INSTANCE.mods.getModByName(entry.getKey());
                if (mod != null && mod.getCategory() != Category.HIDDEN
                        && !modBlacklist.contains(mod.getClass().getName())) {
                    JsonObject jsonModule = (JsonObject) entry.getValue();
                    boolean enabled = jsonModule.get("enabled").getAsBoolean();
                    if (enabled)
                        mod.enableOnStartup();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveKeybinds() {
        try {
            JsonObject json = new JsonObject();
            for (Entry<String, String> entry : WurstClient.INSTANCE.keybinds.entrySet()) {
                json.addProperty(entry.getKey(), entry.getValue());
            }
            PrintWriter save = new PrintWriter(new FileWriter(keybinds));
            save.println(JsonUtils.prettyGson.toJson(json));
            save.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadKeybinds() {
        try {
            BufferedReader load = new BufferedReader(new FileReader(keybinds));
            JsonObject json = (JsonObject)JsonUtils.jsonParser.parse(load);
            load.close();
            WurstClient.INSTANCE.keybinds.clear();
            for (Entry<String, JsonElement> entry : json.entrySet()) {
                WurstClient.INSTANCE.keybinds.put(entry.getKey(), entry
                        .getValue().getAsString());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveNavigatorData() {
        try {
            JsonObject json = new JsonObject();
            Iterator<Entry<String, Long>> itr =
                    WurstClient.INSTANCE.navigator.getClicksIterator();
            while(itr.hasNext()) {
                Entry<String, Long> entry = itr.next();
                json.addProperty(entry.getKey(), entry.getValue());
            }
            PrintWriter save = new PrintWriter(new FileWriter(navigatorData));
            save.println(JsonUtils.prettyGson.toJson(json));
            save.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNavigatorData() {
        try {
            BufferedReader load =
                    new BufferedReader(new FileReader(navigatorData));
            JsonObject json = (JsonObject)JsonUtils.jsonParser.parse(load);
            load.close();
            for (Entry<String, JsonElement> entry : json.entrySet()) {
                WurstClient.INSTANCE.navigator.setClicks(entry.getKey(), entry
                        .getValue().getAsLong());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveOptions() {
        try {
            PrintWriter save = new PrintWriter(new FileWriter(options));
            save.println(JsonUtils.prettyGson
                    .toJson(WurstClient.INSTANCE.options));
            save.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadOptions() {
        try {
            BufferedReader load = new BufferedReader(new FileReader(options));
            WurstClient.INSTANCE.options =
                    JsonUtils.gson.fromJson(load, OptionsManager.class);
            load.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean loadAutoMaximize() {
        boolean autoMaximizeEnabled = false;
        if(!autoMaximize.exists())
            saveAutoMaximize(true);
        try {
            BufferedReader load =
                    new BufferedReader(new FileReader(autoMaximize));
            autoMaximizeEnabled =
                    JsonUtils.gson.fromJson(load, Boolean.class)
                            && !Minecraft.isRunningOnMac;
            load.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return autoMaximizeEnabled;
    }

    public void saveAutoMaximize(boolean autoMaximizeEnabled) {
        try {
            if(!autoMaximize.getParentFile().exists())
                autoMaximize.getParentFile().mkdirs();
            PrintWriter save = new PrintWriter(new FileWriter(autoMaximize));
            save.println(JsonUtils.prettyGson.toJson(autoMaximizeEnabled));
            save.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveSliders() {
        try {
            JsonObject json = new JsonObject();
            for(Mod mod : WurstClient.INSTANCE.mods.getAllMods()) {
                if(mod.getSettings().isEmpty())
                    continue;
                JsonObject jsonModule = new JsonObject();
                mod.getSettings().stream().filter(setting -> setting instanceof BasicSlider).forEach(setting -> {
                    BasicSlider slider = (BasicSlider) setting;
                    jsonModule
                            .addProperty(
                                    slider.getText(),
                                    (double) (Math.round(slider.getValue()
                                            / slider.getIncrement()) * 1000000 * (long) (slider
                                            .getIncrement() * 1000000)) / 1000000 / 1000000);
                });
                json.add(mod.getName(), jsonModule);
            }
            PrintWriter save = new PrintWriter(new FileWriter(sliders));
            save.println(JsonUtils.prettyGson.toJson(json));
            save.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSliders() {
        try {
            BufferedReader load = new BufferedReader(new FileReader(sliders));
            JsonObject json = (JsonObject)JsonUtils.jsonParser.parse(load);
            load.close();
            for (Entry<String, JsonElement> entry : json.entrySet()) {
                Mod mod =
                        WurstClient.INSTANCE.mods.getModByName(entry.getKey());
                if (mod != null) {
                    JsonObject jsonModule = (JsonObject) entry.getValue();
                    mod.getSettings().stream().filter(setting -> setting instanceof BasicSlider).forEach(setting -> {
                        BasicSlider slider = (BasicSlider) setting;
                        try {
                            slider.setValue(jsonModule
                                    .get(slider.getText()).getAsDouble());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAlts() {
        try {
            JsonObject json = new JsonObject();
            for(Alt alt : GuiAltList.alts) {
                JsonObject jsonAlt = new JsonObject();
                jsonAlt.addProperty("name", alt.getName());
                jsonAlt.addProperty("password", alt.getPassword());
                jsonAlt.addProperty("cracked", alt.isCracked());
                jsonAlt.addProperty("starred", alt.isStarred());
                json.add(alt.getEmail(), jsonAlt);
            }
            Files.write(
                    alts.toPath(),
                    Encryption.encrypt(JsonUtils.prettyGson.toJson(json)).getBytes(
                            Encryption.CHARSET));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadModules() {
        System.out.println("[EvenWurse] Scanning modules folder for modules to load...");
        File[] listed = modulesDir.listFiles();
        if(listed == null) return;
        for(File module : listed) {
            if(module.getName().endsWith(".jar")) {
                ModuleUtils.loadModule(module);
            }
        }
    }

    public void loadAlts()
    {
        try {
            JsonObject json =
                    (JsonObject)JsonUtils.jsonParser.parse(Encryption
                            .decrypt(new String(Files.readAllBytes(alts.toPath()),
                                    Encryption.CHARSET)));
            GuiAltList.alts.clear();
            for (Entry<String, JsonElement> entry : json.entrySet()) {
                JsonObject jsonAlt = entry.getValue().getAsJsonObject();
                String email = entry.getKey();
                String name =
                        jsonAlt.get("name") == null ? "" : jsonAlt.get("name")
                                .getAsString();
                String password =
                        jsonAlt.get("password") == null ? "" : jsonAlt.get(
                                "password").getAsString();
                boolean cracked =
                        jsonAlt.get("cracked") == null || jsonAlt.get(
                                "cracked").getAsBoolean();
                boolean starred =
                        jsonAlt.get("starred") != null && jsonAlt.get(
                                "starred").getAsBoolean();
                if (cracked)
                    GuiAltList.alts.add(new Alt(email, starred));
                else
                    GuiAltList.alts
                            .add(new Alt(email, name, password, starred));
            }
            GuiAltList.sortAlts();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFriends() {
        try {
            PrintWriter save = new PrintWriter(new FileWriter(friends));
            save.println(JsonUtils.prettyGson
                    .toJson(WurstClient.INSTANCE.friends));
            save.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFriends() {
        try {
            BufferedReader load = new BufferedReader(new FileReader(friends));
            WurstClient.INSTANCE.friends =
                    JsonUtils.gson.fromJson(load, FriendsList.class);
            load.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveXRayBlocks() {
        try {
            XRayUtils.sortBlocks();
            JsonArray json = new JsonArray();
            for(int i = 0; i < XRayMod.xrayBlocks.size(); i++)
                json.add(JsonUtils.prettyGson.toJsonTree(Block
                        .getIdFromBlock(XRayMod.xrayBlocks.get(i))));
            PrintWriter save = new PrintWriter(new FileWriter(xray));
            save.println(JsonUtils.prettyGson.toJson(json));
            save.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadXRayBlocks() {
        try {
            BufferedReader load = new BufferedReader(new FileReader(xray));
            JsonArray json = JsonUtils.jsonParser.parse(load).getAsJsonArray();
            load.close();
            for (JsonElement aJson : json)
                try {
                    String jsonBlock = aJson.getAsString();
                    XRayMod.xrayBlocks.add(Block.getBlockFromName(jsonBlock));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            XRayUtils.sortBlocks();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void createDefaultAutoBuildTemplates() {
        try {
            String[] comment =
                    {
                            "Copyright 2014 - 2015 | Alexander01998 and contributors | All rights reserved.",
                            "This Source Code Form is subject to the terms of the Mozilla Public",
                            "License, v. 2.0. If a copy of the MPL was not distributed with this",
                            "file, You can obtain one at http://mozilla.org/MPL/2.0/."};
            for (Entry<String, int[][]> entry : new DefaultAutoBuildTemplates().entrySet()) {
                JsonObject json = new JsonObject();
                json.add("__comment",
                        JsonUtils.prettyGson.toJsonTree(comment, String[].class));
                json.add("blocks", JsonUtils.prettyGson.toJsonTree(
                        entry.getValue(), int[][].class));
                PrintWriter save =
                        new PrintWriter(new FileWriter(new File(autobuildDir,
                                entry.getKey() + ".json")));
                save.println(JsonUtils.prettyGson.toJson(json));
                save.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAutoBuildTemplates() {
        try {
            File[] files = autobuildDir.listFiles();
            if(files == null)
                return;
            for(File file : files) {
                BufferedReader load = new BufferedReader(new FileReader(file));
                JsonObject json = (JsonObject)JsonUtils.jsonParser.parse(load);
                load.close();
                AutoBuildMod.templates.add(JsonUtils.gson.fromJson(
                        json.get("blocks"), int[][].class));
                AutoBuildMod.names.add(file.getName().substring(0,
                        file.getName().indexOf(".json")));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
