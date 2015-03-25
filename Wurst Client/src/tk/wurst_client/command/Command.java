/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import tk.wurst_client.Client;

public abstract class Command
{
	private String name = getClass().getAnnotation(Info.class).name();
	private String help = getClass().getAnnotation(Info.class).help();
	private String[] syntax = getClass().getAnnotation(Info.class).syntax();
	
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Info
	{
		String name();
		
		String help();
		
		String[] syntax();
	}
	
	@Deprecated
	public Command(String commandName, String... commandHelp)
	{
		//this.name = commandName;
		//this.help = commandHelp;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getHelp()
	{
		return help;
	}
	
	public String[] getSyntax()
	{
		return syntax;
	}

	public void commandError()
	{
		Client.wurst.chat.error("Something went wrong.");
		Client.wurst.chat.message("If you need help, type \".help "
			+ name + "\".");
	}
	
	public abstract void execute(String[] args);
}
