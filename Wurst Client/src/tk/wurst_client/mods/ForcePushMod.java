/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.utils.EntityUtils;

@Mod.Info(category = Mod.Category.FUN,
	description = "Pushes mobs like crazy.\n"
		+ "They'll literally fly away!\n"
		+ "Can sometimes get you kicked for \"Flying is not enabled\".",
	name = "ForcePush")
public class ForcePushMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().thePlayer.onGround
			&& EntityUtils.getClosestEntity(true) != null
			&& EntityUtils.getClosestEntity(true).getDistanceToEntity(
				Minecraft.getMinecraft().thePlayer) < 2)
			for(int i = 0; i < 1000; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer(true));
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
	}
}
