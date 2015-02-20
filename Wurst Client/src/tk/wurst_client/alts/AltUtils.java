/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.alts;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tk.wurst_client.Client;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class AltUtils
{
	private static final Logger logger = LogManager.getLogger();
	
	public static String login(String name, String password)
	{
		YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)authenticationService.createUserAuthentication(Agent.MINECRAFT);
		authentication.setUsername(name);
		authentication.setPassword(password);
		String displayText;
		try
		{
			authentication.logIn();
			Minecraft.getMinecraft().session = new Session
				(
					authentication.getSelectedProfile().getName(),
					authentication.getSelectedProfile().getId().toString(),
					authentication.getAuthenticatedToken(),
					"mojang"
				);
			displayText = "";
		}catch(AuthenticationUnavailableException e)
		{
			displayText = "§4§lCannot contact authentication server!";
		}catch(AuthenticationException e)
		{// wrong password account migrated
			if(e.getMessage().contains("Invalid username or password.") || e.getMessage().toLowerCase().contains("account migrated"))
				displayText = "§4§lWrong password!";
			else
				displayText = "§4§lCannot contact authentication server!";
			logger.error(e.getMessage());
		}catch(NullPointerException e)
		{
			displayText = "§4§lWeird error: This alt doesn't have a username!";
		}
		return displayText;
	}
	
	public static String check(String name, String password)
	{
		YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)authenticationService.createUserAuthentication(Agent.MINECRAFT);
		authentication.setUsername(name);
		authentication.setPassword(password);
		String displayText;
		try
		{
			authentication.logIn();
			displayText = "";
		}catch(AuthenticationUnavailableException e)
		{
			displayText = "§4§lCannot contact authentication server!";
		}catch(AuthenticationException e)
		{// wrong password account migrated
			if(e.getMessage().contains("Invalid username or password.") || e.getMessage().toLowerCase().contains("account migrated"))
				displayText = "§4§lWrong password!";
			else
				displayText = "§4§lCannot contact authentication server!";
			logger.error(e.getMessage());
		}catch(NullPointerException e)
		{
			displayText = "§4§lWeird error: This alt doesn't have a username!";
		}
		return displayText;
	}
	
	public static void changeCrackedName(String newName)
	{
		Minecraft.getMinecraft().session = new Session(newName, "", "", "mojang");
	}
	
	public static String generateName()
	{
		String name = "";
		int nameLength = (int)Math.round(Math.random() * 4) + 5;
		String vowels = "aeiouy";
		String consonants = "bcdfghklmnprstvwz";
		int usedConsonants = 0;
		int usedVowels = 0;
		String lastLetter = "blah";
		for(int i = 0; i < nameLength; i++)
		{
			String nextLetter = lastLetter;
			if((new Random().nextBoolean() || usedConsonants == 1) && usedVowels < 2)
			{
				while(nextLetter.equals(lastLetter))
				{
					int letterIndex = (int)(Math.random() * vowels.length() - 1);
					nextLetter = vowels.substring(letterIndex, letterIndex + 1);
				}
				usedConsonants = 0;
				usedVowels++;
			}
			else
			{
				while(nextLetter.equals(lastLetter))
				{
					int letterIndex = (int)(Math.random() * consonants.length() - 1);
					nextLetter = consonants.substring(letterIndex, letterIndex + 1);
				}
				usedConsonants++;
				usedVowels = 0;
			}
			lastLetter = nextLetter;
			name = name.concat(nextLetter);
		}
		int capitalMode = (int)Math.round(Math.random() * 2);
		if(capitalMode == 1)
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
		else if(capitalMode == 2)
			for(int i = 0; i < nameLength; i++)
				if((int)Math.round(Math.random() * 3) == 1)
					name = name.substring(0, i) + name.substring(i, i + 1).toUpperCase() + (i == nameLength ? "" : name.substring(i + 1));
		int numberLength = (int)Math.round(Math.random() * 3) + 1;
		int numberMode = (int)Math.round(Math.random() * 3);
		boolean number = new Random().nextBoolean();
		if(number)
			if(numberLength == 1)
			{
				int nextNumber = (int)Math.round(Math.random() * 9);
				name = name.concat(Integer.toString(nextNumber));
			}else if(numberMode == 0)
			{
				int nextNumber = (int)(Math.round(Math.random() * 8) + 1);
				for(int i = 0; i < numberLength; i++)
					name = name.concat(Integer.toString(nextNumber));
			}else if(numberMode == 1)
			{
				int nextNumber = (int)(Math.round(Math.random() * 8) + 1);
				name = name.concat(Integer.toString(nextNumber));
				for(int i = 1; i < numberLength; i++)
					name = name.concat("0");
			}else if(numberMode == 2)
			{
				int nextNumber = (int)(Math.round(Math.random() * 8) + 1);
				name = name.concat(Integer.toString(nextNumber));
				for(int i = 0; i < numberLength; i++)
				{
					nextNumber = (int)Math.round(Math.random() * 9);
					name = name.concat(Integer.toString(nextNumber));
				}
			}else if(numberMode == 3)
			{
				int nextNumber = 99999;
				while(Integer.toString(nextNumber).length() != numberLength)
				{
					nextNumber = (int)(Math.round(Math.random() * 12) + 1);
					nextNumber = (int)Math.pow(2, nextNumber);
				}
				name = name.concat(Integer.toString(nextNumber));
			}
		boolean leet = !number && new Random().nextBoolean();
		if(leet)
		{
			String oldName = name;
			while(name.equals(oldName))
			{
				int leetMode = (int)Math.round(Math.random() * 7);
				if(leetMode == 0)
				{
					name = name.replace("a", "4");
					name = name.replace("A", "4");
				}
				if(leetMode == 1)
				{
					name = name.replace("e", "3");
					name = name.replace("E", "3");
				}
				if(leetMode == 2)
				{
					name = name.replace("g", "6");
					name = name.replace("G", "6");
				}
				if(leetMode == 3)
				{
					name = name.replace("h", "4");
					name = name.replace("H", "4");
				}
				if(leetMode == 4)
				{
					name = name.replace("i", "1");
					name = name.replace("I", "1");
				}
				if(leetMode == 5)
				{
					name = name.replace("o", "0");
					name = name.replace("O", "0");
				}
				if(leetMode == 6)
				{
					name = name.replace("s", "5");
					name = name.replace("S", "5");
				}
				if(leetMode == 7)
				{
					name = name.replace("l", "7");
					name = name.replace("L", "7");
				}
			}
		}
		int special = (int)Math.round(Math.random() * 8);
		if(special == 3)
			name = "xX".concat(name).concat("Xx");
		else if(special == 4)
			name = name.concat("LP");
		else if(special == 5)
			name = name.concat("HD");
		return name;
	}
	
	public static String stealSkin(String name)
	{
		String reply = "";
		try
		{
			URL skinURL = new URL("http://skins.minecraft.net/MinecraftSkins/" + name + ".png");
			URLConnection skinCon = skinURL.openConnection();
			BufferedInputStream skinputStream = new BufferedInputStream(skinCon.getInputStream());
			File skin = new File(Client.wurst.fileManager.skinDir, name + ".png");
			FileOutputStream outputStream = new FileOutputStream(skin);
			int i;
			while((i = skinputStream.read()) != -1)
				outputStream.write(i);
			outputStream.close();
			skinputStream.close();
			reply = "§a§lSaved skin to wurst/skins/" + name + ".png.";
		}catch(UnknownHostException e)
		{
			reply = "§4§lCannot contact skin server!";
		}catch(Exception e)
		{
			reply = "§4§lUnable to steal skin.";
		}
		return reply;
	}
}
