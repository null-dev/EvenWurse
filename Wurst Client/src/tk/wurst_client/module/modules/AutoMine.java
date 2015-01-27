package tk.wurst_client.module.modules;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class AutoMine extends Module
{
	public AutoMine()
	{
		super
		(
			"AutoMine",
			"Automatically mines a block as soon as you look at it.",
			0,
			Category.BLOCKS
		);
	}
	
	public void onEnable()
	{
		Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
	}
	
	public void onUpdate()
	{
		if
		(
			!this.getToggled()
			|| Minecraft.getMinecraft().objectMouseOver == null
			|| Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null
		)
			return;
		if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock()) != 0)
			Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = true;
		else
			Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
		System.out.println(Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed);
	}
	
	public void onDisable()
	{
		Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed = false;
	}
}
