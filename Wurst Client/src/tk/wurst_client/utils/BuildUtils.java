package tk.wurst_client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class BuildUtils
{
	public static void build(int[][] building)
	{
		float playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		while(playerYaw > 180)
			playerYaw -= 360;
		while(playerYaw < -180)
			playerYaw += 360;
		MovingObjectPosition mouseOver = Minecraft.getMinecraft().objectMouseOver;
		BlockPos pos = mouseOver.getBlockPos();
		if(playerYaw > -45 && playerYaw <= 45)
		{//F: 0 South
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() + building[i][0],
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1],
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() + building[i][2]
					),
					Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex(),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() + building[i][0]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.yCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.zCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() + building[i][2])
				));
		}else if(playerYaw > 45 && playerYaw <= 135)
		{//F: 1 West
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() - building[i][2],
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1],
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() + building[i][0]
					),
					Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex(),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() - building[i][2]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() + building[i][0])
				));
		}else if(playerYaw > 135 || playerYaw <= -135)
		{//F: 2 North
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() - building[i][0],
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1],
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() - building[i][2]
					),
					Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex(),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() - building[i][0]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() - building[i][2])
				));
		}else if(playerYaw > -135 && playerYaw <= -45)
		{//F: 3 East
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() + building[i][2],
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1],
						Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() - building[i][0]
					),
					Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex(),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() + building[i][2]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() - building[i][0])
				));
		}
	}
	
	public static void buildNext(int[][] building, MovingObjectPosition mouseOver, float playerYaw, int i)
	{
		if(playerYaw > -45 && playerYaw <= 45)
		{//F: 0 South
			BlockUtils.faceBlockPacket(new BlockPos
			(
				mouseOver.getBlockPos().getX() + building[i][0],
				mouseOver.getBlockPos().getY() + building[i][1],
				mouseOver.getBlockPos().getZ() + building[i][2]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					mouseOver.getBlockPos().getX() + building[i][0],
					mouseOver.getBlockPos().getY() + building[i][1],
					mouseOver.getBlockPos().getZ() + building[i][2]
				),
				mouseOver.sideHit.getIndex(),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() + building[i][0]),
				(float) mouseOver.hitVec.yCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.zCoord - (float) (mouseOver.getBlockPos().getZ() + building[i][2])
			));
		}else if(playerYaw > 45 && playerYaw <= 135)
		{//F: 1 West
			BlockUtils.faceBlockPacket(new BlockPos
			(
				mouseOver.getBlockPos().getX() - building[i][2],
				mouseOver.getBlockPos().getY() + building[i][1],
				mouseOver.getBlockPos().getZ() + building[i][0]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					mouseOver.getBlockPos().getX() - building[i][2],
					mouseOver.getBlockPos().getY() + building[i][1],
					mouseOver.getBlockPos().getZ() + building[i][0]
				),
				mouseOver.sideHit.getIndex(),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() - building[i][2]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getZ() + building[i][0])
			));
		}else if(playerYaw > 135 || playerYaw <= -135)
		{//F: 2 North
			BlockUtils.faceBlockPacket(new BlockPos
			(
				mouseOver.getBlockPos().getX() - building[i][0],
				mouseOver.getBlockPos().getY() + building[i][1],
				mouseOver.getBlockPos().getZ() - building[i][2]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					mouseOver.getBlockPos().getX() - building[i][0],
					mouseOver.getBlockPos().getY() + building[i][1],
					mouseOver.getBlockPos().getZ() - building[i][2]
				),
				mouseOver.sideHit.getIndex(),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() - building[i][0]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getZ() - building[i][2])
			));
		}else if(playerYaw > -135 && playerYaw <= -45)
		{//F: 3 East
			BlockUtils.faceBlockPacket(new BlockPos
			(
				mouseOver.getBlockPos().getX() + building[i][2],
				mouseOver.getBlockPos().getY() + building[i][1],
				mouseOver.getBlockPos().getZ() - building[i][0]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					mouseOver.getBlockPos().getX() + building[i][2],
					mouseOver.getBlockPos().getY() + building[i][1],
					mouseOver.getBlockPos().getZ() - building[i][0]
				),
				mouseOver.sideHit.getIndex(),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() + building[i][2]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getZ() - building[i][0])
			));
		}
	}
	
	public static void advancedBuild(int[][] building)
	{
		float playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		while(playerYaw > 180)
			playerYaw -= 360;
		while(playerYaw < -180)
			playerYaw += 360;
		if(playerYaw > -45 && playerYaw <= 45)
		{//F: 0 South
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						convertPos(1, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) + building[i][0],
						convertPos(2, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) + building[i][1],
						convertPos(3, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) + building[i][2]
					),
					building[i][3],
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() + building[i][0]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.yCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.zCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() + building[i][2])
				));
		}else if(playerYaw > 45 && playerYaw <= 135)
		{//F: 1 West
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						convertPos(1, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) - building[i][2],
						convertPos(2, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) + building[i][1],
						convertPos(3, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) + building[i][0]
					),
					convertSide(building[i][3], 1),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() - building[i][2]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() + building[i][0])
				));
		}else if(playerYaw > 135 || playerYaw <= -135)
		{//F: 2 North
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						convertPos(1, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) - building[i][0],
						convertPos(2, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) + building[i][1],
						convertPos(3, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) - building[i][2]
					),
					convertSide(building[i][3], 2),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() - building[i][0]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() - building[i][2])
				));
		}else if(playerYaw > -135 && playerYaw <= -45)
		{//F: 3 East
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						convertPos(1, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) + building[i][2],
						convertPos(2, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) + building[i][1],
						convertPos(3, Minecraft.getMinecraft().objectMouseOver.sideHit.getIndex()) - building[i][0]
					),
					convertSide(building[i][3], 3),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() + building[i][2]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() - building[i][0])
				));
		}
	}
	
	public static void advancedBuildNext(int[][] building, MovingObjectPosition mouseOver, float playerYaw, int i)
	{
		if(playerYaw > -45 && playerYaw <= 45)
		{//F: 0 South
			BlockUtils.faceBlockPacket(new BlockPos
			(
				convertPosNext(1, mouseOver) + building[i][0],
				convertPosNext(2, mouseOver) + building[i][1],
				convertPosNext(3, mouseOver) + building[i][2]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					convertPosNext(1, mouseOver) + building[i][0],
					convertPosNext(2, mouseOver) + building[i][1],
					convertPosNext(3, mouseOver) + building[i][2]
				),
				building[i][3],
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() + building[i][0]),
				(float) mouseOver.hitVec.yCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.zCoord - (float) (mouseOver.getBlockPos().getZ() + building[i][2])
			));
		}else if(playerYaw > 45 && playerYaw <= 135)
		{//F: 1 West
			BlockUtils.faceBlockPacket(new BlockPos
			(
				convertPosNext(1, mouseOver) - building[i][2],
				convertPosNext(2, mouseOver) + building[i][1],
				convertPosNext(3, mouseOver) + building[i][0]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					convertPosNext(1, mouseOver) - building[i][2],
					convertPosNext(2, mouseOver) + building[i][1],
					convertPosNext(3, mouseOver) + building[i][0]
				),
				convertSide(building[i][3], 1),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() - building[i][2]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getZ() + building[i][0])
			));
		}else if(playerYaw > 135 || playerYaw <= -135)
		{//F: 2 North
			BlockUtils.faceBlockPacket(new BlockPos
			(
				convertPosNext(1, mouseOver) - building[i][0],
				convertPosNext(2, mouseOver) + building[i][1],
				convertPosNext(3, mouseOver) - building[i][2]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					convertPosNext(1, mouseOver) - building[i][0],
					convertPosNext(2, mouseOver) + building[i][1],
					convertPosNext(3, mouseOver) - building[i][2]
				),
				convertSide(building[i][3], 2),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() - building[i][0]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getZ() - building[i][2])
			));
		}else if(playerYaw > -135 && playerYaw <= -45)
		{//F: 3 East
			BlockUtils.faceBlockPacket(new BlockPos
			(
				convertPosNext(1, mouseOver) + building[i][2],
				convertPosNext(2, mouseOver) + building[i][1],
				convertPosNext(3, mouseOver) - building[i][0]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					convertPosNext(1, mouseOver) + building[i][2],
					convertPosNext(2, mouseOver) + building[i][1],
					convertPosNext(3, mouseOver) - building[i][0]
				),
				convertSide(building[i][3], 3),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() + building[i][2]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getZ() - building[i][0])
			));
		}
	}
	
	public static int convertSide(int side, int f)
	{
		int convertedSide = 6;
		if(side == 0 || side == 1)
		{
			convertedSide = side;
		}else if(side == 2)
		{
			if(f == 1)
				convertedSide = 5;
			else if(f == 2)
				convertedSide = 3;
			else if(f == 3)
				convertedSide = 4;
		}else if(side == 3)
		{
			if(f == 1)
				convertedSide = 4;
			else if(f == 2)
				convertedSide = 2;
			else if(f == 3)
				convertedSide = 5;
		}else if(side == 4)
		{
			if(f == 1)
				convertedSide = 2;
			else if(f == 2)
				convertedSide = 5;
			else if(f == 3)
				convertedSide = 3;
		}else if(side == 5)
		{
			if(f == 1)
				convertedSide = 3;
			else if(f == 2)
				convertedSide = 4;
			else if(f == 3)
				convertedSide = 2;
		}
		return convertedSide;
	}
	
	public static int convertPos(int xyz, int side)
	{
		int convertedPos = 256;
		if(side == 0)
			{
				if(xyz == 1)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX();
				else if(xyz == 2)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() -2;
				else if(xyz == 3)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ();
			}else if(side == 1)
			{
				if(xyz == 1)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX();
				else if(xyz == 2)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY();
				else if(xyz == 3)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ();
			}else if(side == 2)
			{
				if(xyz == 1)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX();
				else if(xyz == 2)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() - 1;
				else if(xyz == 3)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() - 1;
			}else if(side == 3)
			{
				if(xyz == 1)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX();
				else if(xyz == 2)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() - 1;
				else if(xyz == 3)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() + 1;
			}else if(side == 4)
			{
				if(xyz == 1)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() - 1;
				else if(xyz == 2)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() - 1;
				else if(xyz == 3)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ();
			}else if(side == 5)
			{
				if(xyz == 1)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() + 1;
				else if(xyz == 2)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() - 1;
				else if(xyz == 3)
					convertedPos = Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ();
			}
		return convertedPos;
	}
	
	public static int convertPosNext(int xyz, MovingObjectPosition mouseOver)
	{
		int convertedPos = 256;
		if(mouseOver.sideHit.getIndex() == 0)
			{
				if(xyz == 1)
					convertedPos = mouseOver.getBlockPos().getX();
				else if(xyz == 2)
					convertedPos = mouseOver.getBlockPos().getY() - 2;
				else if(xyz == 3)
					convertedPos = mouseOver.getBlockPos().getZ();
			}else if(mouseOver.sideHit.getIndex() == 1)
			{
				if(xyz == 1)
					convertedPos = mouseOver.getBlockPos().getX();
				else if(xyz == 2)
					convertedPos = mouseOver.getBlockPos().getY();
				else if(xyz == 3)
					convertedPos = mouseOver.getBlockPos().getZ();
			}else if(mouseOver.sideHit.getIndex() == 2)
			{
				if(xyz == 1)
					convertedPos = mouseOver.getBlockPos().getX();
				else if(xyz == 2)
					convertedPos = mouseOver.getBlockPos().getY() - 1;
				else if(xyz == 3)
					convertedPos = mouseOver.getBlockPos().getZ() - 1;
			}else if(mouseOver.sideHit.getIndex() == 3)
			{
				if(xyz == 1)
					convertedPos = mouseOver.getBlockPos().getX();
				else if(xyz == 2)
					convertedPos = mouseOver.getBlockPos().getY() - 1;
				else if(xyz == 3)
					convertedPos = mouseOver.getBlockPos().getZ() + 1;
			}else if(mouseOver.sideHit.getIndex() == 4)
			{
				if(xyz == 1)
					convertedPos = mouseOver.getBlockPos().getX() - 1;
				else if(xyz == 2)
					convertedPos = mouseOver.getBlockPos().getY() - 1;
				else if(xyz == 3)
					convertedPos = mouseOver.getBlockPos().getZ();
			}else if(mouseOver.sideHit.getIndex() == 5)
			{
				if(xyz == 1)
					convertedPos = mouseOver.getBlockPos().getX() + 1;
				else if(xyz == 2)
					convertedPos = mouseOver.getBlockPos().getY() - 1;
				else if(xyz == 3)
					convertedPos = mouseOver.getBlockPos().getZ();
			}
		return convertedPos;
	}
	
	public static int convertPosInBuiling(int xyz,int i, int[][] building, MovingObjectPosition mouseOver)
	{
		int convertedPos = 256;
		if(i < 0)
			i = 0;
		if(mouseOver.sideHit.getIndex() == 0)
			{
				if(xyz == 1)
					convertedPos = building[i][0];
				else if(xyz == 2)
					convertedPos = building[i][1] -1;
				else if(xyz == 3)
					convertedPos = building[i][2];
			}else if(mouseOver.sideHit.getIndex() == 1)
			{
				if(xyz == 1)
					convertedPos = building[i][0];
				else if(xyz == 2)
					convertedPos = building[i][1] + 1;
				else if(xyz == 3)
					convertedPos = building[i][2];
			}else if(mouseOver.sideHit.getIndex() == 2)
			{
				if(xyz == 1)
					convertedPos = building[i][0];
				else if(xyz == 2)
					convertedPos = building[i][1];
				else if(xyz == 3)
					convertedPos = building[i][2] - 1;
			}else if(mouseOver.sideHit.getIndex() == 3)
			{
				if(xyz == 1)
					convertedPos = building[i][0];
				else if(xyz == 2)
					convertedPos = building[i][1];
				else if(xyz == 3)
					convertedPos = building[i][2] + 1;
			}else if(mouseOver.sideHit.getIndex() == 4)
			{
				if(xyz == 1)
					convertedPos = building[i][0] - 1;
				else if(xyz == 2)
					convertedPos = building[i][1];
				else if(xyz == 3)
					convertedPos = building[i][2];
			}else if(mouseOver.sideHit.getIndex() == 5)
			{
				if(xyz == 1)
					convertedPos = building[i][0] + 1;
				else if(xyz == 2)
					convertedPos = building[i][1];
				else if(xyz == 3)
					convertedPos = building[i][2];
			}
		return convertedPos;
	}
	
	public static int convertPosInAdvancedBuiling(int xyz, int i, int[][] building)
	{
		int convertedPos = 256;
		if(i < 0)
			i = 0;
		if(building[i][3] == 0)
			{
				if(xyz == 1)
					convertedPos = building[i][0];
				else if(xyz == 2)
					convertedPos = building[i][1] -1;
				else if(xyz == 3)
					convertedPos = building[i][2];
			}else if(building[i][3] == 1)
			{
				if(xyz == 1)
					convertedPos = building[i][0];
				else if(xyz == 2)
					convertedPos = building[i][1] + 1;
				else if(xyz == 3)
					convertedPos = building[i][2];
			}else if(building[i][3] == 2)
			{
				if(xyz == 1)
					convertedPos = building[i][0];
				else if(xyz == 2)
					convertedPos = building[i][1];
				else if(xyz == 3)
					convertedPos = building[i][2] - 1;
			}else if(building[i][3] == 3)
			{
				if(xyz == 1)
					convertedPos = building[i][0];
				else if(xyz == 2)
					convertedPos = building[i][1];
				else if(xyz == 3)
					convertedPos = building[i][2] + 1;
			}else if(building[i][3] == 4)
			{
				if(xyz == 1)
					convertedPos = building[i][0] - 1;
				else if(xyz == 2)
					convertedPos = building[i][1];
				else if(xyz == 3)
					convertedPos = building[i][2];
			}else if(building[i][3] == 5)
			{
				if(xyz == 1)
					convertedPos = building[i][0] + 1;
				else if(xyz == 2)
					convertedPos = building[i][1];
				else if(xyz == 3)
					convertedPos = building[i][2];
			}
		return convertedPos;
	}
	
	public static void advancedInstantBuild(int[][] building)
	{
		float playerYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		while(playerYaw > 180)
			playerYaw -= 360;
		while(playerYaw < -180)
			playerYaw += 360;
		if(playerYaw > -45 && playerYaw <= 45)
		{//F: 0 South
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						(int) Minecraft.getMinecraft().thePlayer.posX + building[i][0],
						(int) Minecraft.getMinecraft().thePlayer.posY - 2 + building[i][1],
						(int) Minecraft.getMinecraft().thePlayer.posZ + building[i][2]
					),
					building[i][3],
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() + building[i][0]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.yCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.zCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() + building[i][2])
				));
		}else if(playerYaw > 45 && playerYaw <= 135)
		{//F: 1 West
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						(int) (Minecraft.getMinecraft().thePlayer.posX - building[i][2]),
						(int) Minecraft.getMinecraft().thePlayer.posY - 2 + building[i][1],
						(int) Minecraft.getMinecraft().thePlayer.posZ + building[i][0]
					),
					convertSide(building[i][3], 1),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() - building[i][2]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() + building[i][0])
				));
		}else if(playerYaw > 135 || playerYaw <= -135)
		{//F: 2 North
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						(int) Minecraft.getMinecraft().thePlayer.posX - building[i][0],
						(int) Minecraft.getMinecraft().thePlayer.posY - 2 + building[i][1],
						(int) Minecraft.getMinecraft().thePlayer.posZ - building[i][2]
					),
					convertSide(building[i][3], 2),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() - building[i][0]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() - building[i][2])
				));
		}else if(playerYaw > -135 && playerYaw <= -45)
		{//F: 3 East
			for(int i = 0; i < building.length; i++)
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
				(
					new BlockPos
					(
						(int) Minecraft.getMinecraft().thePlayer.posX + building[i][2],
						(int) Minecraft.getMinecraft().thePlayer.posY - 2 + building[i][1],
						(int) Minecraft.getMinecraft().thePlayer.posZ - building[i][0]
					),
					convertSide(building[i][3], 3),
					Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX() + building[i][2]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + building[i][1]),
					(float) Minecraft.getMinecraft().objectMouseOver.hitVec.xCoord - (float) (Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ() - building[i][0])
				));
		}
	}
	
	public static void advancedInstantBuildNext(int[][] building, MovingObjectPosition mouseOver, float playerYaw, double posX, double posY, double posZ, int i)
	{
		if(playerYaw > -45 && playerYaw <= 45)
		{//F: 0 South
			BlockUtils.faceBlockPacket(new BlockPos
			(
				(int) posX - 1 + building[i][0],
				(int) posY - 2 + building[i][1],
				(int) posZ + building[i][2]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					(int) posX - 1 + building[i][0],
					(int) posY - 2 + building[i][1],
					(int) posZ + building[i][2]
				),
				building[i][3],
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() + building[i][0]),
				(float) mouseOver.hitVec.yCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.zCoord - (float) (mouseOver.getBlockPos().getZ() + building[i][2])
			));
		}else if(playerYaw > 45 && playerYaw <= 135)
		{//F: 1 West
			BlockUtils.faceBlockPacket(new BlockPos
			(
				(int) (posX - 1 - building[i][2]),
				(int) posY - 2 + building[i][1],
				(int) posZ + building[i][0]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					(int) (posX - 1 - building[i][2]),
					(int) posY - 2 + building[i][1],
					(int) posZ + building[i][0]
				),
				convertSide(building[i][3], 1),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() - building[i][2]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getZ() + building[i][0])
			));
		}else if(playerYaw > 135 || playerYaw <= -135)
		{//F: 2 North
			BlockUtils.faceBlockPacket(new BlockPos
			(
				(int) posX - 1 - building[i][0],
				(int) posY - 2 + building[i][1],
				(int) posZ - building[i][2]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					(int) posX - 1 - building[i][0],
					(int) posY - 2 + building[i][1],
					(int) posZ - building[i][2]
				),
				convertSide(building[i][3], 2),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() - building[i][0]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getZ() - building[i][2])
			));
		}else if(playerYaw > -135 && playerYaw <= -45)
		{//F: 3 East
			BlockUtils.faceBlockPacket(new BlockPos
			(
				(int) posX - 1 + building[i][2],
				(int) posY - 2 + building[i][1],
				(int) posZ - building[i][0]
			));
			Minecraft.getMinecraft().thePlayer.swingItem();
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement
			(
				new BlockPos
				(
					(int) posX - 1 + building[i][2],
					(int) posY - 2 + building[i][1],
					(int) posZ - building[i][0]
				),
				convertSide(building[i][3], 3),
				Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getX() + building[i][2]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getY() + building[i][1]),
				(float) mouseOver.hitVec.xCoord - (float) (mouseOver.getBlockPos().getZ() - building[i][0])
			));
		}
	}
}
