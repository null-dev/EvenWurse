/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import tk.wurst_client.WurstClient;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Automatically eats food when necessary.",
	name = "AutoEat")
public class AutoEatMod extends Mod implements UpdateListener
{
	private int oldSlot;
	private int bestSlot;
	
	@Override
	public void onEnable()
	{
		oldSlot = -1;
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(oldSlot != -1
			|| Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
			|| Minecraft.getMinecraft().thePlayer.getFoodStats().getFoodLevel() >= 20)
			return;
		float bestSaturation = 0F;
		bestSlot = -1;
		for(int i = 0; i < 9; i++)
		{
			ItemStack item =
				Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
			if(item == null)
				continue;
			float saturation = 0;
			if(item.getItem() instanceof ItemFood)
				saturation =
					((ItemFood)item.getItem()).getSaturationModifier(item);
			if(saturation > bestSaturation)
			{
				bestSaturation = saturation;
				bestSlot = i;
			}
		}
		if(bestSlot == -1)
			return;
		oldSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
		WurstClient.INSTANCE.eventManager.add(UpdateListener.class,
			new UpdateListener()
			{
				@Override
				public void onUpdate()
				{
					if(!AutoEatMod.this.isActive()
						|| Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
						|| Minecraft.getMinecraft().thePlayer.getFoodStats()
							.getFoodLevel() >= 20)
					{
						stop();
						return;
					}
					ItemStack item =
						Minecraft.getMinecraft().thePlayer.inventory
							.getStackInSlot(bestSlot);
					if(item == null || !(item.getItem() instanceof ItemFood))
					{
						stop();
						return;
					}
					Minecraft.getMinecraft().thePlayer.inventory.currentItem =
						bestSlot;
					Minecraft.getMinecraft().playerController.sendUseItem(
						Minecraft.getMinecraft().thePlayer,
						Minecraft.getMinecraft().theWorld, item);
					Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed =
						true;
				}
				
				private void stop()
				{
					Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed =
						false;
					Minecraft.getMinecraft().thePlayer.inventory.currentItem =
						oldSlot;
					oldSlot = -1;
					WurstClient.INSTANCE.eventManager.remove(
						UpdateListener.class, this);
				}
			});
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.eventManager.remove(UpdateListener.class, this);
	}
	
	public boolean isEating()
	{
		return oldSlot != -1;
	}
}
