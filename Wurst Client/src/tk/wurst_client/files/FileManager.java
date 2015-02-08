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
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

import org.darkstorm.minecraft.gui.component.Frame;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.Client;
import tk.wurst_client.alts.Alt;
import tk.wurst_client.alts.GuiAltList;
import tk.wurst_client.encryption_api.Encryption;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.module.modules.*;
import tk.wurst_client.utils.XRayUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FileManager
{
	public final File WurstDir = new File(Minecraft.getMinecraft().mcDataDir, "wurst");
	public final File SkinDir = new File(WurstDir, "skins");
	public final File ServerlistDir = new File(WurstDir, "serverlists");
	public final File SpamDir = new File(WurstDir, "spam");
	public final File Alts = new File(WurstDir, "alts.wurst");
	public final File AutoBuildCustom = new File(WurstDir, "autobuild_custom.json");
	public final File Friends = new File(WurstDir, "friends.json");
	public final File GUI = new File(WurstDir, "gui.json");
	public final File Modules = new File(WurstDir, "modules.json");
	public final File Sliders = new File(WurstDir, "sliders.json");
	public final File Values = new File(WurstDir, "values.json");
	public final File AutoMaximizeFile = new File(Minecraft.getMinecraft().mcDataDir + "/wurst/automaximize.txt");
	public final File XRay = new File(WurstDir, "xray.json");
	private String split = "§";
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public void init()
	{
		if(!WurstDir.exists())
			WurstDir.mkdir();
		if(!SkinDir.exists())
			SkinDir.mkdir();
		if(!ServerlistDir.exists())
			ServerlistDir.mkdir();
		if(!SpamDir.exists())
			SpamDir.mkdir();
		if(!Values.exists())
			saveOptions();
		else
			loadOptions();
		if(!Modules.exists())
			saveModules();
		else
			loadModules();
		if(!Alts.exists())
			saveAlts();
		else
			loadAlts();
		if(!Friends.exists())
			saveFriends();
		else
			loadFriends();
		if(!XRay.exists())
		{
			XRayUtils.initXRayBlocks();
			saveXRayBlocks();
		}
		else
			loadXRayBlocks();
		loadBuildings();
	}

	public void saveGUI(Frame[] frames)
	{
		try
		{
			PrintWriter save = new PrintWriter(new FileWriter(GUI));
			JsonArray json = new JsonArray();
			for(Frame frame : frames)
			{
				if(!frame.getTitle().equalsIgnoreCase("ArenaBrawl"))
				{
					JsonObject jsonFrame = new JsonObject();
					jsonFrame.addProperty("title", frame.getTitle());
					jsonFrame.addProperty("minimized", frame.isMinimized());
					jsonFrame.addProperty("pinned", frame.isPinned());
					jsonFrame.addProperty("posX", frame.getX());
					jsonFrame.addProperty("posY", frame.getY());
					json.add(jsonFrame);
				}
			}
			save.print(gson.toJson(json));
			save.close();
		}catch(IOException e)
		{	
			
		}
	}

	public void loadGUI(Frame[] frames)
	{
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(GUI));
			JsonArray json = (JsonArray)new JsonParser().parse(load);
			load.close();
			Iterator<JsonElement> itr = json.iterator();
			while(itr.hasNext())
			{
				JsonObject jsonFrame = (JsonObject)itr.next();
				System.out.println(jsonFrame.get("title").getAsString());
				for(Frame frame : frames)
				{
					if(frame.getTitle().equals(jsonFrame.get("title").getAsString()))
					{
						frame.setMinimized(jsonFrame.get("minimized").getAsBoolean());
						frame.setPinned(jsonFrame.get("pinned").getAsBoolean());
						frame.setX(jsonFrame.get("posX").getAsInt());
						frame.setY(jsonFrame.get("posY").getAsInt());
					}
				}
				
			}
		}catch(IOException e)
		{	
			
		}
	}

	public void saveModules()
	{
		try
		{
			PrintWriter save = new PrintWriter(new FileWriter(Modules));
			for(int i = 0; i < Client.Wurst.moduleManager.activeModules.size(); i++)
			{
				Module module = Client.Wurst.moduleManager.activeModules.get(i);
				save.println(module.getName() + split + module.getToggled() + split + module.getBind());
			}
			save.close();
		}catch(IOException e)
		{	
			
		}
	}

	private String[] moduleBlacklist =
	{
		ForceOP.class.getName(),
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
		boolean shouldUpdate = false;
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(Modules));
			int i = 0;
			for(; (load.readLine()) != null;)
				i++;
			load.close();
			if(i != Client.Wurst.moduleManager.activeModules.size())
				shouldUpdate = true;
		}catch(IOException e)
		{	
			
		}
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(Modules));
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split(split);
				Module module = Client.Wurst.moduleManager.getModuleByName(data[0]);
				if(module == null || data.length != 3)
				{
					if(data.length != 0)
						shouldUpdate = true;
					continue;
				}
				if(module.getCategory() != Category.HIDDEN && module.getCategory() != Category.WIP)
				{
					boolean shouldSkip = false;
					for(String element : moduleBlacklist)
						if(module.getClass().getName().equalsIgnoreCase(element))
						{
							shouldSkip = true;
							break;
						}
					if(module.getToggled() != Boolean.valueOf(data[1]) && !shouldSkip)
						module.setToggled(Boolean.valueOf(data[1]));
				}
				if(module.getBind() != Integer.valueOf(data[2]))
					module.setBind(Integer.valueOf(data[2]));
			}
			load.close();
			if(shouldUpdate)
				saveModules();
		}catch(IOException e)
		{	
			
		}
	}

	public void saveOptions()
	{
		try
		{
			PrintWriter save = new PrintWriter(new FileWriter(Values));
			for(Field field : Client.Wurst.options.getClass().getFields())
				try
			{
					if(field.getType().getName().equals("boolean"))
						save.println(field.getName() + split + field.getBoolean(Client.Wurst.options));
					else if(field.getType().getName().equals("int"))
						save.println(field.getName() + split + field.getInt(Client.Wurst.options));
					else if(field.getType().getName().equals("java.lang.String"))
						save.println(field.getName() + split + (String)field.get(Client.Wurst.options));
			}catch(IllegalArgumentException e)
			{
				e.printStackTrace();
			}catch(IllegalAccessException e)
			{
				e.printStackTrace();
			}
			save.close();
		}catch(IOException e)
		{	
			
		}
	}

	public void loadOptions()
	{
		boolean shouldUpdate = false;
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(Values));
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split(split);
				for(Field field : Client.Wurst.options.getClass().getFields())
					if(data[0].equals(field.getName()))
					{
						try
						{
							if(field.getType().getName().equals("boolean"))
								field.setBoolean(Client.Wurst.options, Boolean.valueOf(data[1]));
							else if(field.getType().getName().equals("int"))
								field.setInt(Client.Wurst.options, Integer.valueOf(data[1]));
							else if(field.getType().getName().equals("java.lang.String"))
								field.set(Client.Wurst.options, data[1]);
							else
								shouldUpdate = true;
						}catch(IllegalArgumentException e)
						{
							shouldUpdate = true;
							e.printStackTrace();
						}catch(IllegalAccessException e)
						{
							shouldUpdate = true;
							e.printStackTrace();
						}
						break;
					}
			}
			load.close();
		}catch(IOException e)
		{	
			
		}
		if(shouldUpdate)
			saveOptions();
	}
	
	public boolean loadAutoResize()
	{
		boolean autoMaximizeEnabled = false;
		if(!AutoMaximizeFile.exists())
			saveAutoMaximize(true);
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(AutoMaximizeFile));
			String line = load.readLine();
			load.close();
			autoMaximizeEnabled = line.equals("true") && !Minecraft.isRunningOnMac;
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return autoMaximizeEnabled;
	}
	
	public void saveAutoMaximize(boolean autoMaximizeEnabled)
	{
		try
		{
			if(!AutoMaximizeFile.getParentFile().exists())
				AutoMaximizeFile.getParentFile().mkdirs();
			PrintWriter save = new PrintWriter(new FileWriter(AutoMaximizeFile));
			save.println(Boolean.toString(autoMaximizeEnabled));
			save.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void saveSliders()
	{
		ArrayList<BasicSlider> allSliders = new ArrayList<BasicSlider>();
		for(Module module : Client.Wurst.moduleManager.activeModules)
			for(BasicSlider slider : module.getSliders())
				allSliders.add(slider);
		try
		{
			PrintWriter save = new PrintWriter(new FileWriter(Sliders));
			for(int i = 0; i < allSliders.size(); i++)
			{
				BasicSlider slider = allSliders.get(i);
				save.println(i + split + (double)(Math.round(slider.getValue() / slider.getIncrement()) * 1000000 * (long)(slider.getIncrement() * 1000000)) / 1000000 / 1000000);
			}
			save.close();
		}catch(IOException e)
		{	
			
		}
	}

	public void loadSliders()
	{
		ArrayList<BasicSlider> allSliders = new ArrayList<BasicSlider>();
		for(Module module : Client.Wurst.moduleManager.activeModules)
			for(BasicSlider slider : module.getSliders())
				allSliders.add(slider);
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(Sliders));
			int i = 0;
			for(; (load.readLine()) != null;)
				i++;
			load.close();
			if(i != allSliders.size())
			{
				saveSliders();
				return;
			}
		}catch(IOException e)
		{	
			
		}
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(Sliders));
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split(split);
				allSliders.get(Integer.valueOf(data[0])).setValue(Double.valueOf(data[1]));
			}
			load.close();
		}catch(IOException e)
		{	
			
		}
	}

	public void saveAlts()
	{
		try
		{
			PrintWriter save = new PrintWriter(new FileWriter(Alts));
			for(Alt alt : GuiAltList.alts)
			{
				String saveName = Encryption.encrypt(alt.name);
				String savePassword = Encryption.encrypt(alt.password);
				save.println(saveName + split + savePassword);
			}
			save.close();
		}catch(IOException e)
		{	
			
		}
	}

	public void loadAlts()
	{
		if(!Alts.exists())
		{
			saveAlts();
			return;
		}
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(Alts));
			GuiAltList.alts.clear();
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split(split);
				String name = Encryption.decrypt(data[0]);
				String password = "";
				if(data.length == 2)
					password = Encryption.decrypt(data[1]);
				GuiAltList.alts.add(new Alt(name, password));
			}
			GuiAltList.sortAlts();
			load.close();
		}catch(IOException e)
		{	
			
		}
	}

	public void saveFriends()
	{
		Client.Wurst.options.sortFriends();
		try
		{
			PrintWriter save = new PrintWriter(new FileWriter(Friends));
			for(int i = 0; i < Client.Wurst.options.friends.size(); i++)
				save.println(Client.Wurst.options.friends.get(i));
			save.close();
		}catch(IOException e)
		{	
			
		}
	}

	public void loadFriends()
	{
		boolean shouldUpdate = false;
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(Friends));
			int i = 0;
			for(; (load.readLine()) != null;)
				i++;
			load.close();
			if(i != 1)
				shouldUpdate = true;
		}catch(IOException e)
		{	
			
		}
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(Friends));
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split(split);
				Client.Wurst.options.friends.add(data[0]);
			}
			load.close();
			Client.Wurst.options.sortFriends();
		}catch(IOException e)
		{	
			
		}
		if(shouldUpdate)
			saveFriends();
	}

	public void saveXRayBlocks()
	{
		try
		{
			PrintWriter save = new PrintWriter(new FileWriter(XRay));
			for(int i = 0; i < tk.wurst_client.module.modules.XRay.xrayBlocks.size(); i++)
				save.println(Block.getIdFromBlock(tk.wurst_client.module.modules.XRay.xrayBlocks.get(i)));
			save.close();
		}catch(IOException e)
		{	
			
		}
	}

	public void loadXRayBlocks()
	{
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(XRay));
			for(String line = ""; (line = load.readLine()) != null;)
			{
				String data[] = line.split(split);
				tk.wurst_client.module.modules.XRay.xrayBlocks.add(Block.getBlockById(Integer.valueOf(data[0])));
			}
			load.close();
		}catch(IOException e)
		{	
			
		}
	}

	public void loadBuildings()
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
		if(!Client.Wurst.fileManager.AutoBuildCustom.exists())
			try
		{
				PrintWriter save = new PrintWriter(new FileWriter(Client.Wurst.fileManager.AutoBuildCustom));
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
		}catch(IOException e)
			{}
		ArrayList<String> fileText = new ArrayList<String>();
		try
		{
			BufferedReader load = new BufferedReader(new FileReader(AutoBuildCustom));
			for(String line = ""; (line = load.readLine()) != null;)
				fileText.add(line);
			load.close();
		}catch(IOException e)
		{}
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
