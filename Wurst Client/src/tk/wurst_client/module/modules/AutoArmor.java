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
			"Manages your armor automatically",
			0,
			Category.COMBAT);
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		updateMS();
		if(hasTimePassedM(3000))
		{
			bestArmor = new int[4];
			for(int i = 0; i < 36; i++)
			{
				ItemStack itemstack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
				if(itemstack != null && itemstack.getItem() instanceof ItemArmor)
				{
					ItemArmor armor = (ItemArmor)itemstack.getItem();
					if(armor.damageReduceAmount > bestArmor[armor.armorType])
						bestArmor[armor.armorType] = i;
				}
			}
			for(int i = 0; i < 3; i++)
			{
				ItemStack itemstack = Minecraft.getMinecraft().thePlayer.inventory.armorItemInSlot(i);
				
			}
			updateLastMS();
		}
	}
}
