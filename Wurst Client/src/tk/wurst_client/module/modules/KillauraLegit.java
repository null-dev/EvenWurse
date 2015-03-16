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
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Mod;
import tk.wurst_client.utils.EntityUtils;

public class KillauraLegit extends Mod implements UpdateListener
{
	public KillauraLegit()
	{
		super(
			"KillauraLegit",
			"Slower Killaura that bypasses any cheat prevention\n"
				+ "PlugIn. Not required on most NoCheat+ servers!",
			Category.COMBAT);
	}
	
	@Override
	public void onEnable()
	{
		if(Client.wurst.modManager.getMod(Killaura.class)
			.getToggled())
			Client.wurst.modManager.getMod(Killaura.class)
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
		updateMS();
		if(hasTimePassedS(Killaura.yesCheatSpeed)
			&& EntityUtils.getClosestEntity(true) != null)
		{
			EntityLivingBase en = EntityUtils.getClosestEntity(true);
			if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= Killaura.yesCheatRange)
			{
				if(Client.wurst.modManager.getMod(
					Criticals.class).getToggled()
					&& Minecraft.getMinecraft().thePlayer.onGround)
					Minecraft.getMinecraft().thePlayer.jump();
				if(EntityUtils.getDistanceFromMouse(en) > 55)
					EntityUtils.faceEntityClient(en);
				else
				{
					EntityUtils.faceEntityClient(en);
					Minecraft.getMinecraft().thePlayer.swingItem();
					Minecraft.getMinecraft().playerController.attackEntity(
						Minecraft.getMinecraft().thePlayer, en);
				}
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
