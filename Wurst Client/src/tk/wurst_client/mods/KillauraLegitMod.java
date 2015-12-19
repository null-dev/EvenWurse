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

@Info(category = Category.COMBAT,
	description = "Slower Killaura that bypasses any cheat prevention\n"
		+ "PlugIn. Not required on most NoCheat+ servers!",
	name = "KillauraLegit")
public class KillauraLegitMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		// TODO: Clean up this mess!
		if(WurstClient.INSTANCE.mods.killauraMod.isEnabled())
			WurstClient.INSTANCE.mods.killauraMod.setEnabled(false);
		if(WurstClient.INSTANCE.mods.multiAuraMod.isEnabled())
			WurstClient.INSTANCE.mods.multiAuraMod.setEnabled(false);
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
		EntityLivingBase en = EntityUtils.getClosestEntity(true);
		if(hasTimePassedS(WurstClient.INSTANCE.mods.killauraMod.yesCheatSpeed)
			&& en != null)
		{
			if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= WurstClient.INSTANCE.mods.killauraMod.yesCheatRange)
			{
				if(WurstClient.INSTANCE.mods.criticalsMod.isActive()
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
