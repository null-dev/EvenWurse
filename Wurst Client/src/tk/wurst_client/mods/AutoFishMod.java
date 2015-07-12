/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.EntityFishHook;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Automatically catches fish.",
	name = "AutoFish",
	noCheatCompatible = true)
public class AutoFishMod extends Mod implements UpdateListener
{
	private boolean catching = false;
	
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().thePlayer.fishEntity != null
			&& isHooked(Minecraft.getMinecraft().thePlayer.fishEntity)
			&& !catching)
		{
			catching = true;
			Minecraft.getMinecraft().rightClickMouse();
			new Thread("AutoFish")
			{
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(1000);
					}catch(InterruptedException e)
					{
						e.printStackTrace();
					}
					Minecraft.getMinecraft().rightClickMouse();
					catching = false;
				}
			}.start();
		}
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
	}
	
	private boolean isHooked(EntityFishHook hook)
	{
		return hook.motionX == 0.0D && hook.motionZ == 0.0D
			&& hook.motionY != 0.0D;
	}
}
