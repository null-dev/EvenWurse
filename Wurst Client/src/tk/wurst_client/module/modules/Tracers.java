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
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.RenderUtils;

public class Tracers extends Module
{
	public Tracers()
	{
		super(
			"Tracers",
			"Draws lines to players around you.",
			0,
			Category.RENDER);
	}

	@Override
	public void onRender()
	{
		if(!getToggled() || Client.wurst.moduleManager.getModuleFromClass(ArenaBrawl.class).getToggled())
			return;
		for(Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(entity instanceof EntityPlayer && !((Entity)entity).getName().equals(Minecraft.getMinecraft().getSession().getUsername()))
				RenderUtils.tracerLine((Entity)entity, Client.wurst.friends.contains(((EntityPlayer)entity).getName()) ? 1 : 0);
	}
}
