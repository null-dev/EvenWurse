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
		if(WurstClient.INSTANCE.modManager.getModByClass(KillauraMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(KillauraMod.class)
				.setEnabled(false);
		if(WurstClient.INSTANCE.modManager
			.getModByClass(KillauraLegitMod.class).isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(
				KillauraLegitMod.class).setEnabled(false);
		if(WurstClient.INSTANCE.modManager.getModByClass(MultiAuraMod.class)
			.isEnabled())
			WurstClient.INSTANCE.modManager.getModByClass(MultiAuraMod.class)
				.setEnabled(false);
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
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
				WurstClient.INSTANCE.modManager
					.getModByClass(YesCheatMod.class).isEnabled();
			KillauraMod killaura =
				(KillauraMod)WurstClient.INSTANCE.modManager
					.getModByClass(KillauraMod.class);
			if(yesCheatMode && hasTimePassedS(killaura.yesCheatSpeed)
				|| !yesCheatMode && hasTimePassedS(killaura.normalSpeed))
			{
				EntityLivingBase en =
					(EntityLivingBase)Minecraft.getMinecraft().objectMouseOver.entityHit;
				if((yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= killaura.yesCheatRange || !yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= killaura.normalRange)
					&& EntityUtils.isCorrectEntity(en, true))
				{
					if(WurstClient.INSTANCE.modManager.getModByClass(
						AutoSwordMod.class).isEnabled())
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
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
	}
}
