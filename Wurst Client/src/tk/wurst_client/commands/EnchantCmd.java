/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import tk.wurst_client.Client;
import tk.wurst_client.commands.Cmd.Info;

@Info(help = "Enchants items with everything.",
	name = "enchant",
	syntax = {"[all]"})
public class EnchantCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		if(args.length == 0)
		{
			ItemStack currentItem =
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
			if(currentItem == null)
				error("There is no item in your hand.");
			for(Enchantment enchantment : Enchantment.enchantmentsList)
				try
				{
					if(enchantment == Enchantment.silkTouch)
						continue;
					currentItem.addEnchantment(enchantment, 127);
				}catch(Exception e)
				{	
					
				}
		}else if(args[0].equals("all"))
		{
			int items = 0;
			for(int i = 0; i < 40; i++)
			{
				ItemStack currentItem =
					Minecraft.getMinecraft().thePlayer.inventory
						.getStackInSlot(i);
				if(currentItem == null)
					continue;
				items++;
				for(Enchantment enchantment : Enchantment.enchantmentsList)
					try
					{
						if(enchantment == Enchantment.silkTouch)
							continue;
						currentItem.addEnchantment(enchantment, 127);
					}catch(Exception e)
					{	
						
					}
			}
			if(items == 1)
				Client.wurst.chat.message("Enchanted 1 item.");
			else
				Client.wurst.chat.message("Enchanted " + items + " items.");
		}else
			syntaxError();
	}
}
