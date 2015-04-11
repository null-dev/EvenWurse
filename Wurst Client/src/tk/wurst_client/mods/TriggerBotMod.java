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
import tk.wurst_client.Client;
import tk.wurst_client.events.EventManager;
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
		if(Client.wurst.modManager.getModByClass(KillauraMod.class).isEnabled())
			Client.wurst.modManager.getModByClass(KillauraMod.class)
				.setEnabled(false);
		if(Client.wurst.modManager.getModByClass(KillauraLegitMod.class)
			.isEnabled())
			Client.wurst.modManager.getModByClass(KillauraLegitMod.class)
				.setEnabled(false);
		if(Client.wurst.modManager.getModByClass(MultiAuraMod.class)
			.isEnabled())
			Client.wurst.modManager.getModByClass(MultiAuraMod.class)
				.setEnabled(false);
		EventManager.update.addListener(this);
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
				Client.wurst.modManager.getModByClass(YesCheatMod.class)
					.isEnabled();
			if(yesCheatMode && hasTimePassedS(KillauraMod.yesCheatSpeed)
				|| !yesCheatMode && hasTimePassedS(KillauraMod.normalSpeed))
			{
				EntityLivingBase en =
					(EntityLivingBase)Minecraft.getMinecraft().objectMouseOver.entityHit;
				if((yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= KillauraMod.yesCheatRange || !yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= KillauraMod.normalRange)
					&& EntityUtils.isCorrectEntity(en, true))
				{
					if(Client.wurst.modManager
						.getModByClass(AutoSwordMod.class).isEnabled())
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
		EventManager.update.removeListener(this);
	}
}
