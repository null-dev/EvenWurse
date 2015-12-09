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
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.utils.EntityUtils;

@Mod.Info(category = Mod.Category.COMBAT,
	description = "Automatically attacks the closest valid entity whenever you\n"
		+ "click.\n"
		+ "Warning: ClickAuras generally look more suspicious than Killauras\n"
		+ "and are easier to detect. It is recommended to use Killaura or\n"
		+ "TriggerBot instead.",
	name = "ClickAura")
public class ClickAuraMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		// TODO: Clean up this mess!
		if(WurstClient.INSTANCE.mods.killauraMod.isEnabled())
			WurstClient.INSTANCE.mods.killauraMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.killauraLegitMod.isEnabled())
			WurstClient.INSTANCE.mods.killauraLegitMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.multiAuraMod.isEnabled())
			WurstClient.INSTANCE.mods.multiAuraMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.triggerBotMod.isEnabled())
			WurstClient.INSTANCE.mods.triggerBotMod.setEnabled(false);
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		EntityLivingBase en = EntityUtils.getClosestEntity(true, true);
		if(hasTimePassedS(WurstClient.INSTANCE.mods.killauraMod.realSpeed)
			&& en != null
			&& Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed)
		{
			if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= WurstClient.INSTANCE.mods.killauraMod.realRange)
			{
				if(WurstClient.INSTANCE.mods.autoSwordMod.isActive())
					AutoSwordMod.setSlot();
				CriticalsMod.doCritical();
				EntityUtils.faceEntityPacket(en);
				Minecraft.getMinecraft().thePlayer.swingItem();
				Minecraft.getMinecraft().playerController.attackEntity(
					Minecraft.getMinecraft().thePlayer, en);
				updateLastMS();
			}
		}
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
	}
}
