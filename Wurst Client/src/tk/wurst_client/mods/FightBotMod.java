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
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.EntityUtils;

@Info(category = Category.COMBAT,
	description = "A bot that automatically fights for you.\n"
		+ "It walks around and kills everything.\n"
		+ "Good for MobArena.",
	name = "FightBot")
public class FightBotMod extends Mod implements UpdateListener
{
	private float speed;
	private float range = 6F;
	private double distance = 3D;
	private EntityLivingBase entity;
	
	@Override
	public void onEnable()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(EntityUtils.getClosestEntity(true) != null)
			entity = EntityUtils.getClosestEntity(true);
		if(entity == null)
			return;
		if(entity.getHealth() <= 0 || entity.isDead
			|| Minecraft.getMinecraft().thePlayer.getHealth() <= 0)
		{
			entity = null;
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed =
				false;
			return;
		}
		double xDist =
			Math.abs(Minecraft.getMinecraft().thePlayer.posX - entity.posX);
		double zDist =
			Math.abs(Minecraft.getMinecraft().thePlayer.posZ - entity.posZ);
		EntityUtils.faceEntityClient(entity);
		if(xDist > distance || zDist > distance)
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = true;
		else
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed =
				false;
		if(Minecraft.getMinecraft().thePlayer.isCollidedHorizontally
			&& Minecraft.getMinecraft().thePlayer.onGround)
			Minecraft.getMinecraft().thePlayer.jump();
		if(Minecraft.getMinecraft().thePlayer.isInWater()
			&& Minecraft.getMinecraft().thePlayer.posY < entity.posY)
			Minecraft.getMinecraft().thePlayer.motionY += 0.04;
		if(Client.wurst.modManager.getModByClass(YesCheatMod.class)
			.isEnabled())
			speed = KillauraMod.yesCheatSpeed;
		else
			speed = KillauraMod.normalSpeed;
		updateMS();
		if(hasTimePassedS(speed))
			if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= range)
			{
				if(Client.wurst.modManager.getModByClass(AutoSwordMod.class)
					.isEnabled())
					AutoSwordMod.setSlot();
				CriticalsMod.doCritical();
				if(EntityUtils.getDistanceFromMouse(entity) > 55)
					EntityUtils.faceEntityClient(entity);
				else
				{
					EntityUtils.faceEntityClient(entity);
					Minecraft.getMinecraft().thePlayer.swingItem();
					Minecraft.getMinecraft().playerController.attackEntity(
						Minecraft.getMinecraft().thePlayer, entity);
				}
				updateLastMS();
			}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
		Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
	}
}
