/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class AutoArmor extends Module
{
	private int[] bestArmor;

	public AutoArmor()
	{
		super("AutoArmor",
			"Manages your armor automatically.",
			0,
			Category.COMBAT);
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled() || Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
			return;
		updateMS();
		if(hasTimePassedM(3000))
		{
			bestArmor = new int[4];
			for(int i = 0; i < bestArmor.length; i++)
				bestArmor[i] = -1;
			for(int i = 0; i < 36; i++)
			{
				ItemStack itemstack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
				if(itemstack != null && itemstack.getItem() instanceof ItemArmor)
				{
					ItemArmor armor = (ItemArmor)itemstack.getItem();
					if(armor.damageReduceAmount > bestArmor[3 - armor.armorType])
						bestArmor[3 - armor.armorType] = i;
				}
			}
			for(int i = 0; i < 4; i++)
			{
				ItemStack itemstack = Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(i);
				ItemArmor currentArmor;
				if(itemstack != null)
					currentArmor = ((ItemArmor)itemstack.getItem());
				else
					currentArmor = null;
				ItemArmor bestArmor;
				try
				{
					bestArmor = (ItemArmor)Minecraft.getMinecraft().thePlayer
						.inventory.getStackInSlot(this.bestArmor[i]).getItem();
				}catch(Exception e)
				{
					bestArmor = null;
				}
				if(bestArmor != null && (currentArmor == null
					|| bestArmor.damageReduceAmount > currentArmor.damageReduceAmount))
				{
					if(Minecraft.getMinecraft().thePlayer.inventory.getFirstEmptyStack() != -1 || currentArmor == null)
					{
						if(currentArmor != null)
							Minecraft.getMinecraft().playerController.windowClick(0, 8 - i, 0, 1, Minecraft.getMinecraft().thePlayer);
						Minecraft.getMinecraft().playerController.windowClick(0, this.bestArmor[i] < 9 ? 36 + this.bestArmor[i] : this.bestArmor[i], 0, 1, Minecraft.getMinecraft().thePlayer);
					}
				}
			}
			updateLastMS();
		}
	}
}
