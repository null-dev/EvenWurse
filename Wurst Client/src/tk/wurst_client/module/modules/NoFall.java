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

public class NoFall extends Module
{
	public NoFall()
	{
		super(
			"NoFall",
			"Protects you from fall damage.",
			0,
			Category.MOVEMENT);
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(Minecraft.getMinecraft().thePlayer.fallDistance > 2)
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
	}
}
