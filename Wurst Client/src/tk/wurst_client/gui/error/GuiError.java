/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.error;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.mod.Mod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GuiError extends GuiScreen
{
	private ResourceLocation bugTexture = new ResourceLocation("wurst/bug.png");
	private final Exception e;
	private final Object listener;
	private final String action;
	
	public GuiError(Exception e, Object listener, String action)
	{
		this.e = e;
		this.listener = listener;
		this.action = action;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		buttonList.add(new GuiButton(0, width / 2 - 154, height / 3 * 2, 100, 20, "Report Bug"));
		buttonList.add(new GuiButton(1, width / 2 - 50, height / 3 * 2, 100, 20, "View Stacktrace"));
		buttonList.add(new GuiButton(2, width / 2 + 54, height / 3 * 2, 100, 20, "Ignore"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(!button.enabled)
			return;
		switch(button.id)
		{
			case 0:
				try
				{
					String url = "https://api.github.com/search/issues?q=repo:Wurst-Imperium/Wurst-Client+is:issue+";
					url += URLEncoder.encode("test", "UTF-8");
					HttpsURLConnection connection = (HttpsURLConnection)new URL(url).openConnection();
					connection.connect();
					JsonObject json = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
					boolean known = json.get("total_count").getAsInt() > 0;
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			case 1:
				break;
			case 2:
				if(listener instanceof Mod)
					Client.wurst.modManager.getModByClass(listener.getClass()).setEnabled(false);
				mc.displayGuiScreen(null);
				break;
			default:
				break;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		mc.getTextureManager().bindTexture(bugTexture);
		drawScaledCustomSizeModalRect(width / 4 * 3, height / 3, 0, 0, 256, 256, 96, 96, 256, 256);
		drawCenteredString(fontRendererObj, "§nError!§r", width / 2, height / 4, 0xffffffff);
		drawCenteredString(fontRendererObj, "An error occurred while enabling /home.", width / 2, height / 4 + 16, 0xffffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
