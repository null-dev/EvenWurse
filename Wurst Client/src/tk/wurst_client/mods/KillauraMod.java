/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.Client;
import tk.wurst_client.events.EventManager;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Automatically attacks everything in your range.",
	name = "Killaura")
public class KillauraMod extends Mod implements UpdateListener
{
	public static float normalSpeed = 20F;
	public static float normalRange = 5F;
	public static float yesCheatSpeed = 12F;
	public static float yesCheatRange = 4.25F;
	public static float realSpeed;
	public static float realRange;
	
	@Override
	public void initSliders()
	{
		sliders.add(new BasicSlider("Killaura speed", normalSpeed, 2, 20, 0.1,
			ValueDisplay.DECIMAL));
		sliders.add(new BasicSlider("Killaura range", normalRange, 1, 6, 0.05,
			ValueDisplay.DECIMAL));
	}
	
	@Override
	public void updateSettings()
	{
		normalSpeed = (float)sliders.get(0).getValue();
		yesCheatSpeed = Math.min(normalSpeed, 12F);
		normalRange = (float)sliders.get(1).getValue();
		yesCheatRange = Math.min(normalRange, 4.25F);
	}
	
	@Override
	public void onEnable()
	{
		if(Client.wurst.modManager.getModByClass(KillauraLegit.class)
			.isEnabled())
			Client.wurst.modManager.getModByClass(KillauraLegit.class)
				.setEnabled(false);
		if(Client.wurst.modManager.getModByClass(MultiAuraMod.class)
			.isEnabled())
			Client.wurst.modManager.getModByClass(MultiAuraMod.class)
				.setEnabled(false);
		if(Client.wurst.modManager.getModByClass(TriggerBotMod.class)
			.isEnabled())
			Client.wurst.modManager.getModByClass(TriggerBotMod.class)
				.setEnabled(false);
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getModByClass(YesCheatMod.class).isEnabled())
		{
			realSpeed = yesCheatSpeed;
			realRange = yesCheatRange;
		}else
		{
			realSpeed = normalSpeed;
			realRange = normalRange;
		}
		updateMS();
		if(hasTimePassedS(realSpeed)
			&& EntityUtils.getClosestEntity(true) != null)
		{
			EntityLivingBase en = EntityUtils.getClosestEntity(true);
			if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= realRange)
			{
				if(Client.wurst.modManager.getModByClass(AutoSwordMod.class)
					.isEnabled())
					AutoSwordMod.setSlot();
				CriticalsMod.doCritical();
				EntityUtils.faceEntityPacket(en);
				Minecraft.getMinecraft().thePlayer.swingItem();
				Minecraft.getMinecraft().playerController.attackEntity(
					Minecraft.getMinecraft().thePlayer, en);
				updateLastMS();
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
}
