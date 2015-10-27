/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.capes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

public class WurstCapes
{
	private static JsonObject capes;
	
	/**
	 * @see net.minecraft.client.resources.SkinManager#func_152790_a(GameProfile,
	 *      SkinAvailableCallback, boolean)
	 * @param player
	 * @param skinManagerMap
	 */
	@SuppressWarnings("unchecked")
	public static void checkCape(GameProfile player, HashMap skinManagerMap)
	{
		if(capes == null)
			try
			{
				HttpsURLConnection connection =
					(HttpsURLConnection)new URL(
						"https://www.wurst-client.tk/api/v1/capes.json")
						.openConnection();
				connection.connect();
				capes =
					new JsonParser().parse(
						new InputStreamReader(connection.getInputStream()))
						.getAsJsonObject();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		else if(capes.has("use_new_server")
			&& capes.get("use_new_server").getAsBoolean())
			// get cape from new server
			try
			{
				HttpsURLConnection connection =
					(HttpsURLConnection)new URL(
						"https://www.wurst-capes.tk/cape/?uuid="
							+ player.getId().toString().replace("-", ""))
						.openConnection();
				connection.connect();
				System.out.println(connection.getResponseCode());
				if(connection.getResponseCode() == 200)
				{
					BufferedReader reader =
						new BufferedReader(new InputStreamReader(
							connection.getInputStream()));
					String cape = reader.readLine();
					reader.close();
					skinManagerMap.put(Type.CAPE, new MinecraftProfileTexture(
						cape, null));
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		else
			// get cape from old server
			try
			{
				if(capes.has(player.getName()))
					skinManagerMap.put(Type.CAPE, new MinecraftProfileTexture(
						capes.get(player.getName()).getAsString(), null));
			}catch(Exception e)
			{
				e.printStackTrace();
			}
	}
}
