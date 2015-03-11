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

public class MultiAura extends Module
{
	
	public MultiAura()
	{
		super(
			"MultiAura",
			"Faster Killaura that attacks multiple entities at once.",
			Category.COMBAT);
	}
	
	private float range = 6F;
	
	@Override
	public void onEnable()
	{
		if(Client.wurst.moduleManager.getModuleFromClass(Killaura.class)
			.getToggled())
			Client.wurst.moduleManager.getModuleFromClass(Killaura.class)
				.setToggled(false);
		if(Client.wurst.moduleManager.getModuleFromClass(KillauraLegit.class)
			.getToggled())
			Client.wurst.moduleManager.getModuleFromClass(KillauraLegit.class)
				.setToggled(false);
		if(Client.wurst.moduleManager.getModuleFromClass(TriggerBot.class)
			.getToggled())
			Client.wurst.moduleManager.getModuleFromClass(TriggerBot.class)
				.setToggled(false);
	}
	
	@Override
	public void oldOnUpdate()
	{
		if(!getToggled())
			return;
		if(Client.wurst.moduleManager.getModuleFromClass(YesCheat.class)
			.getToggled())
		{
			noCheatMessage();
			setToggled(false);
			Client.wurst.chat.message("Switching to "
				+ Client.wurst.moduleManager.getModuleFromClass(Killaura.class)
					.getName() + ".");
			Client.wurst.moduleManager.getModuleFromClass(Killaura.class)
				.setToggled(true);
			return;
		}
		updateMS();
		if(EntityUtils.getClosestEntity(true) != null)
		{
			for(int i = 0; i < Math.min(
				EntityUtils.getCloseEntities(true, range).size(), 64); i++)
			{
				EntityLivingBase en =
					EntityUtils.getCloseEntities(true, range).get(i);
				Criticals.doCritical();
				EntityUtils.faceEntityPacket(en);
				Minecraft.getMinecraft().thePlayer.swingItem();
				Minecraft.getMinecraft().playerController.attackEntity(
					Minecraft.getMinecraft().thePlayer, en);
			}
			updateLastMS();
		}
	}
}
