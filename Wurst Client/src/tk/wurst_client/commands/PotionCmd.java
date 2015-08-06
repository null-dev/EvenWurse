package tk.wurst_client.commands;

import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.utils.MiscUtils;

@Info (help="Add to/sets the efects of the held potion. Can also remove potion effects the from held potion. "
		+ "Accepts arguments in groups of 3 for add and set modes.",
	name="potion",
	syntax = {"[add] <id> <amplifier> <duration>", "[set] <id> <amplifier> <duration>", "[remove] <id>"})

public class PotionCmd extends Cmd {

	@Override
	public void execute(String[] args) throws Error {
		
		if(args.length == 0)
			syntaxError();
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
			error("Creative mode only.");
		ItemStack currentItem = 
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
		if(currentItem == null || Item.getIdFromItem(currentItem.getItem()) != 373)
			error("Please execute this command while holding a potion.");
		NBTTagList effectList = new NBTTagList();
		if(args.length == 2) {
			if(!args[0].equalsIgnoreCase("Remove"))
				syntaxError();
			int id = 0;
				try {
					id = PotionCmd.parsePotionEffectId(args[1]);
				}
				catch(NullPointerException e) {
					syntaxError();
				}
			List oldEffects = new ItemPotion().getEffects(currentItem);
			for(int i = 0; i < oldEffects.size(); i++) {
				PotionEffect temp = (PotionEffect) oldEffects.get(i);
				if(temp.getPotionID() != id) {
					NBTTagCompound effect = new NBTTagCompound();
					effect.setInteger("Id", temp.getPotionID());
					effect.setInteger("Amplifier", temp.getAmplifier());
					effect.setInteger("Duration", temp.getDuration());
					effectList.appendTag(effect);
				}
			}
			currentItem.setTagInfo("CustomPotionEffects", effectList);
			return;
		}
		else if((args.length - 1) % 3 != 0)
			syntaxError();
		if(args[0].equalsIgnoreCase("add")) {
			List oldEffects = new ItemPotion().getEffects(currentItem);
			for(int i = 0; i < oldEffects.size(); i++) {
				PotionEffect temp = (PotionEffect) oldEffects.get(i);
				NBTTagCompound effect = new NBTTagCompound();
				effect.setInteger("Id", temp.getPotionID());
				effect.setInteger("Amplifier", temp.getAmplifier());
				effect.setInteger("Duration", temp.getDuration());
				effectList.appendTag(effect);
			}
		}
		else if(!args[0].equalsIgnoreCase("set"))
			syntaxError();
		int id = 0;
		int amplifier = 0;
		int duration = 0;
		for(int i = 0; i < (args.length - 1) / 3; i++) {
			try {
				id = parsePotionEffectId(args[1+(i*3)]);
			}
			catch(NullPointerException e) {
				syntaxError();
			}
			if(MiscUtils.isInteger(args[2+(i*3)]) && MiscUtils.isInteger((args[3+(i*3)]))) {
				amplifier = Integer.parseInt(args[2+(i*3)]);
				duration = Integer.parseInt(args[3+(i*3)]);
			}
			else
				syntaxError();
			NBTTagCompound effect = new NBTTagCompound();
			effect.setInteger("Id", id);
			effect.setInteger("Amplifier", amplifier);
			effect.setInteger("Duration", duration*20);
			effectList.appendTag(effect);
		}
		currentItem.setTagInfo("CustomPotionEffects", effectList);
	}
	public static int parsePotionEffectId(String input) throws NullPointerException {
		if(input.equalsIgnoreCase("Swiftness") || input.equalsIgnoreCase("Speed") ||
				input.equalsIgnoreCase("1"))
			return 1;
		else if(input.equalsIgnoreCase("Slowness") || input.equalsIgnoreCase("Slow") ||
				input.equalsIgnoreCase("2"))
			return 2;
		else if(input.equalsIgnoreCase("Haste") || input.equalsIgnoreCase("3"))
			return 3;
		else if(input.equalsIgnoreCase("MiningFatigue") || input.equalsIgnoreCase("Fatigue") ||
				input.equalsIgnoreCase("4"))
			return 4;
		else if(input.equalsIgnoreCase("Strength") || input.equalsIgnoreCase("Power") ||
				input.equalsIgnoreCase("5"))
			return 5;
		else if(input.equalsIgnoreCase("InstantHealth") || input.equalsIgnoreCase("Heal") ||
				input.equalsIgnoreCase("6"))
			return 6;
		else if(input.equalsIgnoreCase("InstantDamage") || input.equalsIgnoreCase("Damage") ||
				input.equalsIgnoreCase("7"))
			return 7;
		else if(input.equalsIgnoreCase("JumpBoost") || input.equalsIgnoreCase("Jump") ||
				input.equalsIgnoreCase("8"))
			return 8;
		else if(input.equalsIgnoreCase("Nausea") || input.equalsIgnoreCase("Sickness") ||
				input.equalsIgnoreCase("9"))
			return 9;
		else if(input.equalsIgnoreCase("Regeneration") || input.equalsIgnoreCase("Regen") ||
				input.equalsIgnoreCase("10"))
			return 10;
		else if(input.equalsIgnoreCase("Resistance") || input.equalsIgnoreCase("Resist") ||
				input.equalsIgnoreCase("Defence") || input.equalsIgnoreCase("11"))
			return 11;
		else if(input.equalsIgnoreCase("FireResistance") || input.equalsIgnoreCase("FireResist") ||
				input.equalsIgnoreCase("12"))
			return 12;
		else if(input.equalsIgnoreCase("WaterBreathing") || input.equalsIgnoreCase("Breathing") ||
				input.equalsIgnoreCase("13"))
			return 13;
		else if(input.equalsIgnoreCase("Invisibility") || input.equalsIgnoreCase("Invisible") ||
				input.equalsIgnoreCase("Invis") || input.equalsIgnoreCase("14"))
			return 14;
		else if(input.equalsIgnoreCase("Blindness") || input.equalsIgnoreCase("Blind") ||
				input.equalsIgnoreCase("15"))
			return 15;
		else if(input.equalsIgnoreCase("NightVision") || input.equalsIgnoreCase("Vision") ||
				input.equalsIgnoreCase("16"))
			return 16;
		else if(input.equalsIgnoreCase("Hunger") || input.equalsIgnoreCase("17"))
			return 17;
		else if(input.equalsIgnoreCase("Weakness") || input.equalsIgnoreCase("Weaken") ||
				input.equalsIgnoreCase("Weak") || input.equalsIgnoreCase("18"))
			return 18;
		else if(input.equalsIgnoreCase("Poison") || input.equalsIgnoreCase("19"))
			return 19;
		else if(input.equalsIgnoreCase("Wither") || input.equalsIgnoreCase("20"))
			return 20;
		else if(input.equalsIgnoreCase("HealthBoost") || input.equalsIgnoreCase("21"))
			return 21;
		else if(input.equalsIgnoreCase("Absorption") || input.equalsIgnoreCase("Absorb") ||
				input.equalsIgnoreCase("22"))
			return 22;
		else if(input.equalsIgnoreCase("Saturation") || input.equalsIgnoreCase("Food") ||
				input.equalsIgnoreCase("Feed") || input.equalsIgnoreCase("23"))
			return 23;
		else
			throw new NullPointerException();
	}
}
