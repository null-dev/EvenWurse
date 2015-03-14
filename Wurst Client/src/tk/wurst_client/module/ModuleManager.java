/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import tk.wurst_client.Client;
import tk.wurst_client.module.modules.*;

public class ModuleManager
{
	@Deprecated
	public final ArrayList<Module> activeModules = new ArrayList<Module>();
	private final TreeMap<Class, Module> mods = new TreeMap<Class, Module>();
	
	public ModuleManager()
	{
		addMod(new AntiAFK());
		addMod(new AntiBlind());
		addMod(new AntiKnockback());
		addMod(new AntiSpam());
		addMod(new ArenaBrawl());
		addMod(new AutoArmor());
		addMod(new AutoFish());
		addMod(new AutoMine());
		addMod(new AutoRespawn());
		addMod(new AutoSign());
		addMod(new AutoSprint());
		addMod(new AutoSteal());
		addMod(new AutoSwitch());
		addMod(new AutoTool());
		addMod(new AutoWalk());
		addMod(new BaseFinder());
		addMod(new Blink());
		addMod(new BowAimbot());
		addMod(new BuildRandom());
		addMod(new BunnyHop());
		addMod(new ChestESP());
		addMod(new ClickGUI());
		addMod(new Criticals());
		addMod(new Derp());
		addMod(new Dolphin());
		addMod(new DropCMD());
		addMod(new FastBreak());
		addMod(new FastBow());
		addMod(new FastEat());
		addMod(new FastLadder());
		addMod(new FastPlace());
		addMod(new FightBot());
		addMod(new Flight());
		addMod(new Follow());
		addMod(new ForceOP());
		addMod(new Freecam());
		addMod(new Fullbright());
		addMod(new Glide());
		addMod(new GoToCmd());
		addMod(new Headless());
		addMod(new HealthTags());
		addMod(new HighJump());
		addMod(new Home());
		addMod(new InstantBunker());
		addMod(new Invisibility());
		addMod(new ItemESP());
		addMod(new Jesus());
		addMod(new Jetpack());
		addMod(new Kaboom());
		addMod(new Killaura());
		addMod(new KillauraLegit());
		addMod(new Liquids());
		addMod(new LSD());
		addMod(new MassTPA());
		addMod(new MileyCyrus());
		addMod(new MobESP());
		addMod(new MultiAura());
		addMod(new NameProtect());
		addMod(new NameTags());
		addMod(new NoFall());
		addMod(new NoHurtcam());
		addMod(new NoSlowdown());
		addMod(new NoWeb());
		addMod(new Nuker());
		addMod(new NukerLegit());
		addMod(new Overlay());
		addMod(new Panic());
		addMod(new Phase());
		addMod(new PlayerESP());
		addMod(new PlayerFinder());
		addMod(new ProphuntESP());
		addMod(new Protect());
		addMod(new Pwnage());
		addMod(new Regen());
		addMod(new RemoteView());
		addMod(new Search());
		addMod(new Sneak());
		addMod(new Spammer());
		addMod(new SpeedNuker());
		addMod(new Spider());
		addMod(new Step());
		addMod(new Throw());
		addMod(new Timer());
		addMod(new Tracers());
		addMod(new TriggerBot());
		addMod(new TrueSight());
		addMod(new XRay());
		addMod(new YesCheat());
		addMod(new AutoBuild());
	}
	
	public Module getMod(Class modClass)
	{
		return mods.get(modClass);
	}
	
	public void addMod(Module mod)
	{
		mods.put(mod.getClass(), mod);
	}
	
	@Deprecated
	public Module getModuleFromClass(Class moduleClass)
	{
		for(int i = 0; i < Client.wurst.moduleManager.activeModules.size(); i++)
			if(Client.wurst.moduleManager.activeModules.get(i).getClass()
				.getName().equals(moduleClass.getName()))
				return Client.wurst.moduleManager.activeModules.get(i);
		throw new IllegalArgumentException("There is no module called \""
			+ moduleClass.getName() + "\".");
	}
	
	@Deprecated
	public Module getModuleByName(String name)
	{
		for(int i = 0; i < Client.wurst.moduleManager.activeModules.size(); i++)
			if(Client.wurst.moduleManager.activeModules.get(i).getName()
				.equals(name))
				return Client.wurst.moduleManager.activeModules.get(i);
		return null;
	}
	
	public int countMods()
	{
		return mods.size();
	}
	
	public Iterator<Module> iterator()
	{
		return mods.values().iterator();
	}
}
