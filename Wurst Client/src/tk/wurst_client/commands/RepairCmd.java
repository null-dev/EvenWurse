/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.WurstClient;

@Cmd.Info(help = "Repairs the held item. Requires creative mode.",
	name = "repair",
	syntax = {})
public class RepairCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length > 0)
			syntaxError();
		
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		
		if(!player.capabilities.isCreativeMode)
			error("Creative mode only.");
		
		ItemStack heldItem = player.inventory.getCurrentItem();
		
		if (heldItem == null)
			error("You need an item in your hand.");
		
		if (!heldItem.isItemStackDamageable())
			error("This item can't take damage.");
		
		if (!heldItem.isItemDamaged())
			error("This item is not damaged.");
		
		heldItem.setItemDamage(0);
		
		player.sendQueue.addToSendQueue(
				new C10PacketCreativeInventoryAction(
						36+player.inventory.currentItem, heldItem));
		
		WurstClient.INSTANCE.chat.info("Your item has been repaired.");
	}
}
