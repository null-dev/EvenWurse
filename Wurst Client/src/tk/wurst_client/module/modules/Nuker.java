package tk.wurst_client.module.modules;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.lwjgl.input.Keyboard;

import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.BlockUtils;
import tk.wurst_client.utils.RenderUtils;

public class Nuker extends Module
{
	public Nuker()
	{
		super
		(
			"Nuker",
			"Destroys blocks around you.\n"
			+ "Use .nuker mode <mode> to change the mode.",
			Keyboard.KEY_L,
			Category.BLOCKS
		);
	}
	
	public static float normalRange = 5F;
	public static float yesCheatRange = 4.25F;
	private float realRange;
	private static Block currentBlock;
	private float currentDamage;
	private EnumFacing side = EnumFacing.UP;
	private byte blockHitDelay = 0;
	public static int id = 0;
	private BlockPos pos;
	private boolean shouldRenderESP;
	private int oldSlot = -1;
	
	public String getRenderName()
	{
		if(Client.Wurst.options.nukerMode == 1)
			return "IDNuker [" + id + "]";
		else if(Client.Wurst.options.nukerMode == 2)
			return "FlatNuker";
		else if(Client.Wurst.options.nukerMode == 3)
			return "SmashNuker";
		else
			return "Nuker";
	}
	
	public void initSliders()
	{
		this.moduleSliders.add(new BasicSlider("Nuker range", normalRange, 1, 6, 0.05, ValueDisplay.DECIMAL));
	}
	
	public void updateSettings()
	{
		this.normalRange = (float) this.moduleSliders.get(0).getValue();
		this.yesCheatRange = Math.min(normalRange, 4.25F);
	}
	
	public void onEnable()
	{
		if(Client.Wurst.moduleManager.getModuleFromClass(NukerLegit.class).getToggled())
			Client.Wurst.moduleManager.getModuleFromClass(NukerLegit.class).setToggled(false);
		if(Client.Wurst.moduleManager.getModuleFromClass(SpeedNuker.class).getToggled())
			Client.Wurst.moduleManager.getModuleFromClass(SpeedNuker.class).setToggled(false);
	}
	
	public void onLeftClick()
	{
		if
		(
			!this.getToggled()
			|| Minecraft.getMinecraft().objectMouseOver == null
			|| Minecraft.getMinecraft().objectMouseOver.getBlockPos() == null
		)
			return;
		if(Client.Wurst.options.nukerMode == 1 && Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock().getMaterial() != Material.air)
		{
			id = Block.getIdFromBlock(Minecraft.getMinecraft().theWorld.getBlockState(Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock());
			Client.Wurst.fileManager.saveOptions();
		}
	}
	
	public void onRender()
	{
		if (blockHitDelay == 0 && shouldRenderESP)
		{
			if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode && currentBlock.getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, pos) < 1)
				RenderUtils.nukerBox(pos, currentDamage);
			else
				RenderUtils.nukerBox(pos, 1);
		}
	}
	
	public void onUpdate()
	{
		if(!this.getToggled())
			return;
		if(Client.Wurst.moduleManager.getModuleFromClass(YesCheat.class).getToggled())
		{
			realRange = yesCheatRange;
		}else
		{
			realRange = normalRange;
		}
		shouldRenderESP = false;
		BlockPos newPos = find();
		if(newPos == null)
		{
			if(oldSlot != -1)
			{
				Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
				oldSlot = -1;
			}
			return;
		}
		if(pos == null || !pos.equals(newPos))
			currentDamage = 0;
		pos = newPos;
		currentBlock = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
		if (blockHitDelay > 0)
		{
			blockHitDelay--;
			return;
		}
		BlockUtils.faceBlockPacket(pos);
		if(currentDamage == 0)
		{
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, side));
			if(Client.Wurst.moduleManager.getModuleFromClass(AutoTool.class).getToggled() && oldSlot == -1)
				oldSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
			if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode || currentBlock.getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, pos) >= 1)
			{
				currentDamage = 0;
				if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode && !Client.Wurst.moduleManager.getModuleFromClass(YesCheat.class).getToggled())
				{
					nukeAll();
				}else
				{
					shouldRenderESP = true;
					Minecraft.getMinecraft().thePlayer.swingItem();
					Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(pos, side);
				}
				return;
			}
		}
		if(Client.Wurst.moduleManager.getModuleFromClass(AutoTool.class).getToggled())
			AutoTool.setSlot(pos);
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
		shouldRenderESP = true;
		BlockUtils.faceBlockPacket(pos);
		currentDamage += currentBlock.getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, pos) * (Client.Wurst.moduleManager.getModuleFromClass(FastBreak.class).getToggled() && Client.Wurst.options.fastbreakMode == 0 ? FastBreak.speed : 1);
		Minecraft.getMinecraft().theWorld.sendBlockBreakProgress(Minecraft.getMinecraft().thePlayer.getEntityId(), pos, (int)(this.currentDamage * 10.0F) - 1);
		if (currentDamage >= 1)
		{
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, side));
			Minecraft.getMinecraft().playerController.onPlayerDestroyBlock(pos, side);
			blockHitDelay = (byte) (4);
			currentDamage = 0;
		}else if(Client.Wurst.moduleManager.getModuleFromClass(FastBreak.class).getToggled() && Client.Wurst.options.fastbreakMode == 1)
        {
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, side));
        }
	}
	
	public void onDisable()
	{
		if(oldSlot != -1)
		{
			Minecraft.getMinecraft().thePlayer.inventory.currentItem = oldSlot;
			oldSlot = -1;
		}
		currentDamage = 0;
		shouldRenderESP = false;
		id = 0;
		Client.Wurst.fileManager.saveOptions();
	}
	
	private BlockPos find()
	{
		BlockPos closest = null;
		float closestDistance = realRange + 1;
		for(int y = (int) realRange; y >= (Client.Wurst.options.nukerMode == 2 ? 0 : -realRange); y--)
		{
			for(int x = (int) realRange; x >= -realRange - 1; x--)
			{
				for(int z = (int) realRange; z >= -realRange; z--)
				{
					if(Minecraft.getMinecraft().thePlayer == null)
						continue;
					int posX = (int) (Math.floor(Minecraft.getMinecraft().thePlayer.posX) + x);
					int posY = (int) (Math.floor(Minecraft.getMinecraft().thePlayer.posY) + y);
					int posZ = (int) (Math.floor(Minecraft.getMinecraft().thePlayer.posZ) + z);
					BlockPos blockPos = new BlockPos(posX, posY, posZ);
					Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
					float xDiff = (float)(Minecraft.getMinecraft().thePlayer.posX - posX);
					float yDiff = (float)(Minecraft.getMinecraft().thePlayer.posY - posY);
					float zDiff = (float)(Minecraft.getMinecraft().thePlayer.posZ - posZ);
					float currentDistance = BlockUtils.getBlockDistance(xDiff, yDiff, zDiff);
					MovingObjectPosition fakeObjectMouseOver = Minecraft.getMinecraft().objectMouseOver;
					if(fakeObjectMouseOver == null)
						continue;
					fakeObjectMouseOver.setBlockPos(blockPos);
					if(Block.getIdFromBlock(block) != 0 && posY >= 0 && currentDistance <= realRange)
					{
						if(Client.Wurst.options.nukerMode == 1 && Block.getIdFromBlock(block) != id)
							continue;
						if(Client.Wurst.options.nukerMode == 3 && block.getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, blockPos) < 1)
							continue;
						side = fakeObjectMouseOver.sideHit;
						if(closest == null)
						{
							closest = blockPos;
							closestDistance = currentDistance;
						}else if(currentDistance < closestDistance)
						{
							closest = blockPos;
							closestDistance = currentDistance;
						}
					}
				}
			}
		}
		return closest;
	}
	
	private void nukeAll()
	{
		for(int y = (int) realRange; y >= (Client.Wurst.options.nukerMode == 2 ? 0 : -realRange); y--)
		{
			for(int x = (int) realRange; x >= -realRange - 1; x--)
			{
				for(int z = (int) realRange; z >= -realRange; z--)
				{
					int posX = (int) (Math.floor(Minecraft.getMinecraft().thePlayer.posX) + x);
					int posY = (int) (Math.floor(Minecraft.getMinecraft().thePlayer.posY) + y);
					int posZ = (int) (Math.floor(Minecraft.getMinecraft().thePlayer.posZ) + z);
					BlockPos blockPos = new BlockPos(posX, posY, posZ);
					Block block = Minecraft.getMinecraft().theWorld.getBlockState(blockPos).getBlock();
					float xDiff = (float)(Minecraft.getMinecraft().thePlayer.posX - posX);
					float yDiff = (float)(Minecraft.getMinecraft().thePlayer.posY - posY);
					float zDiff = (float)(Minecraft.getMinecraft().thePlayer.posZ - posZ);
					float currentDistance = BlockUtils.getBlockDistance(xDiff, yDiff, zDiff);
					MovingObjectPosition fakeObjectMouseOver = Minecraft.getMinecraft().objectMouseOver;
					fakeObjectMouseOver.setBlockPos(blockPos);
					if(Block.getIdFromBlock(block) != 0 && posY >= 0 && currentDistance <= realRange)
					{
						if(Client.Wurst.options.nukerMode == 1 && Block.getIdFromBlock(block) != id)
							continue;
						if(Client.Wurst.options.nukerMode == 3 && block.getPlayerRelativeBlockHardness(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, blockPos) < 1)
							continue;
						side = fakeObjectMouseOver.sideHit;
						shouldRenderESP = true;
						BlockUtils.faceBlockPacket(pos);
						Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, blockPos, side));
						block.onBlockDestroyedByPlayer(Minecraft.getMinecraft().theWorld, blockPos, Minecraft.getMinecraft().theWorld.getBlockState(blockPos));
					}
				}
			}
		}
	}
}
