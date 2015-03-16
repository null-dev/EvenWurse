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
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Faster Killaura that attacks multiple entities at once.",
	name = "MultiAura")
public class MultiAura extends Mod implements UpdateListener
{
	private float range = 6F;
	
	@Override
	public void onEnable()
	{
		if(Client.wurst.modManager.getModByClass(Killaura.class)
			.getToggled())
			Client.wurst.modManager.getModByClass(Killaura.class)
				.setToggled(false);
		if(Client.wurst.modManager.getModByClass(KillauraLegit.class)
			.getToggled())
			Client.wurst.modManager.getModByClass(KillauraLegit.class)
				.setToggled(false);
		if(Client.wurst.modManager.getModByClass(TriggerBot.class)
			.getToggled())
			Client.wurst.modManager.getModByClass(TriggerBot.class)
				.setToggled(false);
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getModByClass(YesCheat.class)
			.getToggled())
		{
			noCheatMessage();
			setToggled(false);
			Client.wurst.chat.message("Switching to "
				+ Client.wurst.modManager.getModByClass(Killaura.class)
					.getName() + ".");
			Client.wurst.modManager.getModByClass(Killaura.class)
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
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
	}
}
