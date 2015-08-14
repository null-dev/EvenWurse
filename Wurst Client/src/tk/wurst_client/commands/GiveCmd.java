package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.util.ResourceLocation;
import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.Info;
import tk.wurst_client.utils.MiscUtils;

@Info(help = "Gives you an item with custom NBT data in creative.",
name = "give",
syntax = {"<item name> <amount> <metadata> <NBT data>", "template <template number> <amount>", "templates"})
public class GiveCmd extends Cmd {

	private static class ItemTemplate {
		public Item item;
		public String name, tag;
		public int metadata;
		
		public ItemTemplate(String name, Item item, int metadata, String tag) {
			this.name = name;
			this.item = item;
			this.metadata = metadata;
			this.tag = tag;
		}
	}
	
	private static ItemTemplate[] templates = new ItemTemplate[] {
		new ItemTemplate("Knockback Stick", Items.stick, 0,
				"{ench: [{id:19, lvl:6}], display: {Name: §6Knockback Stick}, HideFlags: 63}"),
		new ItemTemplate("One Hit Sword", Items.diamond_sword, 0,
				"{AttributeModifiers:[{AttributeName:generic.attackDamage,Name:"
				+ "generic.attackDamage,Amount:2147483647,Operation:0,UUIDMost:246216,UUIDLeast:24636}], "
				+ "display: {Name: §6One Hitter}, Unbreakable: 1, HideFlags: 63}"),
		new ItemTemplate("Super Thorns Chestplate", Items.diamond_chestplate, 0,
				"{ench: [{id:7, lvl:32767}, {id: 0, lvl:32767}], AttributeModifiers:"
				+ "[{AttributeName:generic.maxHealth,Name:generic.maxHealth,Amount:200,Operation:0,"
				+ "UUIDMost:43631,UUIDLeast:2641}], display: "
				+ "{Name: §6Super Thorns Chestplate}, HideFlags: 63, Unbreakable: 1}"),
		new ItemTemplate("Super Potion", Items.potionitem, 0,
				"{CustomPotionEffects: [{Id:11, Amplifier: 127, Duration: 2147483647}, "
				+ "{Id:10, Amplifier: 127, Duration: 2147483647},"
				+ "{Id:23, Amplifier: 127, Duration: 2147483647},"
				+ "{Id:16, Amplifier: 0, Duration: 2147483647},"
				+ "{Id:8, Amplifier: 3, Duration: 2147483647},"
				+ "{Id:1, Amplifier: 5, Duration: 2147483647},"
				+ "{Id:5, Amplifier: 127, Duration: 2147483647}], display: "
				+ "{Name: §6Super Potion}, HideFlags: 63}"),
		new ItemTemplate("Griefer Potion", Items.potionitem, 0,
				"{CustomPotionEffects: [{Id:3, Amplifier: 127, Duration: 2147483647}],"
				+ "display: {Name: §6Griefer Potion}, HideFlags: 63}")
	};
	
	private int parseAmount(Item item, String amount) throws Error {
		if (!MiscUtils.isInteger(amount))
			syntaxError("Amount has to be an integer.");
		
		int out = Integer.valueOf(amount);
		
		if (out < 1)
			error("Amount has to be at least 1.");
		
		if (out > item.getItemStackLimit()) {
			error("Amount is larger than the maximum stack size.");
		}
		
		return out;
	}
	
	@Override
	public void execute(String[] args) throws Error {
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		if (!player.capabilities.isCreativeMode) {
			error("Creative mode only.");
		}
		
		Item item = null;
		int amount = -1;
		int metadata = 0;
		String nbt = null;
		
		if (args.length < 1)
			syntaxError();

		if (args[0].equalsIgnoreCase("templates")) {
			for (int i = 0; i < templates.length; i++) {
				ItemTemplate template = templates[i];
				WurstClient.INSTANCE.chat.message(String.format("§l§c%d§6: %s", i+1, template.name));
			}
			
			return;
		} else if (args[0].equalsIgnoreCase("template")) {
			if (args.length < 2 || args.length > 3)
				syntaxError();
			
			if (!MiscUtils.isInteger(args[1]))
				syntaxError("Template index has to be an integer.");
			
			int index = Integer.valueOf(args[1]);
			
			if (index < 1 || index > templates.length) {
				WurstClient.INSTANCE.chat.error("Template index out of range.");
				return;
			}
			
			ItemTemplate template = templates[index-1];
			item = template.item;
			amount = item.getItemStackLimit();
			nbt = template.tag;
			
			if (args.length == 3)
				amount = parseAmount(item, args[2]);
		} else {
			ResourceLocation loc = new ResourceLocation(args[0]);
			item = (Item)Item.itemRegistry.getObject(loc);
			
			if (item == null)
				error("This item does not exist.");		
			
			amount = item.getItemStackLimit();
			if (args.length >= 2) {
				amount = parseAmount(item, args[1]);
			}
			
			if (args.length >= 3) {
				if (!MiscUtils.isInteger(args[2]))
					syntaxError("Metadata has to be an integer.");
				
				metadata = Integer.valueOf(args[2]);
			}
			
			if (args.length >= 4) {
				try {
					nbt = CommandBase.getChatComponentFromNthArg(null, args, 3).getUnformattedText();
				} catch (CommandException e) {
					e.printStackTrace();
					error("An error occurred while parsing NBT data.");
				}
			}
		}
			
		ItemStack out = new ItemStack(item, amount, metadata);
		
		if (nbt != null) {
			try {
				out.setTagCompound(JsonToNBT.func_180713_a(nbt));
			} catch (NBTException e) {
				e.printStackTrace();
				error("An error occurred while parsing NBT data.");
			}
		}
		
		for (int i = 0; i<9; i++) {
			if (player.inventory.getStackInSlot(i) == null) {
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(
						new C10PacketCreativeInventoryAction(36+i, out));
				WurstClient.INSTANCE.chat.info("Your specified item has been placed into your hotbar.");
				return;
			}
		}
		
		error("Please clear a slot of your hotbar to use this command.");
	}

}
