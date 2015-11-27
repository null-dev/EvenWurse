/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Automatically attacks the entity you're looking at.",
	name = "TriggerBot")
public class TriggerBotMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		if(WurstClient.INSTANCE.mods.killauraMod.isEnabled())
			WurstClient.INSTANCE.mods.killauraMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.killauraLegitMod.isEnabled())
			WurstClient.INSTANCE.mods.killauraLegitMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.multiAuraMod.isEnabled())
			WurstClient.INSTANCE.mods.multiAuraMod.setEnabled(false);
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().objectMouseOver != null
			&& Minecraft.getMinecraft().objectMouseOver.typeOfHit == MovingObjectType.ENTITY
			&& Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase)
		{
			updateMS();
			boolean yesCheatMode =
				WurstClient.INSTANCE.mods.yesCheatMod.isActive();
			if(yesCheatMode
				&& hasTimePassedS(WurstClient.INSTANCE.mods.killauraMod.yesCheatSpeed)
				|| !yesCheatMode
				&& hasTimePassedS(WurstClient.INSTANCE.mods.killauraMod.normalSpeed))
			{
				EntityLivingBase en =
					(EntityLivingBase)Minecraft.getMinecraft().objectMouseOver.entityHit;
				if((yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= WurstClient.INSTANCE.mods.killauraMod.yesCheatRange || !yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= WurstClient.INSTANCE.mods.killauraMod.normalRange)
					&& EntityUtils.isCorrectEntity(en, true))
				{
					if(WurstClient.INSTANCE.mods.autoSwordMod.isActive())
						AutoSwordMod.setSlot();
					CriticalsMod.doCritical();
					Minecraft.getMinecraft().thePlayer.swingItem();
					Minecraft.getMinecraft().playerController.attackEntity(
						Minecraft.getMinecraft().thePlayer, en);
					updateLastMS();
				}
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
	}
}
