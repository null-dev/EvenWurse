/*
 * Copyright � 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import tk.wurst_client.WurstClient;
import tk.wurst_client.api.Module;
import tk.wurst_client.utils.EntityUtils;
import tk.wurst_client.utils.F;
import tk.wurst_client.utils.MiscUtils;
import tk.wurst_client.navigator.NavigatorItem;
import tk.wurst_client.navigator.NavigatorPossibleKeybind;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import java.util.ArrayList;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Cmd extends Module implements NavigatorItem
{
	private String name = getClass().getAnnotation(Info.class).name();
	private String[] aliases = getClass().getAnnotation(Info.class).aliases();
	private String help = getClass().getAnnotation(Info.class).help();
	private String[] syntax = getClass().getAnnotation(Info.class).syntax();
	private String[] tags = getClass().getAnnotation(Info.class).tags();
	private String tutorial = getClass().getAnnotation(Info.class).tutorial();

	@Retention(RetentionPolicy.RUNTIME)
	public @interface Info
	{
		String name();

		String[] aliases() default {};

		String help();

		String[] syntax();

		String[] tags() default {};

		String tutorial() default "";
	}

	public class SyntaxError extends Error
	{
		public SyntaxError()
		{
			super();
		}

		public SyntaxError(String message)
		{
			super(message);
		}
	}

	public class Error extends Throwable
	{
		public Error()
		{
			super();
		}

		public Error(String message)
		{
			super(message);
		}
	}

	public String getCmdName()
	{
		return name;
	}

	public String[] getAliases() { return aliases; }

	public String getHelp()
	{
		return help;
	}

	public String[] getSyntax()
	{
		return syntax;
	}

	@Override
	public final String getName()
	{
		return "." + name;
	}

	@Override
	public final String getDescription()
	{
		return help;
	}

	@Override
	public final boolean isEnabled()
	{
		return false;
	}

	@Override
	public final boolean isBlocked()
	{
		return false;
	}

	@Override
	public final String[] getTags()
	{
		return tags;
	}

	@Override
	public final ArrayList<BasicSlider> getSettings()
	{
		return new ArrayList<BasicSlider>();
	}

	@Override
	public final ArrayList<NavigatorPossibleKeybind> getPossibleKeybinds()
	{
		return new ArrayList<>();
	}

	@Override
	public String getPrimaryAction()
	{
		return "";
	}

	@Override
	public void doPrimaryAction()
	{

	}

	@Override
	public final String getTutorialPage()
	{
		return tutorial;
	}

	public void printHelp()
	{
		for(String line : help.split("\n"))
			WurstClient.INSTANCE.chat.message(line);
	}

	public void printSyntax()
	{
		String output = F.ITALIC + "." + name + F.RESET;
		if(syntax.length != 0)
		{
			output += " " + syntax[0];
			for(int i = 1; i < syntax.length; i++)
				output += "\n    " + syntax[i];
		}
		for(String line : output.split("\n"))
			WurstClient.INSTANCE.chat.message(line);
	}

	protected final int[] argsToPos(String... args) throws Cmd.Error
	{
		int[] pos = new int[3];
		if(args.length == 3)
		{
			int[] playerPos =
					new int[]{(int)Minecraft.getMinecraft().thePlayer.posX,
							(int)Minecraft.getMinecraft().thePlayer.posY,
							(int)Minecraft.getMinecraft().thePlayer.posZ};
			for(int i = 0; i < args.length; i++)
				if(MiscUtils.isInteger(args[i]))
					pos[i] = Integer.parseInt(args[i]);
				else if(args[i].startsWith("~"))
					if(args[i].equals("~"))
						pos[i] = playerPos[i];
					else if(MiscUtils.isInteger(args[i].substring(1)))
						pos[i] =
								playerPos[i]
										+ Integer.parseInt(args[i].substring(1));
					else
						syntaxError("Invalid coordinates.");
				else
					syntaxError("Invalid coordinates.");
		}else if(args.length == 1)
		{
			EntityLivingBase entity =
					EntityUtils.searchEntityByNameRaw(args[0]);
			if(entity == null)
				error("Entity \"" + args[0] + "\" could not be found.");
			BlockPos blockPos = new BlockPos(entity);
			pos = new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
		}else
			syntaxError("Invalid coordinates.");
		return pos;
	}

	protected final void syntaxError() throws SyntaxError
	{
		throw new SyntaxError();
	}

	protected final void syntaxError(String message) throws SyntaxError
	{
		throw new SyntaxError(message);
	}

	protected final void error(String message) throws Error
	{
		throw new Error(message);
	}

	public abstract void execute(String[] args) throws Cmd.Error;
}
