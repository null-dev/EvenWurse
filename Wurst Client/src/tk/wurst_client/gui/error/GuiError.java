/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.error;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
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
		StringWriter stacktraceWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stacktraceWriter));
		final String trace = stacktraceWriter.toString();
		switch(button.id)
		{
			case 0:
				try
				{
					String query =
						trace.replace(" ", "+").replace("\r\n", "")
							.replace("\t", "");
					query = query.substring(0, 128);
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
						Client.wurst.chat
							.message("This bug has been reported before.");
						Client.wurst.chat
							.message("Showing existing bug reports.");
						MiscUtils
							.openLink("https://github.com/Wurst-Imperium/Wurst-Client/issues?q=is%3Aissue+"
								+ query);
					}else
					{
						String title =
							URLEncoder.encode(getReportDescription(), "UTF-8");
						String report =
							URLEncoder.encode(generateReport(trace), "UTF-8");
						Client.wurst.chat
							.message("Generated a new bug report.");
						Client.wurst.chat
							.message("Press the green submit button to report it.");
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
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						if(JOptionPane.showOptionDialog(Minecraft
							.getMinecraft().getFrame(), trace,
							"Stacktrace", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null,
							new String[]{"Close", "Copy to Clipboard"}, 0) == 1)
							Toolkit.getDefaultToolkit().getSystemClipboard()
								.setContents(new StringSelection(trace), null);
					}
				}).start();
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
	
	private String getReportDescription()
	{
		String title = "An error occurred ";
		if(listener instanceof Mod)
			title +=
				"in `"
					+ Client.wurst.modManager.getModByClass(
						listener.getClass()).getName() + "` ";
		else if(listener instanceof Command)
			title += "in `." + ((Command)listener).getName() + "` ";
		title += "while " + action + ".";
		return title;
	}
	
	private String generateReport(String trace)
	{
		String report = "# Description\n"
			+ getReportDescription() + "\n\n"
			+ "# Stacktrace\n"
			+ "```\n" + trace + "```"
			+ "\n\n# System details\n"
			+ "- OS: " + System.getProperty("os.name")
			+ " (" + System.getProperty("os.arch") + ")\n"
			+ "- Java version: "
			+ System.getProperty("java.version") + " ("
			+ System.getProperty("java.vendor") + ")\n"
			+ "- Wurst version: "
			+ Client.wurst.updater.getCurrentVersion() + " (latest: "
			+ Client.wurst.updater.getLatestVersion() + ")\n";
		return report;
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
		drawCenteredString(fontRendererObj, getReportDescription(), width / 2,
			height / 4 + 16, 0xffffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
