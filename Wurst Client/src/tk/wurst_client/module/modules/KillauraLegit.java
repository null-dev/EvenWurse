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
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.EntityUtils;

public class KillauraLegit extends Module
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
		if(Client.wurst.moduleManager.getModuleFromClass(Killaura.class)
			.getToggled())
			Client.wurst.moduleManager.getModuleFromClass(Killaura.class)
				.setToggled(false);
		if(Client.wurst.moduleManager.getModuleFromClass(MultiAura.class)
			.getToggled())
			Client.wurst.moduleManager.getModuleFromClass(MultiAura.class)
				.setToggled(false);
		if(Client.wurst.moduleManager.getModuleFromClass(TriggerBot.class)
			.getToggled())
			Client.wurst.moduleManager.getModuleFromClass(TriggerBot.class)
				.setToggled(false);
	}
	
	@Override
	public void onUpdate()
	{
		if(getToggled())
		{
			updateMS();
			if(hasTimePassedS(Killaura.yesCheatSpeed)
				&& EntityUtils.getClosestEntity(true) != null)
			{
				EntityLivingBase en = EntityUtils.getClosestEntity(true);
				if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= Killaura.yesCheatRange)
				{
					if(Client.wurst.moduleManager.getModuleFromClass(
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
	}
}
