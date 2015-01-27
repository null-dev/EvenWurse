package tk.wurst_client.module.modules;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.RenderUtils;

public class Overlay extends Module
{
	public Overlay()
	{
		super
		(
			"Overlay",
			"Renders the Nuker animation when you mine a block.",
			0,
			Category.RENDER
		);
	}
	
	public void onRender()
	{
		if(!this.getToggled() || Minecraft.getMinecraft().objectMouseOver == null || Minecraft.getMinecraft().objectMouseOver.typeOfHit != MovingObjectType.BLOCK)
			return;
		BlockPos pos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();
		Block mouseOverBlock = Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock();
		if(Block.getIdFromBlock(mouseOverBlock) != 0)
		{
			RenderUtils.nukerBox(pos, PlayerControllerMP.curBlockDamageMP);
		}
	}
}
