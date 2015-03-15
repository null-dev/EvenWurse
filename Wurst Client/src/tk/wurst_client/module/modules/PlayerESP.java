/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.RenderListener;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.RenderUtils;

public class PlayerESP extends Module implements RenderListener
{
	public PlayerESP()
	{
		super(
			"PlayerESP",
			"Allows you to see players through walls.",
			Category.RENDER);
	}
	
	@Override
	public void onEnable()
	{
		EventManager.addRenderListener(this);
	}
	
	@Override
	public void onRender()
	{
		if(Client.wurst.moduleManager.getMod(ArenaBrawl.class)
				.getToggled())
			return;
		for(Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(entity instanceof EntityPlayer
				&& !((Entity)entity).getName().equals(
					Minecraft.getMinecraft().getSession().getUsername()))
				RenderUtils.entityESPBox((Entity)entity, Client.wurst.friends
					.contains(((EntityPlayer)entity).getName()) ? 1 : 0);
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeRenderListener(this);
	}
}
