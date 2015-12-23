/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package xyz.nulldev.ew.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Chat;
import tk.wurst_client.api.Module;
import tk.wurst_client.api.ModuleConfiguration;
import tk.wurst_client.events.listeners.UpdateListener;
import tk.wurst_client.mods.Mod;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Module.ModuleInfo(
		version = 1.00f,
		usesConfig = true
)
@Info(category = Category.COMBAT,
	description = "Spams low power arrows.\n"
		+ "Tip: This works with BowAimbot.",
	name = "BowSpam",
	noCheatCompatible = false)
public class BowSpamMod extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		WurstClient.INSTANCE.events.add(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Minecraft.getMinecraft().thePlayer.getHealth() > 0
			&& (Minecraft.getMinecraft().thePlayer.onGround || Minecraft
				.getMinecraft().thePlayer.capabilities.isCreativeMode)
			&& Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null
			&& Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem()
				.getItem() instanceof ItemBow
			&& Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed)
		{
			new Thread(() -> {
				Minecraft.getMinecraft().playerController.sendUseItem(
						Minecraft.getMinecraft().thePlayer,
						Minecraft.getMinecraft().theWorld,
						Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
				Minecraft.getMinecraft().thePlayer.inventory
						.getCurrentItem()
						.getItem()
						.onItemRightClick(
								Minecraft.getMinecraft().thePlayer.inventory
										.getCurrentItem(),
								Minecraft.getMinecraft().theWorld,
								Minecraft.getMinecraft().thePlayer);
				String configKey = "Charge Level (1 - 20)";
				int iters = ModuleConfiguration.forModule(this).getInt(configKey, 3);
				if(iters > 20 || iters < 1) {
					iters = 1;
					Chat.sendWarning("BowSpam charge level range out of range, resetting!");
					ModuleConfiguration.forModule(this).putString(configKey, String.valueOf(iters));
				}
				for(int i = 0; i < iters; i++) {
					try {
						Thread.sleep(ModuleConfiguration.forModule(this).getInt("Delay between charges (ms)", 10));
					} catch (InterruptedException ignored) {}
					Minecraft.getMinecraft().thePlayer.sendQueue
							.addToSendQueue(new C03PacketPlayer(false));
				}
				Minecraft
						.getMinecraft()
						.getNetHandler()
						.addToSendQueue(
								new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM,
										new BlockPos(0, 0, 0), EnumFacing.DOWN));
				Minecraft.getMinecraft().thePlayer.inventory
						.getCurrentItem()
						.getItem()
						.onPlayerStoppedUsing(
								Minecraft.getMinecraft().thePlayer.inventory
										.getCurrentItem(),
								Minecraft.getMinecraft().theWorld,
								Minecraft.getMinecraft().thePlayer, 10);
			}).start();
		}
	}
	
	@Override
	public void onDisable()
	{
		WurstClient.INSTANCE.events.remove(UpdateListener.class, this);
	}
}
