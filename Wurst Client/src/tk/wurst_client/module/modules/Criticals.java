/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Criticals extends Module
{
	public Criticals()
	{
		super(
			"Criticals",
			"Changes all your hits to critical hits.",
			0,
			Category.COMBAT);
	}
	
	@Override
	public void onLeftClick()
	{
		if(Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase)
			doCritical();
	}
	
	public static void doCritical()
	{
		if(!Client.wurst.moduleManager.getModuleFromClass(Criticals.class).getToggled())
			return;
		if(!Minecraft.getMinecraft().thePlayer.isInWater() && !Minecraft.getMinecraft().thePlayer.isInsideOfMaterial(Material.lava) && Minecraft.getMinecraft().thePlayer.onGround)
		{
			Minecraft.getMinecraft().thePlayer.motionY = 0.1F;
			Minecraft.getMinecraft().thePlayer.fallDistance = 0.1F;
			Minecraft.getMinecraft().thePlayer.onGround = false;
		}
	}
}
