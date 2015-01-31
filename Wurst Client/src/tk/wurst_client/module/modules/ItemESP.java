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
import net.minecraft.entity.item.EntityItem;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.RenderUtils;

public class ItemESP extends Module
{
	public ItemESP()
	{
		super(
			"ItemESP",
			"Allows you to see items through walls.",
			0,
			Category.RENDER);
	}

	@Override
	public void onRender()
	{
		if(!getToggled())
			return;
		for(Object entity : Minecraft.getMinecraft().theWorld.loadedEntityList)
			if(entity instanceof EntityItem)
				RenderUtils.entityESPBox((Entity)entity, 2);
	}
}
