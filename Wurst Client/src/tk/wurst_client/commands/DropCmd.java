/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.Client;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.mod.mods.YesCheat;

@Info(help = "Drops all your items on the ground.",
	name = "drop",
	syntax = {"[infinite]"})
public class DropCmd extends Cmd implements UpdateListener
{
	private int timer;
	private int counter;
	private boolean infinite;
	
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length > 1)
			syntaxError();
		if(args.length == 1)
			if(args[0].equalsIgnoreCase("infinite"))
				infinite = !infinite;
			else
				syntaxError();
		else
			infinite = false;
		timer = 0;
		counter = 9;
		EventManager.update.addListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(infinite)
		{
			Item item = null;
			while(item == null)
				item = Item.getItemById(new Random().nextInt(431));
			Minecraft.getMinecraft().thePlayer.sendQueue
				.addToSendQueue(new C10PacketCreativeInventoryAction(-1,
					new ItemStack(item, 64)));
			return;
		}
		if(Client.wurst.modManager.getModByClass(YesCheat.class)
			.isEnabled())
		{
			timer++;
			if(timer >= 5)
			{
				Minecraft.getMinecraft().playerController.windowClick(0,
					counter, 1, 4, Minecraft.getMinecraft().thePlayer);
				counter++;
				timer = 0;
				if(counter >= 45)
					EventManager.update.removeListener(this);
			}
		}else
		{
			for(int i = 9; i < 45; i++)
				Minecraft.getMinecraft().playerController.windowClick(0, i, 1,
					4, Minecraft.getMinecraft().thePlayer);
			EventManager.update.removeListener(this);
		}
	}
}
