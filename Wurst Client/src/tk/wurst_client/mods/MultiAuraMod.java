/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
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

import java.util.ArrayList;

@Info(category = Category.COMBAT,
	description = "Faster Killaura that attacks multiple entities at once.",
	name = "MultiAura",
	noCheatCompatible = false)
public class MultiAuraMod extends Mod implements UpdateListener
{
	private static final float RANGE = 6F;
	
	@Override
	public void onEnable()
	{
		// TODO: Clean up this mess!
		if(WurstClient.INSTANCE.mods.killauraMod.isEnabled())
			WurstClient.INSTANCE.mods.killauraMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.killauraLegitMod.isEnabled())
			WurstClient.INSTANCE.mods.killauraLegitMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.clickAuraMod.isEnabled())
			WurstClient.INSTANCE.mods.clickAuraMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.triggerBotMod.isEnabled())
			WurstClient.INSTANCE.mods.triggerBotMod.setEnabled(false);
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		if(EntityUtils.getClosestEntity(true) != null)
		{
			if(WurstClient.INSTANCE.mods.autoSwordMod.isActive())
				AutoSwordMod.setSlot();
			CriticalsMod.doCritical();
			WurstClient.INSTANCE.mods.blockHitMod.doBlock();
			ArrayList<EntityLivingBase> entities =
				EntityUtils.getCloseEntities(true, RANGE);
			for(int i = 0; i < Math.min(entities.size(), 64); i++)
			{
				EntityLivingBase en = entities.get(i);
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
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
	}
}
