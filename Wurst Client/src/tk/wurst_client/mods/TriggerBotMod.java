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
		// TODO: Clean up this mess!
		WurstClient.INSTANCE.mods.disableModsByClass(KillauraMod.class,
				KillauraLegitMod.class,
				MultiAuraMod.class,
				ClickAuraMod.class);
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
				WurstClient.INSTANCE.mods.getModByClass(YesCheatMod.class).isActive();
			if(yesCheatMode
				&& hasTimePassedS(WurstClient.INSTANCE.mods.getModByClass(KillauraMod.class).yesCheatSpeed)
				|| !yesCheatMode
				&& hasTimePassedS(WurstClient.INSTANCE.mods.getModByClass(KillauraMod.class).normalSpeed))
			{
				EntityLivingBase en =
					(EntityLivingBase)Minecraft.getMinecraft().objectMouseOver.entityHit;
				if((yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= WurstClient.INSTANCE.mods.getModByClass(KillauraMod.class).yesCheatRange || !yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= WurstClient.INSTANCE.mods.getModByClass(KillauraMod.class).normalRange)
					&& EntityUtils.isCorrectEntity(en, true))
				{
					if(WurstClient.INSTANCE.mods.getModByClass(AutoSwordMod.class).isActive())
						AutoSwordMod.setSlot();
					CriticalsMod.doCritical();
					WurstClient.INSTANCE.mods.getModByClass(BlockHitMod.class).doBlock();
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
