/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.WurstClient;
import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.MISC,
	description = "Gives you a ForceOP Sign. Place & click it to get OP.\n"
		+ "Patched in Minecraft 1.8.6! Only works on servers running 1.8.5 and\n"
		+ "older versions.",
	name = "OP Sign")
public class OpSignMod extends Mod
{
	@Override
	public void onEnable()
	{
		if(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(0) != null
			|| !Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
		{
			WurstClient.INSTANCE.chat
				.error("Please clear the first slot in your hotbar.");
			setEnabled(false);
			return;
		}
		ItemStack stack =
			new ItemStack(Item.getByNameOrId("minecraft:sign"), 1);
		NBTBase nbtTest = null;
		try
		{
			nbtTest =
				JsonToNBT
					.func_180713_a("{BlockEntityTag:{Text1:\"{text:\\\"\\\",clickEvent:{action:run_command,value:\\\"/op "
						+ Minecraft.getMinecraft().session.getUsername()
						+ "\\\"}}\",},}");
		}catch(NBTException e)
		{
			throw new IllegalStateException("Failed to create NBT data.", e);
		}
		stack.setTagCompound((NBTTagCompound)nbtTest);
		Minecraft.getMinecraft().thePlayer.sendQueue
			.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
		WurstClient.INSTANCE.chat
			.message("OP Sign created. Place & click it to get OP.");
		setEnabled(false);
	}
}
