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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import tk.wurst_client.Client;
import tk.wurst_client.mod.Mod;
import tk.wurst_client.utils.MiscUtils;

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
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 3 * 2, 200,
			20, "Report Bug on GitHub"));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 3 * 2 + 24,
			98, 20, "View Stacktrace"));
		buttonList.add(new GuiButton(2, width / 2 + 2, height / 3 * 2 + 24, 98,
			20, "Back to Game"));
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
					String report = "# Description\n"
						+ "An error occurred while " + action;
					if(listener instanceof Mod)
						report +=
							" "
								+ Client.wurst.modManager.getModByClass(
									listener.getClass()).getName();
					report += ".\n\n"
						+ "# Stacktrace\n";
					StringWriter stacktraceWriter = new StringWriter();
					e.printStackTrace(new PrintWriter(stacktraceWriter));
					String trace = stacktraceWriter.toString();
					report +=
						"```\n" + trace + "```"
							+ "\n\n# System details\n"
							+ "- OS: " + System.getProperty("os.name") + " ("
							+ System.getProperty("os.arch") + ")\n"
							+ "- Java version: "
							+ System.getProperty("java.version") + " ("
							+ System.getProperty("java.vendor") + ")\n";
					report = URLEncoder.encode(report, "UTF-8");
					String query =
						trace.replace(" ", "+").replace("\r\n", "")
							.replace("\t", "");
					query = query.substring(0, 128);
					System.out.println(query);
					String url =
						"https://api.github.com/search/issues?q=repo:Wurst-Imperium/Wurst-Client+is:issue+"
							+ query;
					HttpsURLConnection connection =
						(HttpsURLConnection)new URL(url).openConnection();
					connection.connect();
					JsonObject json =
						new JsonParser().parse(
							new InputStreamReader(connection.getInputStream()))
							.getAsJsonObject();
					boolean known = json.get("total_count").getAsInt() > 0;
					if(known)
					{
						Client.wurst.chat.message("This bug has been reported before.");
						Client.wurst.chat.message("Showing existing bug reports.");
						MiscUtils
							.openLink("https://github.com/Wurst-Imperium/Wurst-Client/issues?q=is%3Aissue+"
								+ query);
					}else
					{
						String title = "Error while " + action;
						if(listener instanceof Mod)
							title +=
								" "
									+ Client.wurst.modManager.getModByClass(
										listener.getClass()).getName();
						title = URLEncoder.encode(title, "UTF-8");
						Client.wurst.chat.message("Generated a new bug report.");
						Client.wurst.chat.message("Press the green submit button to report it.");
						MiscUtils
							.openLink("https://github.com/Wurst-Imperium/Wurst-Client/issues/new?title="
								+ title + "&body=" + report);
					}
				}catch(Exception e)
				{
					e.printStackTrace();
					Client.wurst.chat.error("Bug could not be reported. :(");
					Client.wurst.chat.message("Try reporting it manually.");
					MiscUtils
						.openLink("https://github.com/Wurst-Imperium/Wurst-Client/labels/bug");
				}
				break;
			case 1:
				break;
			case 2:
				if(listener instanceof Mod)
					Client.wurst.modManager.getModByClass(listener.getClass())
						.setEnabled(false);
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
		drawScaledCustomSizeModalRect(width / 2 - 48, height / 3, 0, 0, 256,
			256, 96, 96, 256, 256);
		drawCenteredString(fontRendererObj, "§nError!§r", width / 2,
			height / 4, 0xffffffff);
		drawCenteredString(fontRendererObj,
			"An error occurred while enabling /home.", width / 2,
			height / 4 + 16, 0xffffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
