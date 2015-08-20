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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Allows you to see players through walls.",
	name = "PlayerESP")
public class PlayerEspMod extends Mod implements RenderListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.eventManager.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender()
	{
		if(WurstClient.INSTANCE.modManager.getModByClass(ArenaBrawlMod.class)
			.isActive())
			return;
		for(Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(entity instanceof EntityPlayer
				&& !((Entity)entity).getName().equals(
					Minecraft.getMinecraft().getSession().getUsername()))
				RenderUtils.entityESPBox((Entity)entity,
					WurstClient.INSTANCE.friends
						.contains(((EntityPlayer)entity).getName()) ? 1 : 0);
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(RenderListener.class, this);
	}
}
