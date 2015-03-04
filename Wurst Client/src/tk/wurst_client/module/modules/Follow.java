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
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.EntityUtils;

public class Follow extends Module
{
	public Follow()
	{
		super(
			"Follow",
			"A bot that follows the closest entity.\n"
				+ "Very annoying.",
			Category.COMBAT);
	}
	
	private EntityLivingBase entity;
	private float range = 12F;
	
	@Override
	public String getRenderName()
	{
		if(entity != null)
			return "Following " + entity.getName();
		else
			return "Follow";
	}
	
	@Override
	public void onEnable()
	{
		entity = null;
		if(EntityUtils.getClosestEntity(false) != null)
		{
			EntityLivingBase en = EntityUtils.getClosestEntity(false);
			if(Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) <= range)
				entity = en;
		}
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(entity == null)
		{
			setToggled(false);
			return;
		}
		if(entity.isDead || Minecraft.getMinecraft().thePlayer.isDead)
		{
			entity = null;
			setToggled(false);
			return;
		}
		double xDist =
			Math.abs(Minecraft.getMinecraft().thePlayer.posX - entity.posX);
		double zDist =
			Math.abs(Minecraft.getMinecraft().thePlayer.posZ - entity.posZ);
		EntityUtils.faceEntityClient(entity);
		if(xDist > 1D || zDist > 1D)
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
	}
	
	@Override
	public void onDisable()
	{
		if(entity != null)
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed =
				false;
	}
}
