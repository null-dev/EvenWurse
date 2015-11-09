/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.capes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;
import tk.wurst_client.utils.JsonUtils;
import tk.wurst_client.utils.MiscUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

public class CapeFetcher implements Runnable
{
	private JsonArray uuids = new JsonArray();
	private ArrayList<SkinManager.SkinAvailableCallback> callbacks = new ArrayList<>();
	private boolean running = false;
	
	public boolean addUUID(String uuid, SkinAvailableCallback callback)
	{
		if(uuids.size() < 100 && !running)
		{
			uuids.add(new JsonPrimitive(uuid));
			callbacks.add(callback);
			return true;
		}else
			return false;
	}
	
	@Override
	public void run()
	{
		try
		{
			Thread.sleep(1000);
		}catch(InterruptedException e1)
		{
			e1.printStackTrace();
		}
		running = true;
		String response = null;
		try
		{
			response =
				MiscUtils.post(new URL("https://www.wurst-capes.tk/capes/"),
					JsonUtils.gson.toJson(uuids), "application/json");
			JsonArray capes =
				JsonUtils.jsonParser.parse(response).getAsJsonArray();
			for(int i = 0; i < capes.size(); i++)
			{
				final int iFinal = i;
				Minecraft.getMinecraft().addScheduledTask(new Runnable()
				{
					@Override
					public void run()
					{
						Minecraft
							.getMinecraft()
							.getSkinManager()
							.loadSkin(
								new MinecraftProfileTexture(capes.get(iFinal).getAsString(),
									null), Type.CAPE, callbacks.get(iFinal));
					}
				});
			}
		}catch(IOException | JsonParseException e)
		{
			System.err.println("[Wurst] Failed to load " + uuids.size()
				+ " cape(s) from wurst-capes.tk!");
			System.out.println("Server response:\n" + response);
			e.printStackTrace();
		}
	}
}
