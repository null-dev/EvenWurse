/*
 * Copyright � 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.hooks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import tk.wurst_client.bot.WurstBot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class FrameHook
{
	private static JFrame frame;
	
	public static void createFrame(DefaultResourcePack mcDefaultResourcePack,
		Logger logger) throws LWJGLException
	{
		// check if frame should be created
		if(!isAutoMaximize() && !WurstBot.isEnabled())
			return;
		
		// create frame
		frame = new JFrame("Minecraft 1.8");
		
		// add LWJGL
		Canvas canvas = new Canvas();
		canvas.setBackground(new Color(16, 16, 16));
		Display.setParent(canvas);
		Minecraft mc = Minecraft.getMinecraft();
		canvas.setSize(mc.displayWidth, mc.displayHeight);
		frame.add(canvas);
		
		// configure frame
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		// add icons
		InputStream icon16 = null;
		InputStream icon32 = null;
		try
		{
			icon16 =
				mcDefaultResourcePack.func_152780_c(new ResourceLocation(
					"icons/icon_16x16.png"));
			icon32 =
				mcDefaultResourcePack.func_152780_c(new ResourceLocation(
					"icons/icon_32x32.png"));
			ArrayList<BufferedImage> icons = new ArrayList<>();
			icons.add(ImageIO.read(icon16));
			icons.add(ImageIO.read(icon32));
			frame.setIconImages(icons);
		}catch(Exception e)
		{
			logger.error("Couldn't set icon", e);
		}finally
		{
			IOUtils.closeQuietly(icon16);
			IOUtils.closeQuietly(icon32);
		}
		
		// show frame
		if(!WurstBot.isEnabled())
			frame.setVisible(true);
	}

	private static boolean isAutoMaximize()
	{
		File autoMaximizeFile =
			new File(Minecraft.getMinecraft().mcDataDir
				+ "/wurst/automaximize.json");
		boolean autoMaximizeEnabled = false;
		if(!autoMaximizeFile.exists())
			createAutoMaximizeFile(autoMaximizeFile);
		try
		{
			BufferedReader load =
				new BufferedReader(new FileReader(autoMaximizeFile));
			String line = load.readLine();
			load.close();
			Minecraft.getMinecraft();
			autoMaximizeEnabled =
				line.equals("true") && !Minecraft.isRunningOnMac;
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return autoMaximizeEnabled;
	}
	
	private static void createAutoMaximizeFile(File autoMaximizeFile)
	{
		try
		{
			if(!autoMaximizeFile.getParentFile().exists())
				autoMaximizeFile.getParentFile().mkdirs();
			PrintWriter save =
				new PrintWriter(new FileWriter(autoMaximizeFile));
			save.println(Boolean.toString(!Minecraft.isRunningOnMac));
			save.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void maximize()
	{
		if(frame != null)
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
	
	public static JFrame getFrame()
	{
		return frame;
	}
}
