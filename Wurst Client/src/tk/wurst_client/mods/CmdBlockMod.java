/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import tk.wurst_client.WurstClient;
import tk.wurst_client.gui.mods.GuiCmdBlock;

@Mod.Info(category = Mod.Category.EXPLOITS,
	description = "Allows you to make a Command Block without having OP.\n"
		+ "Appears to be patched on Spigot.",
	name = "CMD-Block")
public class CmdBlockMod extends Mod
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
		Minecraft.getMinecraft().displayGuiScreen(
			new GuiCmdBlock(this, Minecraft.getMinecraft().currentScreen));
		setEnabled(false);
	}
	
	public void createCmdBlock(String cmd)
	{
		ItemStack stack = new ItemStack(Blocks.command_block);
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		nbtTagCompound.setTag("Command", new NBTTagString(cmd));
		stack.writeToNBT(nbtTagCompound);
		stack.setTagInfo("BlockEntityTag", nbtTagCompound);
		Minecraft.getMinecraft().thePlayer.sendQueue
			.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
		WurstClient.INSTANCE.chat.message("Command Block created.");
	}
}
