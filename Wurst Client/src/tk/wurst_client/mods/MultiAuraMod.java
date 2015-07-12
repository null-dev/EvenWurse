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
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Faster Killaura that attacks multiple entities at once.",
	name = "MultiAura",
	noCheatCompatible = false)
public class MultiAuraMod extends Mod implements UpdateListener
{
	private float range = 6F;
	
	@Override
	public void onEnable()
	{
		if(WurstClient.INSTANCE.modManager.getModByClass(KillauraMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(KillauraMod.class)
				.setEnabled(false);
		if(WurstClient.INSTANCE.modManager
			.getModByClass(KillauraLegitMod.class).isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(
				KillauraLegitMod.class).setEnabled(false);
		if(WurstClient.INSTANCE.modManager.getModByClass(TriggerBotMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(TriggerBotMod.class)
				.setEnabled(false);
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		if(EntityUtils.getClosestEntity(true) != null)
		{
			for(int i = 0; i < Math.min(
				EntityUtils.getCloseEntities(true, range).size(), 64); i++)
			{
				EntityLivingBase en =
					EntityUtils.getCloseEntities(true, range).get(i);
				if(WurstClient.INSTANCE.modManager.getModByClass(
					AutoSwordMod.class).isActive())
					AutoSwordMod.setSlot();
				CriticalsMod.doCritical();
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
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
	}
}
