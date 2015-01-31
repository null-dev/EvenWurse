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
import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.EntityUtils;

public class FightBot extends Module
{
	public FightBot()
	{
		super(
			"FightBot",
			"A bot that automatically fights for you.\n"
				+ "It walks around and kills everything.\n"
				+ "Good for MobArena.",
				0,
				Category.COMBAT);
	}

	private float speed;
	private float range = 6F;
	private double distance = 3D;
	private EntityLivingBase entity;

	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(EntityUtils.getClosestEntity(true) != null)
			entity = EntityUtils.getClosestEntity(true);
		if(entity == null)
			return;
		if(entity.getHealth() <= 0 || entity.isDead || Minecraft.getMinecraft().thePlayer.getHealth() <= 0)
		{
			entity = null;
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
			return;
		}
		double xDist = Math.abs(Minecraft.getMinecraft().thePlayer.posX - entity.posX);
		double zDist = Math.abs(Minecraft.getMinecraft().thePlayer.posZ - entity.posZ);
		EntityUtils.faceEntityClient(entity);
		if(xDist > distance || zDist > distance)
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = true;
		else
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
		if(Minecraft.getMinecraft().thePlayer.isCollidedHorizontally && Minecraft.getMinecraft().thePlayer.onGround)
			Minecraft.getMinecraft().thePlayer.jump();
		if(Minecraft.getMinecraft().thePlayer.isInWater() && Minecraft.getMinecraft().thePlayer.posY < entity.posY)
			Minecraft.getMinecraft().thePlayer.motionY += 0.04;
		if(Client.Wurst.moduleManager.getModuleFromClass(YesCheat.class).getToggled())
			speed = Killaura.yesCheatSpeed;
		else
			speed = Killaura.normalSpeed;
		updateMS();
		if(hasTimePassedS(speed))
			if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity) <= range)
			{
				Criticals.doCritical();
				if(EntityUtils.getDistanceFromMouse(entity) > 55)
					EntityUtils.faceEntityClient(entity);
				else
				{
					EntityUtils.faceEntityClient(entity);
					Minecraft.getMinecraft().thePlayer.swingItem();
					Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, entity);
				}
				updateLastMS();
			}
	}

	@Override
	public void onDisable()
	{
		Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
	}
}
