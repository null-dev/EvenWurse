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
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Mod;
import tk.wurst_client.module.Mod.Category;
import tk.wurst_client.module.Mod.Info;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Automatically attacks the entity you're looking at.",
	name = "TriggerBot")
public class TriggerBot extends Mod implements UpdateListener
{
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
		if(Client.wurst.modManager.getModByClass(MultiAura.class)
			.getToggled())
			Client.wurst.modManager.getModByClass(MultiAura.class)
				.setToggled(false);
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().objectMouseOver != null
			&& Minecraft.getMinecraft().objectMouseOver.typeOfHit == MovingObjectType.ENTITY)
		{
			updateMS();
			boolean yesCheatMode =
				Client.wurst.modManager.getModByClass(YesCheat.class)
					.getToggled();
			if(yesCheatMode && hasTimePassedS(Killaura.yesCheatSpeed)
				|| !yesCheatMode && hasTimePassedS(Killaura.normalSpeed))
			{
				EntityLivingBase en =
					(EntityLivingBase)Minecraft.getMinecraft().objectMouseOver.entityHit;
				if((yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= Killaura.yesCheatRange
					|| !yesCheatMode
					&& Minecraft.getMinecraft().thePlayer
						.getDistanceToEntity(en) <= Killaura.normalRange)
					&& EntityUtils.isCorrectEntity(en, true))
				{
					Criticals.doCritical();
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
		EventManager.removeUpdateListener(this);
	}
}
