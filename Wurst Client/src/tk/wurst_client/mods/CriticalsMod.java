/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.LeftClickListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Changes all your hits to critical hits.",
	name = "Criticals")
public class CriticalsMod extends Mod implements LeftClickListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.eventManager.add(LeftClickListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(LeftClickListener.class, this);
	}
	
	@Override
	public void onLeftClick()
	{
		if(Minecraft.getMinecraft().objectMouseOver != null
			&& Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase)
			doCritical();
	}
	
	public static void doCritical()
	{
		if(!WurstClient.INSTANCE.modManager.getModByClass(CriticalsMod.class)
			.isActive())
			return;
		if(!Minecraft.getMinecraft().thePlayer.isInWater()
			&& !Minecraft.getMinecraft().thePlayer
				.isInsideOfMaterial(Material.lava)
			&& Minecraft.getMinecraft().thePlayer.onGround)
		{
			Minecraft.getMinecraft().thePlayer.motionY = 0.1F;
			Minecraft.getMinecraft().thePlayer.fallDistance = 0.1F;
			Minecraft.getMinecraft().thePlayer.onGround = false;
		}
	}
}
