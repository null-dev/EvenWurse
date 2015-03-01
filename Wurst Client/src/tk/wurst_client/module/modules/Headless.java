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

public class Headless extends Module
{
	public Headless()
	{
		super(
			"Headless",
			"While this is active, other people will think you are\n"
				+ "headless. Looks hilarious!",
			Category.FUN);
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook
			(
				Minecraft.getMinecraft().thePlayer.rotationYaw,
				180F,
				Minecraft.getMinecraft().thePlayer.onGround
			));
	}
}
