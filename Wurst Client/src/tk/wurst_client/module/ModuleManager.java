/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module;

import java.util.ArrayList;

import tk.wurst_client.Client;
import tk.wurst_client.module.modules.*;

public class ModuleManager
{
	public final ArrayList<Module> activeModules = new ArrayList<Module>();
	
	public Module getModuleFromClass(Class moduleClass)
	{
		for(int i = 0; i < Client.wurst.moduleManager.activeModules.size(); i++)
			if(Client.wurst.moduleManager.activeModules.get(i).getClass()
				.getName().equals(moduleClass.getName()))
				return Client.wurst.moduleManager.activeModules.get(i);
		throw new IllegalArgumentException("There is no module called \""
			+ moduleClass.getName() + "\".");
	}
	
	public Module getModuleByName(String name)
	{
		for(int i = 0; i < Client.wurst.moduleManager.activeModules.size(); i++)
			if(Client.wurst.moduleManager.activeModules.get(i).getName()
				.equals(name))
				return Client.wurst.moduleManager.activeModules.get(i);
		return null;
	}
	
	public ModuleManager()
	{
		activeModules.add(new AntiAFK());
		activeModules.add(new AntiBlind());
		activeModules.add(new AntiKnockback());
		activeModules.add(new AntiSpam());
		activeModules.add(new ArenaBrawl());
		activeModules.add(new AutoArmor());
		activeModules.add(new AutoFish());
		activeModules.add(new AutoMine());
		activeModules.add(new AutoRespawn());
		activeModules.add(new AutoSign());
		activeModules.add(new AutoSprint());
		activeModules.add(new AutoSteal());
		activeModules.add(new AutoSwitch());
		activeModules.add(new AutoTool());
		activeModules.add(new AutoWalk());
		activeModules.add(new BaseFinder());
		activeModules.add(new Blink());
		activeModules.add(new BowAimbot());
		activeModules.add(new BuildRandom());
		activeModules.add(new BunnyHop());
		activeModules.add(new ChestESP());
		activeModules.add(new ClickGUI());
		activeModules.add(new Criticals());
		activeModules.add(new Derp());
		activeModules.add(new Dolphin());
		activeModules.add(new DropCMD());
		activeModules.add(new FastBreak());
		activeModules.add(new FastBow());
		activeModules.add(new FastEat());
		activeModules.add(new FastLadder());
		activeModules.add(new FastPlace());
		activeModules.add(new FightBot());
		activeModules.add(new Flight());
		activeModules.add(new Follow());
		activeModules.add(new ForceOP());
		activeModules.add(new Freecam());
		activeModules.add(new Fullbright());
		activeModules.add(new Glide());
		activeModules.add(new GoToCmd());
		activeModules.add(new Headless());
		activeModules.add(new HealthTags());
		activeModules.add(new HighJump());
		activeModules.add(new Home());
		activeModules.add(new InstantBunker());
		activeModules.add(new Invisibility());
		activeModules.add(new ItemESP());
		activeModules.add(new Jesus());
		activeModules.add(new Jetpack());
		activeModules.add(new Kaboom());
		activeModules.add(new Killaura());
		activeModules.add(new KillauraLegit());
		activeModules.add(new Liquids());
		activeModules.add(new LSD());
		activeModules.add(new MassTPA());
		activeModules.add(new MileyCyrus());
		activeModules.add(new MobESP());
		activeModules.add(new MultiAura());
		activeModules.add(new NameProtect());
		activeModules.add(new NameTags());
		activeModules.add(new NoFall());
		activeModules.add(new NoHurtcam());
		activeModules.add(new NoSlowdown());
		activeModules.add(new NoWeb());
		activeModules.add(new Nuker());
		activeModules.add(new NukerLegit());
		activeModules.add(new Overlay());
		activeModules.add(new Panic());
		activeModules.add(new Phase());
		activeModules.add(new PlayerESP());
		activeModules.add(new PlayerFinder());
		activeModules.add(new ProphuntESP());
		activeModules.add(new Protect());
		activeModules.add(new Pwnage());
		activeModules.add(new Regen());
		activeModules.add(new RemoteView());
		activeModules.add(new Search());
		activeModules.add(new Sneak());
		activeModules.add(new Spammer());
		activeModules.add(new SpeedNuker());
		activeModules.add(new Spider());
		activeModules.add(new Step());
		activeModules.add(new Throw());
		activeModules.add(new Timer());
		activeModules.add(new Tracers());
		activeModules.add(new TriggerBot());
		activeModules.add(new TrueSight());
		activeModules.add(new XRay());
		activeModules.add(new YesCheat());
		activeModules.add(new AutoBuild());
	}
}
