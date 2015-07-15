/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

import tk.wurst_client.mods.Mod.Info;

public class ModManager
{
	private final TreeMap<String, Mod> mods = new TreeMap<String, Mod>(
		new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				return o1.compareToIgnoreCase(o2);
			}
		});
	
	public ModManager()
	{
		addMod(new AntiAfkMod());
		addMod(new AntiBlindMod());
		addMod(new AntiFireMod());
		addMod(new AntiKnockbackMod());
		addMod(new AntiPotionMod());
		addMod(new AntiSpamMod());
		addMod(new ArenaBrawlMod());
		addMod(new AutoArmorMod());
		addMod(new AutoLeaveMod());
		addMod(new AutoEatMod());
		addMod(new AutoFishMod());
		addMod(new AutoMineMod());
		addMod(new AutoRespawnMod());
		addMod(new AutoSignMod());
		addMod(new AutoSprintMod());
		addMod(new AutoStealMod());
		addMod(new AutoSwitchMod());
		addMod(new AutoSwordMod());
		addMod(new AutoToolMod());
		addMod(new AutoWalkMod());
		addMod(new BaseFinderMod());
		addMod(new BlinkMod());
		addMod(new BowAimbotMod());
		addMod(new BuildRandomMod());
		addMod(new BunnyHopMod());
		addMod(new CaveFinderMod());
		addMod(new ChestEspMod());
		addMod(new ClickGuiMod());
		addMod(new CmdBlockMod());
		addMod(new CrashChestMod());
		addMod(new CrashItemMod());
		addMod(new CriticalsMod());
		addMod(new DerpMod());
		addMod(new DolphinMod());
		addMod(new FastBreakMod());
		addMod(new FastBowMod());
		addMod(new FastEatMod());
		addMod(new FastLadderMod());
		addMod(new FastPlaceMod());
		addMod(new FightBotMod());
		addMod(new FlightMod());
		addMod(new FollowMod());
		addMod(new ForceOpMod());
		addMod(new FreecamMod());
		addMod(new FullbrightMod());
		addMod(new GhostHandMod());
		addMod(new GlideMod());
		addMod(new GoToCmdMod());
		addMod(new HeadlessMod());
		addMod(new HeadRollMod());
		addMod(new HealthTagsMod());
		addMod(new HighJumpMod());
		addMod(new HomeMod());
		addMod(new InstantBunkerMod());
		addMod(new InvisibilityMod());
		addMod(new ItemEspMod());
		addMod(new JesusMod());
		addMod(new JetpackMod());
		addMod(new KaboomMod());
		addMod(new KillauraMod());
		addMod(new KillauraLegitMod());
		addMod(new LiquidsMod());
		addMod(new LsdMod());
		addMod(new MassTpaMod());
		addMod(new MileyCyrusMod());
		addMod(new MobEspMod());
		addMod(new MultiAuraMod());
		addMod(new NameProtectMod());
		addMod(new NameTagsMod());
		addMod(new NoFallMod());
		addMod(new NoHurtcamMod());
		addMod(new NoSlowdownMod());
		addMod(new NoWebMod());
		addMod(new NukerMod());
		addMod(new NukerLegitMod());
		addMod(new OpSignMod());
		addMod(new OverlayMod());
		addMod(new PanicMod());
		addMod(new PhaseMod());
		addMod(new PlayerEspMod());
		addMod(new PlayerFinderMod());
		addMod(new PotionSaverMod());
		addMod(new ProphuntEspMod());
		addMod(new ProtectMod());
		addMod(new RegenMod());
		addMod(new RemoteViewMod());
		addMod(new SearchMod());
		addMod(new SkinBlinkerMod());
		addMod(new SneakMod());
		addMod(new SpammerMod());
		addMod(new SpeedNukerMod());
		addMod(new SpiderMod());
		addMod(new StepMod());
		addMod(new ThrowMod());
		addMod(new TimerMod());
		addMod(new TiredMod());
		addMod(new TracersMod());
		addMod(new TriggerBotMod());
		addMod(new TrollPotionMod());
		addMod(new TrueSightMod());
		addMod(new TunnellerMod());
		addMod(new XRayMod());
		addMod(new YesCheatMod());
		addMod(new AutoBuildMod());
	}
	
	public Mod getModByClass(Class<?> modClass)
	{
		return mods.get(modClass.getAnnotation(Info.class).name());
	}
	
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
	
	private void addMod(Mod mod)
	{
		mods.put(mod.getName(), mod);
		mod.initSliders();
	}
}
