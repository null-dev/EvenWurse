/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Mod;
import tk.wurst_client.utils.EntityUtils;

public class Killaura extends Mod implements UpdateListener
{
	public static float normalSpeed = 20F;
	public static float normalRange = 5F;
	public static float yesCheatSpeed = 12F;
	public static float yesCheatRange = 4.25F;
	public static float realSpeed;
	public static float realRange;
	
	public Killaura()
	{
		super(
			"Killaura",
			"Automatically attacks everything in your range.",
			Category.COMBAT);
	}
	
	@Override
	public void initSliders()
	{
		sliders.add(new BasicSlider("Killaura speed", normalSpeed, 2, 20,
			0.1, ValueDisplay.DECIMAL));
		sliders.add(new BasicSlider("Killaura range", normalRange, 1, 6,
			0.05, ValueDisplay.DECIMAL));
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
		if(Client.wurst.modManager.getMod(KillauraLegit.class)
			.getToggled())
			Client.wurst.modManager.getMod(KillauraLegit.class)
				.setToggled(false);
		if(Client.wurst.modManager.getMod(MultiAura.class)
			.getToggled())
			Client.wurst.modManager.getMod(MultiAura.class)
				.setToggled(false);
		if(Client.wurst.modManager.getMod(TriggerBot.class)
			.getToggled())
			Client.wurst.modManager.getMod(TriggerBot.class)
				.setToggled(false);
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getMod(YesCheat.class)
			.getToggled())
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
				Criticals.doCritical();
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
		EventManager.removeUpdateListener(this);
	}
}
