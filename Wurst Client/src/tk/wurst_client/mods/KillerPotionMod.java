/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.WurstClient;

@Mod.Info(category = Mod.Category.EXPLOITS,
	description = "Generates a potion that can kill players in Creative mode.\n"
		+ "Requires Creative mode.",
	name = "KillerPotion")
public class KillerPotionMod extends Mod
{
	@Override
	public void onEnable()
	{
		if(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(0) != null)
		{
			WurstClient.INSTANCE.chat
				.error("Please clear the first slot in your hotbar.");
			setEnabled(false);
			return;
		}else if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
		{
			WurstClient.INSTANCE.chat.error("Creative mode only.");
			setEnabled(false);
			return;
		}
		
		ItemStack stack = new ItemStack(Items.potionitem);
		stack.setItemDamage(16384);
		NBTTagList effects = new NBTTagList();
		NBTTagCompound effect = new NBTTagCompound();
		effect.setInteger("Amplifier", 125);
		effect.setInteger("Duration", 2000);
		effect.setInteger("Id", 6);
		effects.appendTag(effect);
		stack.setTagInfo("CustomPotionEffects", effects);
		stack.setStackDisplayName("§c§lKiller§6§lPotion");
		
		Minecraft.getMinecraft().thePlayer.sendQueue
			.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
		WurstClient.INSTANCE.chat.message("Potion created.");
		setEnabled(false);
	}
}
