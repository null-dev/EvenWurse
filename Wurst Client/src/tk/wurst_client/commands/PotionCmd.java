package tk.wurst_client.commands;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import tk.wurst_client.commands.Cmd;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.utils.MiscUtils;

@Info (help="Changes the efects of held potion. Accepts arguments in groups of 3.",
	name="potion",
	syntax = {"<id> <amplifier> <duration>"})

public class PotionCmd extends Cmd {

	@Override
	public void execute(String[] args) throws Error {
		
		if(args.length == 0 || args.length % 3 != 0) 
			syntaxError();
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		ItemStack currentItem = 
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
		if(currentItem == null || Item.getIdFromItem(currentItem.getItem()) != 373)
			error("Please execute this command while holding a potion.");
		for(int i = 0; i < args.length; i++) {
			if(!MiscUtils.isInteger(args[i])) 
				syntaxError();
		}
		NBTTagList effectList = new NBTTagList();
		for(int x = 0; x < args.length / 3; x++) {
			int id = Integer.parseInt(args[0+(x*3)]);
			int amplifier = Integer.parseInt(args[1+(x*3)]);
			int duration = Integer.parseInt(args[2+(x*3)]);
			NBTTagCompound effect = new NBTTagCompound();
			effect.setInteger("Id",id);
			effect.setInteger("Amplifier",amplifier);
			effect.setInteger("Duration",duration);
			effectList.appendTag(effect);
		}
		currentItem.setTagInfo("CustomPotionEffects", effectList);
	}
}
