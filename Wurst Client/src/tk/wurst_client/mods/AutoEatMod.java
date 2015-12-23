/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
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
import tk.wurst_client.utils.Utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
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
		final AtomicBoolean canceled = new AtomicBoolean(false);
		AutoEatListener listener = new AutoEatListener(canceled);
		//Timeout after 10 seconds of trying to eat
		Utils.schedule(() -> {
			if(!canceled.get()) {
				WurstClient.INSTANCE.chat.warning("AutoEat timed out, trying again...");
				this.setEnabled(false);
				try {Thread.sleep(3000);} catch (InterruptedException ignored) {}
				this.setEnabled(true);
			}
		}, 15, TimeUnit.SECONDS);
		WurstClient.INSTANCE.events.add(UpdateListener.class, listener);
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
	}
	
	public boolean isEating()
	{
		return oldSlot != -1;
	}

	class AutoEatListener implements UpdateListener {

		AtomicBoolean canceled;

		public AutoEatListener(AtomicBoolean canceled) {
			this.canceled = canceled;
		}

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

		public void stop()
		{
			Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed =
					false;
			Minecraft.getMinecraft().thePlayer.inventory.currentItem =
					oldSlot;
			oldSlot = -1;
			WurstClient.INSTANCE.events.remove(
					UpdateListener.class, this);
			canceled.set(true);
		}
	}

}