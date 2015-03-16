/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;

import tk.wurst_client.Client;
import tk.wurst_client.event.EventManager;
import tk.wurst_client.event.listeners.UpdateListener;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Mod;
import tk.wurst_client.utils.BlockUtils;

public class Kaboom extends Mod implements UpdateListener
{
	private int range = 6;
	public static int power = 128;
	
	public Kaboom()
	{
		super(
			"Kaboom",
			"Breaks blocks around you like an explosion.\n"
				+ "This can be a lot faster than Nuker if the server\n"
				+ "doesn't have NoCheat+. It works best with fast tools\n"
				+ "and weak blocks.\n"
				+ "Note that this is not an actual explosion.",
			Category.BLOCKS);
	}
	
	@Override
	public void initSliders()
	{
		sliders.add(new BasicSlider("Kaboom power", power, 32, 512, 32,
			ValueDisplay.INTEGER));
	}
	
	@Override
	public void updateSettings()
	{
		power = (int)sliders.get(0).getValue();
	}
	
	@Override
	public void onEnable()
	{
		EventManager.addUpdateListener(this);
	}
	
	@Override
	public void onUpdate()
	{
		if(Client.wurst.modManager.getMod(YesCheat.class)
			.getToggled())
		{
			noCheatMessage();
			setToggled(false);
			return;
		}
		if(Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
		{
			Client.wurst.chat.error("Surivival mode only.");
			setToggled(false);
			return;
		}
		new Thread("Kaboom")
		{
			@Override
			public void run()
			{
				for(int y = range; y >= -range; y--)
				{
					new Explosion(
						Minecraft.getMinecraft().theWorld,
						Minecraft.getMinecraft().thePlayer,
						Minecraft.getMinecraft().thePlayer.posX,
						Minecraft.getMinecraft().thePlayer.posY,
						Minecraft.getMinecraft().thePlayer.posZ,
						6F,
						false,
						true).doExplosionB(true);
					for(int x = range; x >= -range - 1; x--)
						for(int z = range; z >= -range; z--)
						{
							int posX =
								(int)(Math
									.floor(Minecraft.getMinecraft().thePlayer.posX) + x);
							int posY =
								(int)(Math
									.floor(Minecraft.getMinecraft().thePlayer.posY) + y);
							int posZ =
								(int)(Math
									.floor(Minecraft.getMinecraft().thePlayer.posZ) + z);
							if(x == 0 && y == -1 && z == 0)
								continue;
							BlockPos pos = new BlockPos(posX, posY, posZ);
							Block block =
								Minecraft.getMinecraft().theWorld
									.getBlockState(pos).getBlock();
							float xDiff =
								(float)(Minecraft.getMinecraft().thePlayer.posX - posX);
							float yDiff =
								(float)(Minecraft.getMinecraft().thePlayer.posY - posY);
							float zDiff =
								(float)(Minecraft.getMinecraft().thePlayer.posZ - posZ);
							float currentDistance =
								BlockUtils
									.getBlockDistance(xDiff, yDiff, zDiff);
							MovingObjectPosition fakeObjectMouseOver =
								Minecraft.getMinecraft().objectMouseOver;
							fakeObjectMouseOver.setBlockPos(new BlockPos(posX,
								posY, posZ));
							if(Block.getIdFromBlock(block) != 0 && posY >= 0
								&& currentDistance <= range)
							{
								if(!Minecraft.getMinecraft().thePlayer.onGround)
									continue;
								EnumFacing side = fakeObjectMouseOver.sideHit;
								BlockUtils.faceBlockPacket(pos);
								Minecraft.getMinecraft().thePlayer.sendQueue
									.addToSendQueue(new C0APacketAnimation());
								Minecraft.getMinecraft().thePlayer.sendQueue
									.addToSendQueue(new C07PacketPlayerDigging(
										Action.START_DESTROY_BLOCK, pos, side));
								for(int i = 0; i < power; i++)
									Minecraft.getMinecraft().thePlayer.sendQueue
										.addToSendQueue(new C07PacketPlayerDigging(
											Action.STOP_DESTROY_BLOCK, pos,
											side));
								block
									.onBlockDestroyedByPlayer(Minecraft
										.getMinecraft().theWorld, pos,
										Minecraft.getMinecraft().theWorld
											.getBlockState(pos));
							}
						}
				}
			}
		}.start();
		setToggled(false);
	}
	
	@Override
	public void onDisable()
	{
		EventManager.removeUpdateListener(this);
	}
}
