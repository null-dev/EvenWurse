/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.module.modules;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import tk.wurst_client.ai.PathUtils;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;
import tk.wurst_client.utils.BlockUtils;

public class GoToCmd extends Module
{
	private static ArrayList<BlockPos> path;
	private static BlockPos goal;
	private int index;
	
	public GoToCmd()
	{
		super("GoTo", "", Category.HIDDEN);
	}
	
	@Override
	public String getRenderName()
	{
		if(goal != null)
			return "Go to " + goal.getX() + " " + goal.getY() + " " + goal.getZ();
		else
			return "GoTo";
	}
	
	@Override
	public void onEnable()
	{
		index = 0;
	}
	
	@Override
	public void onUpdate()
	{
		if(!getToggled())
			return;
		if(path == null || goal == null)
		{
			setToggled(false);
			return;
		}
		BlockPos currentPos = new BlockPos(Minecraft.getMinecraft().thePlayer);
		BlockPos nextPos = path.get(index);
		float dist = BlockUtils.getPlayerBlockDistance(nextPos);
		float hDist = BlockUtils.getHorizontalPlayerBlockDistance(nextPos);
		double vDist = Math.abs(Minecraft.getMinecraft().thePlayer.posX - nextPos.getY());
		Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindBack.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindRight.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = false;
		Minecraft.getMinecraft().thePlayer.rotationPitch = 10;
		BlockUtils.faceBlockClientHorizontally(nextPos);
		
		if(hDist > 0.25)
		{
			Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = true;
		}
		if(vDist > 1)
		{
			if(PathUtils.isFlyable(currentPos))
			{
				if(currentPos.getY() > nextPos.getY())
					Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = true;
				else
					Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = true;
			}
		}
		
		if(dist < 1)
			index++;
		if(index >= path.size())
			setToggled(false);
	}
	
	@Override
	public void onDisable()
	{
		path = null;
		goal = null;
		Minecraft.getMinecraft().gameSettings.keyBindForward.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindBack.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindRight.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindLeft.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindJump.pressed = false;
		Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed = false;
	}

	public static void setPath(ArrayList<BlockPos> path)
	{
		GoToCmd.path = path;
	}

	public static BlockPos getGoal()
	{
		return goal;
	}

	public static void setGoal(BlockPos goal)
	{
		GoToCmd.goal = goal;
	}
}
