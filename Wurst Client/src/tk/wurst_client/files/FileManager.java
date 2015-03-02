/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.Client;
import tk.wurst_client.alts.Alt;
import tk.wurst_client.alts.Encryption;
import tk.wurst_client.gui.alts.GuiAltList;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.module.modules.*;
import tk.wurst_client.options.Friends;
import tk.wurst_client.options.Options;
import tk.wurst_client.utils.XRayUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FileManager
{
	public final File wurstDir = new File(Minecraft.getMinecraft().mcDataDir, "wurst");
	public final File autobuildDir = new File(wurstDir, "autobuild");
	public final File skinDir = new File(wurstDir, "skins");
	public final File serverlistsDir = new File(wurstDir, "serverlists");
	public final File spamDir = new File(wurstDir, "spam");
	public final File scriptsDir = new File(spamDir, "autorun");
	
	public final File alts = new File(wurstDir, "alts.json");
	public final File autoBuild_custom = new File(wurstDir, "autobuild_custom.txt");
	public final File friends = new File(wurstDir, "friends.json");
	public final File gui = new File(wurstDir, "gui.json");
	public final File modules = new File(wurstDir, "modules.json");
	public final File keybinds = new File(wurstDir, "keybinds.json");
	public final File sliders = new File(wurstDir, "sliders.json");
	public final File options = new File(wurstDir, "options.json");
	public final File autoMaximize = new File(Minecraft.getMinecraft().mcDataDir + "/wurst/automaximize.json");
	public final File xray = new File(wurstDir, "xray.json");
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	@Deprecated
	private String split = "§";
	
	public void init()
	{
		if(!wurstDir.exists())
			wurstDir.mkdir();
		if(!spamDir.exists())
			spamDir.mkdir();
		if(!scriptsDir.exists())
			scriptsDir.mkdir();
		if(!skinDir.exists())
			skinDir.mkdir();
		if(!serverlistsDir.exists())
			serverlistsDir.mkdir();
		if(!options.exists())
			saveOptions();
		else
			loadOptions();
		if(!modules.exists())
			saveModules();
		else
			loadModules();
		if(!keybinds.exists())
			saveKeybinds();
		else
			loadKeybinds();
		if(!alts.exists())
			saveAlts();
		else
			loadAlts();
		if(!friends.exists())
			saveFriends();
		else
			loadFriends();
		if(!xray.exists())
		{
			XRayUtils.initXRayBlocks();
			saveXRayBlocks();
		}
		else
			loadXRayBlocks();
		loadAutoBuildTemplates();
	}
	
	public void saveGUI(Frame[] frames)
	{
		try
		{
			JsonObject json = new JsonObject();
			for(Frame frame : frames)
				if(!frame.getTitle().equalsIgnoreCase("ArenaBrawl"))
				{
					JsonObject jsonFrame = new JsonObject();
					jsonFrame.addProperty("minimized", frame.isMinimized());
					jsonFrame.addProperty("pinned", frame.isPinned());
					jsonFrame.addProperty("posX", frame.getX());
					jsonFrame.addProperty("posY", frame.getY());
					json.add(frame.getTitle(), jsonFrame);
				}
			PrintWriter save = new PrintWriter(new FileWriter(gui));
			save.println(gson.toJson(json));
			save.close();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void loadGUI(Frame[] frames)
	{
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(gui));
			JsonObject json = (JsonObject)new JsonParser().parse(load);
			load.close();
			Iterator<Entry<String, JsonElement>> itr = json.entrySet().iterator();
			while(itr.hasNext())
			{
				Entry<String, JsonElement> entry = itr.next();
				for(Frame frame : frames)
					if(frame.getTitle().equals(entry.getKey()))
					{
						JsonObject jsonFrame = (JsonObject)entry.getValue();
						frame.setMinimized(jsonFrame.get("minimized").getAsBoolean());
						frame.setPinned(jsonFrame.get("pinned").getAsBoolean());
						frame.setX(jsonFrame.get("posX").getAsInt());
						frame.setY(jsonFrame.get("posY").getAsInt());
					}
				
			}
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void saveModules()
	{
		try
		{
			JsonObject json = new JsonObject();
			for(Module module : Client.wurst.moduleManager.activeModules)
			{
				JsonObject jsonModule = new JsonObject();
				jsonModule.addProperty("enabled", module.getToggled());
				json.add(module.getName(), jsonModule);
			}
			PrintWriter save = new PrintWriter(new FileWriter(modules));
			save.println(gson.toJson(json));
			save.close();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	private String[] moduleBlacklist =
	{
		AntiAFK.class.getName(),
		ArenaBrawl.class.getName(),
		AutoBuild.class.getName(),
		AutoSign.class.getName(),
		AnnoyCMD.class.getName(),
		FightBot.class.getName(),
		Follow.class.getName(),
		ForceOP.class.getName(),
		Freecam.class.getName(),
		Invisibility.class.getName(),
		LSD.class.getName(),
		MassTPA.class.getName(),
		Protect.class.getName(),
		Pwnage.class.getName(),
		RemoteView.class.getName(),
		Spammer.class.getName(),
	};
	
	public void loadModules()
	{
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(modules));
			JsonObject json = (JsonObject)new JsonParser().parse(load);
			load.close();
			Iterator<Entry<String, JsonElement>> itr = json.entrySet().iterator();
			while(itr.hasNext())
			{
				Entry<String, JsonElement> entry = itr.next();
				Module module = Client.wurst.moduleManager.getModuleByName(entry.getKey());
				if(module != null
					&& module.getCategory() != Category.HIDDEN
					&& module.getCategory() != Category.WIP
					&& !Arrays.asList(moduleBlacklist).contains(module.getClass().getName()))
				{
					JsonObject jsonModule = (JsonObject)entry.getValue();
					boolean enabled = jsonModule.get("enabled").getAsBoolean();
					if(module.getToggled() != enabled)
						module.setToggled(enabled);
				}
			}
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void saveKeybinds()
	{
		try
		{
			JsonObject json = new JsonObject();
			Iterator<Entry<String, String>> itr = Client.wurst.keybinds.entrySet().iterator();
			while(itr.hasNext())
			{
				Entry<String, String> entry = itr.next();
				json.addProperty(entry.getKey(), entry.getValue());
			}
			PrintWriter save = new PrintWriter(new FileWriter(keybinds));
			save.println(gson.toJson(json));
			save.close();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void loadKeybinds()
	{
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(keybinds));
			JsonObject json = (JsonObject)new JsonParser().parse(load);
			load.close();
			Client.wurst.keybinds.clear();
			Iterator<Entry<String, JsonElement>> itr = json.entrySet().iterator();
			while(itr.hasNext())
			{
				Entry<String, JsonElement> entry = itr.next();
				Client.wurst.keybinds.put(entry.getKey(), entry.getValue().getAsString());
			}
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void saveOptions()
	{
		try
		{
			PrintWriter save = new PrintWriter(new FileWriter(options));
			save.println(gson.toJson(Client.wurst.options));
			save.close();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void loadOptions()
	{
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(options));
			Client.wurst.options = gson.fromJson(load, Options.class);
			load.close();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public boolean loadAutoMaximize()
	{
		boolean autoMaximizeEnabled = false;
		if(!autoMaximize.exists())
			saveAutoMaximize(true);
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(autoMaximize));
			autoMaximizeEnabled = gson.fromJson(load, Boolean.class) && !Minecraft.isRunningOnMac;
			load.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return autoMaximizeEnabled;
	}
	
	public void saveAutoMaximize(boolean autoMaximizeEnabled)
	{
		try
		{
			if(!autoMaximize.getParentFile().exists())
				autoMaximize.getParentFile().mkdirs();
			PrintWriter save = new PrintWriter(new FileWriter(autoMaximize));
			save.println(gson.toJson(autoMaximizeEnabled));
			save.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveSliders()
	{
		try
		{
			JsonObject json = new JsonObject();
			for(Module module : Client.wurst.moduleManager.activeModules)
			{
				if(module.getSliders().isEmpty())
					continue;
				JsonObject jsonModule = new JsonObject();
				for(BasicSlider slider : module.getSliders())
					jsonModule.addProperty(slider.getText(), (double)(Math.round(slider.getValue() / slider.getIncrement()) * 1000000 * (long)(slider.getIncrement() * 1000000)) / 1000000 / 1000000);
				json.add(module.getName(), jsonModule);
			}
			PrintWriter save = new PrintWriter(new FileWriter(sliders));
			save.println(gson.toJson(json));
			save.close();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void loadSliders()
	{
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(sliders));
			JsonObject json = (JsonObject)new JsonParser().parse(load);
			load.close();
			Iterator<Entry<String, JsonElement>> itr = json.entrySet().iterator();
			while(itr.hasNext())
			{
				Entry<String, JsonElement> entry = itr.next();
				Module module = Client.wurst.moduleManager.getModuleByName(entry.getKey());
				if(module != null)
				{
					JsonObject jsonModule = (JsonObject)entry.getValue();
					for(BasicSlider slider : module.getSliders())
						try
						{
							slider.setValue(jsonModule.get(slider.getText()).getAsDouble());
						}catch(Exception e)
						{	
							e.printStackTrace();
						}
				}
			}
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void saveAlts()
	{
		try
		{
			JsonObject json = new JsonObject();
			for(Alt alt : GuiAltList.alts)
			{
				JsonObject jsonAlt = new JsonObject();
				jsonAlt.addProperty("name", alt.getName());
				jsonAlt.addProperty("password", alt.getPassword());
				jsonAlt.addProperty("cracked", alt.isCracked());
				jsonAlt.addProperty("starred", alt.isStarred());
				json.add(alt.getEmail(), jsonAlt);
			}
			Files.write(alts.toPath(), Encryption.encrypt(gson.toJson(json)).getBytes(Encryption.CHARSET));
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void loadAlts()
	{
		try
		{
			JsonObject json = (JsonObject)new JsonParser().parse(Encryption.decrypt(new String(
				Files.readAllBytes(alts.toPath()), Encryption.CHARSET)));
			GuiAltList.alts.clear();
			Iterator<Entry<String, JsonElement>> itr = json.entrySet().iterator();
			while(itr.hasNext())
			{
				Entry<String, JsonElement> entry = itr.next();
				JsonObject jsonAlt = entry.getValue().getAsJsonObject();
				String email = entry.getKey();
				String name = jsonAlt.get("name") == null ? "" : jsonAlt.get("name").getAsString();
				String password = jsonAlt.get("password") == null ? "" : jsonAlt.get("password").getAsString();
				boolean cracked = jsonAlt.get("cracked") == null ? true : jsonAlt.get("cracked").getAsBoolean();
				boolean starred = jsonAlt.get("starred") == null ? false : jsonAlt.get("starred").getAsBoolean();
				if(cracked)
					GuiAltList.alts.add(new Alt(email, starred));
				else
					GuiAltList.alts.add(new Alt(email, name, password, starred));
			}
			GuiAltList.sortAlts();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void saveFriends()
	{
		try
		{
			Client.wurst.friends.sort();
			PrintWriter save = new PrintWriter(new FileWriter(friends));
			save.println(gson.toJson(Client.wurst.friends));
			save.close();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void loadFriends()
	{
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(friends));
			Client.wurst.friends = gson.fromJson(load, Friends.class);
			load.close();
			Client.wurst.friends.sort();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void saveXRayBlocks()
	{
		try
		{
			XRayUtils.sortBlocks();
			JsonArray json = new JsonArray();
			for(int i = 0; i < XRay.xrayBlocks.size(); i++)
				json.add(gson.toJsonTree(Block.getIdFromBlock(XRay.xrayBlocks.get(i))));
			PrintWriter save = new PrintWriter(new FileWriter(xray));
			save.println(gson.toJson(json));
			save.close();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void loadXRayBlocks()
	{
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(xray));
			JsonArray json = new JsonParser().parse(load).getAsJsonArray();
			load.close();
			Iterator<JsonElement> itr = json.iterator();
			while(itr.hasNext())
				try
				{
					String jsonBlock = itr.next().getAsString();
					XRay.xrayBlocks.add(Block.getBlockFromName(jsonBlock));
				}catch(Exception e)
				{	
					e.printStackTrace();
				}
			XRayUtils.sortBlocks();
		}catch(Exception e)
		{	
			e.printStackTrace();
		}
	}
	
	public void loadAutoBuildTemplates()
	{
		int[][] bridge =
		{
			{0, 1, 0, 5},
			{0, 1, 0, 4},
			{0, 1, 0, 2},
			{0, 1, -1, 5},
			{0, 1, -1, 4},
			{0, 1, -1, 2},
			{0, 1, -2, 5},
			{0, 1, -2, 4},
			{0, 1, -2, 2},
			{0, 1, -3, 5},
			{0, 1, -3, 4},
			{0, 1, -3, 2},
			{0, 1, -4, 5},
			{0, 1, -4, 4},
			{0, 1, -4, 2},
			{0, 1, -5, 5},
			{0, 1, -5, 4},
		};
		AutoBuild.buildings.add(bridge);
		int[][] floor =
		{
			{0, 0, 0},
			{1, 0, 1},
			{0, 0, 1},
			{-1, 0, 1},
			{1, 0, 0},
			{-1, 0, 0},
			{1, 0, -1},
			{0, 0, -1},
			{-1, 0, -1},
			{2, 0, 2},
			{1, 0, 2},
			{0, 0, 2},
			{-1, 0, 2},
			{-2, 0, 2},
			{2, 0, 1},
			{-2, 0, 1},
			{2, 0, 0},
			{-2, 0, 0},
			{2, 0, -1},
			{-2, 0, -1},
			{2, 0, -2},
			{1, 0, -2},
			{0, 0, -2},
			{-1, 0, -2},
			{-2, 0, -2},
			{3, 0, 3},
			{2, 0, 3},
			{1, 0, 3},
			{0, 0, 3},
			{-1, 0, 3},
			{-2, 0, 3},
			{-3, 0, 3},
			{3, 0, 2},
			{-3, 0, 2},
			{3, 0, 1},
			{-3, 0, 1},
			{3, 0, 0},
			{-3, 0, 0},
			{3, 0, -1},
			{-3, 0, -1},
			{3, 0, -2},
			{-3, 0, -2},
			{3, 0, -3},
			{2, 0, -3},
			{1, 0, -3},
			{0, 0, -3},
			{-1, 0, -3},
			{-2, 0, -3},
			{-3, 0, -3},
		};
		AutoBuild.buildings.add(floor);
		int[][] nazi =
		{
			{0, 1, 0, 5},
			{1, 1, 0, 5},
			{0, 1, 0, 1},
			{0, 2, 0, 1},
			{0, 3, 0, 5},
			{0, 3, 0, 4},
			{1, 3, 0, 5},
			{-1, 3, 0, 4},
			{-2, 3, 0, 0},
			{-2, 2, 0, 0},
			{0, 3, 0, 1},
			{2, 3, 0, 1},
			{2, 4, 0, 1},
			{0, 4, 0, 1},
			{0, 5, 0, 4},
			{-1, 5, 0, 4},
		};
		AutoBuild.buildings.add(nazi);
		int[][] penis =
		{
			{1, 0, 0, 1},
			{1, 0, 1, 1},
			{0, 0, 1, 1},
			{0, 1, 0, 1},
			{1, 1, 0, 1},
			{1, 1, 1, 1},
			{0, 1, 1, 1},
			{0, 2, 0, 1},
			{1, 2, 0, 1},
			{1, 2, 1, 1},
			{0, 2, 1, 1},
			{0, 3, 0, 1},
			{1, 3, 0, 1},
			{1, 3, 1, 1},
			{0, 3, 1, 1},
			{0, 4, 0, 1},
			{1, 4, 0, 1},
			{1, 4, 1, 1},
			{0, 4, 1, 1},
			{0, 5, 0, 1},
			{1, 5, 0, 1},
			{1, 5, 1, 1},
			{0, 5, 1, 1},
			{0, 6, 0, 1},
			{1, 6, 0, 1},
			{1, 6, 1, 1},
			{0, 6, 1, 1},
			{0, 7, 0, 1},
			{1, 7, 0, 1},
			{1, 7, 1, 1},
			{0, 7, 1, 1},
			{2, 0, -1, 1},
			{-1, 0, -1, 1},
			{3, 0, -1, 1},
			{-2, 0, -1, 1},
			{3, 0, -2, 1},
			{-2, 0, -2, 1},
			{2, 0, -2, 1},
			{-1, 0, -2, 1},
			{2, 1, -1, 1},
			{-1, 1, -1, 1},
			{3, 1, -1, 1},
			{-2, 1, -1, 1},
			{3, 1, -2, 1},
			{-2, 1, -2, 1},
			{2, 1, -2, 1},
			{-1, 1, -2, 1},
		};
		AutoBuild.buildings.add(penis);
		int[][] pillar =
		{
			{0, 0, 0},
			{0, 1, 0},
			{0, 2, 0},
			{0, 3, 0},
			{0, 4, 0},
			{0, 5, 0},
			{0, 6, 0},
		};
		AutoBuild.buildings.add(pillar);
		int[][] wall =
		{
			{3, 0, 0},
			{2, 0, 0},
			{1, 0, 0},
			{0, 0, 0},
			{-1, 0, 0},
			{-2, 0, 0},
			{-3, 0, 0},
			{3, 1, 0},
			{2, 1, 0},
			{1, 1, 0},
			{0, 1, 0},
			{-1, 1, 0},
			{-2, 1, 0},
			{-3, 1, 0},
			{3, 2, 0},
			{2, 2, 0},
			{1, 2, 0},
			{0, 2, 0},
			{-1, 2, 0},
			{-2, 2, 0},
			{-3, 2, 0},
			{3, 3, 0},
			{2, 3, 0},
			{1, 3, 0},
			{0, 3, 0},
			{-1, 3, 0},
			{-2, 3, 0},
			{-3, 3, 0},
			{3, 4, 0},
			{2, 4, 0},
			{1, 4, 0},
			{0, 4, 0},
			{-1, 4, 0},
			{-2, 4, 0},
			{-3, 4, 0},
			{3, 5, 0},
			{2, 5, 0},
			{1, 5, 0},
			{0, 5, 0},
			{-1, 5, 0},
			{-2, 5, 0},
			{-3, 5, 0},
			{3, 6, 0},
			{2, 6, 0},
			{1, 6, 0},
			{0, 6, 0},
			{-1, 6, 0},
			{-2, 6, 0},
			{-3, 6, 0},
		};
		AutoBuild.buildings.add(wall);
		int[][] wurst =
		{
			{0, 1, 0, 5},
			{1, 1, 0, 5},
			{0, 1, 0, 4},
			{-1, 1, 0, 4},
			{0, 1, 0, 1},
			{1, 1, 0, 1},
			{-1, 1, 0, 1},
			{2, 1, 0, 1},
			{-2, 1, 0, 1},
			{2, 2, 0, 5},
			{-2, 2, 0, 4},
			{0, 2, 0, 3},
			{1, 2, 0, 3},
			{-1, 2, 0, 3},
			{2, 2, 0, 3},
			{-2, 2, 0, 3},
			{0, 2, 0, 2},
			{1, 2, 0, 2},
			{-1, 2, 0, 2},
			{2, 2, 0, 2},
			{-2, 2, 0, 2},
			{0, 2, 0, 1},
			{1, 2, 0, 1},
			{-1, 2, 0, 1},
			{2, 2, 0, 1},
			{-2, 2, 0, 1},
		};
		AutoBuild.buildings.add(wurst);
		if(!Client.wurst.fileManager.autoBuild_custom.exists())
			try
			{
				PrintWriter save = new PrintWriter(new FileWriter(Client.wurst.fileManager.autoBuild_custom));
				save.println("WARNING! This is complicated!");
				save.println("");
				save.println("How to make a custom structure for AutoBuild:");
				save.println("There are two types of structures: simple structures and advanced structures.");
				save.println("The difference is that advanced strcutures place blocks in certain directions,");
				save.println("while simple structures use the direction that you placed the first block in");
				save.println("for all their blocks. To make your structure, you need to add a line for every");
				save.println("block. In a simple structure, these lines look like this:");
				save.println("0§0§0");
				save.println("The first number determines how far the block is moved to the right or left. 0 is");
				save.println("where you placed the first");
				save.println("block, 1 is one block further on the left and -1 is one block further on the right.");
				save.println("The second number determines how far the block is moved up or down. 0 is where you placed");
				save.println("the first block, 1 is one block above and -1 is one block below.");
				save.println("The third number determines how far the block is moved to the front/rear.");
				save.println("0 is where you placed the first block, 1 is one block further away from you");
				save.println("and -1 is one block closer to you.");
				save.println("All simple structures have to start with 0§0§0. This will place the first block where you");
				save.println("clicked.");
				save.println("In an advanced structure, the lines look like this:");
				save.println("0§1§0§2");
				save.println("The first value and the third value work the same way they work in simple structures.");
				save.println("For the second value, 1 is where you placed the first block, 2 is above and 0 is below.");
				save.println("The fourth value determines the direction the block will be placed in.");
				save.println("0 = bottom, 1 = top, 2 = front, 3 = back, 4 = right, 5 = left");
				save.println("In the above example (0§1§0§2), a block is placed in front of the first block you placed.");
				save.println("The first value is 0, the second one is 1 and the third one is 0, meaning it will be placed");
				save.println("on the first block you placed.");
				save.println("The fourth value is 2 (front), meaning it will be placed against the front face of the first");
				save.println("block you placed. So in the end, it will be placed in front of that block.");
				save.println("It's like clicking on the front face of that block.");
				save.println("Advanced structures do NOT start by placing the first block where you clicked.");
				save.println("");
				save.println("Tips:");
				save.println("-Do not mix simple and advanced structures. Only choose one.");
				save.println("-Your structure should not contain more than 64 blocks and should not be bigger than 8x8x8.");
				save.println("-Make sure you don't try to place blocks against air blocks. That will only work in");
				save.println(" Singleplayer.");
				save.println("");
				save.println("Example for a simple structure (pillar):");
				save.println("0§0§0");
				save.println("0§1§0");
				save.println("0§2§0");
				save.println("0§3§0");
				save.println("0§4§0");
				save.println("0§5§0");
				save.println("0§6§0");
				save.println("");
				save.println("Example for an advanced structure (swastika):");
				save.println("0§1§0§5");
				save.println("1§1§0§5");
				save.println("0§1§0§1");
				save.println("0§2§0§1");
				save.println("0§3§0§5");
				save.println("0§3§0§4");
				save.println("1§3§0§5");
				save.println("-1§3§0§4");
				save.println("-2§3§0§0");
				save.println("-2§2§0§0");
				save.println("0§3§0§1");
				save.println("2§3§0§1");
				save.println("2§4§0§1");
				save.println("0§4§0§1");
				save.println("0§5§0§4");
				save.println("-1§5§0§4");
				save.println("");
				save.println("Make your own structure here:");
				save.println("0§0§0");
				save.println("1§0§0");
				save.println("0§0§1");
				save.println("-1§0§0");
				save.println("0§0§-1");
				save.println("0§1§0");
				save.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		ArrayList<String> fileText = new ArrayList<String>();
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(autoBuild_custom));
			for(String line = ""; (line = load.readLine()) != null;)
				fileText.add(line);
			load.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		ArrayList<String> buildingText = (ArrayList<String>)fileText.clone();
		for(int i = 0; i < fileText.size(); i++)// Removes all the text before
		// "Make your own structure here:".
		{
			if(fileText.get(i).contains("Make your own structure here:"))
				break;
			buildingText.remove(0);
		}
		buildingText.remove(0);// Removes "Make your own structure here:".
		ArrayList<int[]> loadedBuilding = new ArrayList<int[]>();
		for(int i = 0; i < buildingText.size(); i++)
		{
			String data[] = buildingText.get(i).split(split);
			int[] block = new int[data.length];
			for(int i2 = 0; i2 < data.length; i2++)
				block[i2] = Integer.valueOf(data[i2]);
			loadedBuilding.add(block);
		}
		int[][] custom = new int[loadedBuilding.size()][loadedBuilding.get(0).length];
		custom = loadedBuilding.toArray(custom);
		AutoBuild.buildings.add(custom);
	}
}
