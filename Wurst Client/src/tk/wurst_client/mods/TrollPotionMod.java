/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
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

@Mod.Info(category = Mod.Category.FUN,
	description = "Generates an incredibly annoying potion.\n"
		+ "Tip: AntiBlind makes you partially immune to it.",
	name = "TrollPotion")
public class TrollPotionMod extends Mod
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
		for(int i = 1; i <= 23; i++)
		{
			NBTTagCompound effect = new NBTTagCompound();
			effect.setInteger("Amplifier", Integer.MAX_VALUE);
			effect.setInteger("Duration", Integer.MAX_VALUE);
			effect.setInteger("Id", i);
			effects.appendTag(effect);
		}
		stack.setTagInfo("CustomPotionEffects", effects);
		stack.setStackDisplayName("§c§lTroll§6§lPotion");
		Minecraft.getMinecraft().thePlayer.sendQueue
			.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
		WurstClient.INSTANCE.chat.message("Potion created. Trololo!");
		setEnabled(false);
	}
}
