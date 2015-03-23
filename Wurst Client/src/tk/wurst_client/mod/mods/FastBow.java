/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mod.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mod.Mod;
import tk.wurst_client.mod.Mod.Category;
import tk.wurst_client.mod.Mod.Info;

@Info(category = Category.COMBAT,
	description = "Turns your bow into a machine gun.\n"
		+ "Tip: This works with BowAimbot.",
	name = "FastBow")
public class FastBow extends Mod implements UpdateListener
{
	@Override
	public void onEnable()
	{
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getModByClass(YesCheat.class)
			.isEnabled())
		{
			noCheatMessage();
			setEnabled(false);
			return;
		}
		if(Minecraft.getMinecraft().thePlayer.getHealth() > 0
			&& (Minecraft.getMinecraft().thePlayer.onGround || Minecraft
				.getMinecraft().thePlayer.capabilities.isCreativeMode)
			&& Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null
			&& Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem()
				.getItem() instanceof ItemBow
			&& Minecraft.getMinecraft().gameSettings.keyBindUseItem.pressed)
		{
			Minecraft.getMinecraft().playerController.sendUseItem
				(
					Minecraft.getMinecraft().thePlayer,
					Minecraft.getMinecraft().theWorld,
					Minecraft.getMinecraft().thePlayer.inventory
						.getCurrentItem()
				);
			Minecraft.getMinecraft().thePlayer.inventory
				.getCurrentItem()
				.getItem()
				.onItemRightClick
				(
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					Minecraft.getMinecraft().theWorld,
					Minecraft.getMinecraft().thePlayer
				);
			for(int i = 0; i < 20; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue
					.addToSendQueue(new C03PacketPlayer(false));
			Minecraft
				.getMinecraft()
				.getNetHandler()
				.addToSendQueue(
					new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM,
						new BlockPos(0, 0, 0), EnumFacing.DOWN));
			Minecraft.getMinecraft().thePlayer.inventory
				.getCurrentItem()
				.getItem()
				.onPlayerStoppedUsing
				(
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					Minecraft.getMinecraft().theWorld,
					Minecraft.getMinecraft().thePlayer,
					10
				);
		}
	}
	
	@Override
	public void onDisable()
	{
		EventManager.update.removeListener(this);
	}
}
