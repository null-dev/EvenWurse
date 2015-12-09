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
import net.minecraft.entity.item.EntityItem;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.RenderListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;
import tk.wurst_client.utils.RenderUtils;

@Info(category = Category.RENDER,
	description = "Allows you to see items through walls.",
	name = "ItemESP")
public class ItemEspMod extends Mod implements RenderListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.events.addRenderListener(this);
	}
	
	@Override
	public void onRender()
	{
		for(Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(entity instanceof EntityItem)
				RenderUtils.entityESPBox((Entity)entity, 2);
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.removeRenderListener(this);
	}
}
