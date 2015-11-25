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
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "Slower Killaura that bypasses any cheat prevention\n"
		+ "PlugIn. Not required on most NoCheat+ servers!",
	name = "KillauraLegit")
public class KillauraLegitMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		if(WurstClient.INSTANCE.mods.getModByClass(KillauraMod.class)
			.isEnabled())
			WurstClient.INSTANCE.mods.getModByClass(KillauraMod.class)
				.setEnabled(false);
		if(WurstClient.INSTANCE.mods.getModByClass(MultiAuraMod.class)
			.isEnabled())
			WurstClient.INSTANCE.mods.getModByClass(MultiAuraMod.class)
				.setEnabled(false);
		if(WurstClient.INSTANCE.mods.getModByClass(TriggerBotMod.class)
			.isEnabled())
			WurstClient.INSTANCE.mods.getModByClass(TriggerBotMod.class)
				.setEnabled(false);
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		updateMS();
		KillauraMod killaura =
			(KillauraMod)WurstClient.INSTANCE.mods
				.getModByClass(KillauraMod.class);
		EntityLivingBase en = EntityUtils.getClosestEntity(true, true);
		if(hasTimePassedS(killaura.yesCheatSpeed) && en != null)
		{
			if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= killaura.yesCheatRange)
			{
				if(WurstClient.INSTANCE.mods.getModByClass(
					CriticalsMod.class).isActive()
					&& Minecraft.getMinecraft().thePlayer.onGround)
					Minecraft.getMinecraft().thePlayer.jump();
				if(EntityUtils.getDistanceFromMouse(en) > 55)
					EntityUtils.faceEntityClient(en);
				else
				{
					EntityUtils.faceEntityClient(en);
					Minecraft.getMinecraft().thePlayer.swingItem();
					Minecraft.getMinecraft().playerController.attackEntity(
						Minecraft.getMinecraft().thePlayer, en);
				}
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
