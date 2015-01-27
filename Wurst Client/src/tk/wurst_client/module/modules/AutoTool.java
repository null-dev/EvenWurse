package tk.wurst_client.module.modules;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class AutoTool extends Module
{
	public AutoTool()
	{
		super
		(
			"AutoTool",
			"Automatically uses the best tool in your hotbar to\n"
			+ "mine blocks. Tip: This works with Nuker.",
			0,
			Category.BLOCKS
		);
	}
	
	private boolean isActive = false;
	private int oldSlot;
	
	public void onLeftClick()
	{
		if
		(
			!this.getToggled()
			|| Minecraft.getMinecraft().objectMouseOver == null
			|| Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null
		)
			return;
		if
		(
			Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air
		)
		{
			isActive = true;
			oldSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
			setSlot(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
		}
	}
	
	public static void setSlot(BlockPos blockPos)
	{
		float bestSpeed = 1F;
		int bestSlot = -1;
		Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
		for(int i = 0; i < 9; i++)
		{
			ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i);
			if(item == null)
				continue;
			float speed = item.getStrVsBlock(block);
			if(speed > bestSpeed)
			{
				bestSpeed = speed;
				bestSlot = i;
			}
		}
		if(bestSlot != -1)
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = bestSlot;
	}
	
	public void onUpdate()
	{
		if(!this.getToggled())
			return;
		if(!Minecraft.getMinecraft().gameSettings.keyBindAttack.pressed && isActive)
			onDisable();
		else if
		(
			this.getToggled()
			&& isActive
			&& Minecraft.getMinecraft().objectMouseOver != null
			&& Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null
			&& Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air
		)
			setSlot(Minecraft.getMinecraft().objectMouseOver.getBlockPos());
	}
	
	public void onDisable()
	{
		isActive = false;
		Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
	}
}
