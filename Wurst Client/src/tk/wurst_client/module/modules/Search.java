package tk.wurst_client.module.modules;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.RenderUtils;

public class Search extends Module
{
	public Search()
	{
		super
		(
			"Search",
			"Helps you to find specific blocks.\n"
			+ "Use .search id <block id> or .search name <block name>\n"
			+ "to specify it.",
			0,
			Category.RENDER
		);
	}
	
	private ArrayList<BlockPos> matchingBlocks = new ArrayList<BlockPos>();
	private int range = 50;
	private int maxBlocks = 1000;
	public static boolean shouldInform = true;
	
	public String getRenderName()
	{
		return this.getName() + " [" + Client.Wurst.options.searchID + "]";
	}
	
	public void onEnable()
	{
		shouldInform = true;
	}
	
	public void onRender()
	{
		if(!this.getToggled())
			return;
		for(BlockPos blockPos : matchingBlocks)
		{
			RenderUtils.searchBox(blockPos);
		}
	}
	
	public void onUpdate()
	{
		if(!this.getToggled())
			return;
		updateMS();
		if(hasTimePassedM(3000))
		{
			matchingBlocks.clear();
			for(int y = range; y >= -range; y--)
			{
				for(int x = range; x >= -range; x--)
				{
					for(int z = range; z >= -range; z--)
					{
						int posX = (int) (Minecraft.getMinecraft().thePlayer.posX + x);
						int posY = (int) (Minecraft.getMinecraft().thePlayer.posY + y);
						int posZ = (int) (Minecraft.getMinecraft().thePlayer.posZ + z);
						BlockPos pos = new BlockPos(posX, posY, posZ);
						if(Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock()) == Client.Wurst.options.searchID)
							matchingBlocks.add(pos);
						if(matchingBlocks.size() >= maxBlocks)
							break;
					}
					if(matchingBlocks.size() >= maxBlocks)
						break;
				}
				if(matchingBlocks.size() >= maxBlocks)
					break;
			}
			if(matchingBlocks.size() >= maxBlocks && shouldInform)
			{
				Client.Wurst.chat.warning(getName() + " found §lA LOT§r of blocks.");
				Client.Wurst.chat.message("To prevent lag, it will only show the first " + maxBlocks + " blocks.");
				shouldInform = false;
			}else if(matchingBlocks.size() < maxBlocks)
				shouldInform = true;
			updateLastMS();
		}
	}
}
