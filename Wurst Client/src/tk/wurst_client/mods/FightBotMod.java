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
	description = "A bot that automatically fights for you.\n"
		+ "It walks around and kills everything.\n" + "Good for MobArena.",
	name = "FightBot")
public class FightBotMod extends Mod implements UpdateListener
{
	private static final float RANGE = 6F;
	private static final double DISTANCE = 3D;

	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		EntityLivingBase entity = EntityUtils.getClosestEntity(true);
		if(entity == null)
			return;
		if(entity.getHealth() <= 0 || entity.isDead
			|| Minecraft.getMinecraft().thePlayer.getHealth() <= 0)
		{
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed =
				false;
			return;
		}
		double xDist =
			Math.abs(Minecraft.getMinecraft().thePlayer.posX - entity.posX);
		double zDist =
			Math.abs(Minecraft.getMinecraft().thePlayer.posZ - entity.posZ);
		EntityUtils.faceEntityClient(entity);
		Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = xDist > DISTANCE || zDist > DISTANCE;
		if(Minecraft.getMinecraft().thePlayer.isCollidedHorizontally
			&& Minecraft.getMinecraft().thePlayer.onGround)
			Minecraft.getMinecraft().thePlayer.jump();
		if(Minecraft.getMinecraft().thePlayer.isInWater()
			&& Minecraft.getMinecraft().thePlayer.posY < entity.posY)
			Minecraft.getMinecraft().thePlayer.motionY += 0.04;
		float speed;
		if(WurstClient.INSTANCE.mods.yesCheatMod.isActive())
			speed = WurstClient.INSTANCE.mods.killauraMod.yesCheatSpeed;
		else
			speed = WurstClient.INSTANCE.mods.killauraMod.normalSpeed;
		updateMS();
		if(hasTimePassedS(speed))
			if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= RANGE)
			{
				if(WurstClient.INSTANCE.mods.autoSwordMod.isActive())
					AutoSwordMod.setSlot();
				CriticalsMod.doCritical();
				WurstClient.INSTANCE.mods.blockHitMod.doBlock();
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
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
		Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
	}
}
