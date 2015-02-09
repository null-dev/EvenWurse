/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Regen extends Module
{
	public Regen()
	{
		super(
			"Regen",
			"Regenerates your health 100 times faster.\n"
				+ "Can cause unwanted \"Flying is not enabled!\" kicks.",
			0,
			Category.COMBAT);
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
			&& Minecraft.getMinecraft().thePlayer.getFoodStats().getFoodLevel() > 17
			&& Minecraft.getMinecraft().thePlayer.getHealth() < 20
			&& Minecraft.getMinecraft().thePlayer.onGround)
			for(int i = 0; i < 1000; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
	}
}
