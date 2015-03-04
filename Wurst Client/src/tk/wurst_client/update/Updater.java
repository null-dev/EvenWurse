/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.update;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tk.wurst_client.Client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Updater
{
	private static final Logger logger = LogManager.getLogger();
	private boolean outdated;
	private JsonArray json;
	private JsonObject latestRelease;
	
	private String currentVersion;
	private int currentMajor;
	private int currentMinor;
	private int currentPatch;
	private int currentPreRelease;
	
	private String latestVersion;
	private int latestMajor;
	private int latestMinor;
	private int latestPatch;
	private int latestPreRelease;
	
	public void checkForUpdate()
	{
		try
		{
			currentVersion = Client.wurst.CLIENT_VERSION;
			outdated = false;
			try
			{
				currentMajor = Integer.parseInt(currentVersion.split("\\.")[0]);
				if(currentVersion.contains("pre"))
					currentPreRelease =
						Integer.parseInt(currentVersion
							.substring(currentVersion.indexOf("pre") + 3));
				else
					currentPreRelease = 0;
				if(currentVersion.split("\\.").length > 2)
					if(currentPreRelease == 0)
						currentPatch =
							Integer.parseInt(currentVersion.split("\\.")[2]);
					else
						currentPatch =
							Integer.parseInt(currentVersion.split("\\.")[2]
								.substring(0, currentVersion.split("\\.")[2]
									.indexOf("pre")));
				else
					currentPatch = 0;
				if(currentPreRelease == 0 || currentPatch > 0)
					currentMinor =
						Integer.parseInt(currentVersion.split("\\.")[1]);
				else
					currentMinor =
						Integer.parseInt(currentVersion.split("\\.")[1]
							.substring(0,
								currentVersion.split("\\.")[1].indexOf("pre")));
			}catch(Exception e)
			{
				logger.error("Current version (\"" + currentVersion
					+ "\") doesn't follow the semver.org syntax!", e);
			}
			HttpsURLConnection connection =
				(HttpsURLConnection)new URL(
					"https://api.github.com/repos/Wurst-Imperium/Wurst-Client/releases")
					.openConnection();
			BufferedReader load =
				new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String content = load.readLine();
			for(String line = ""; (line = load.readLine()) != null;)
				content += "\n" + line;
			load.close();
			json = new JsonParser().parse(content).getAsJsonArray();
			latestRelease = new JsonObject();
			for(JsonElement release : json)
				if(!release.getAsJsonObject().get("prerelease").getAsBoolean()
					|| currentPreRelease > 0)
				{
					latestRelease = release.getAsJsonObject();
					break;
				}
			latestVersion =
				latestRelease.get("tag_name").getAsString().substring(1);
			try
			{
				latestMajor = Integer.parseInt(latestVersion.split("\\.")[0]);
				if(latestVersion.contains("pre"))
					latestPreRelease =
						Integer.parseInt(latestVersion.substring(latestVersion
							.indexOf("pre") + 3));
				else
					latestPreRelease = 0;
				if(latestVersion.split("\\.").length > 2)
					if(latestPreRelease == 0)
						latestPatch =
							Integer.parseInt(latestVersion.split("\\.")[2]);
					else
						latestPatch =
							Integer.parseInt(latestVersion.split("\\.")[2]
								.substring(0, latestVersion.split("\\.")[2]
									.indexOf("pre")));
				else
					latestPatch = 0;
				if(latestPreRelease == 0 || latestPatch > 0)
					latestMinor =
						Integer.parseInt(latestVersion.split("\\.")[1]);
				else
					latestMinor =
						Integer.parseInt(latestVersion.split("\\.")[1]
							.substring(0,
								latestVersion.split("\\.")[1].indexOf("pre")));
			}catch(Exception e)
			{
				logger.error("Latest version (\"" + latestVersion
					+ "\") doesn't follow the semver.org syntax!", e);
			}
			try
			{
				outdated = isLatestVersionHigher();
			}catch(Exception e)
			{
				outdated = !latestVersion.equals(currentVersion);
			}
			if(outdated)
				logger.info("Update found: " + latestVersion);
			else
				logger.info("No update found.");
		}catch(Exception e)
		{
			logger.error("Unable to check for updates!", e);
		}
	}
	
	private boolean isLatestVersionHigher()
	{
		if(latestMajor > currentMajor)
			return true;
		else if(latestMajor < currentMajor)
			return false;
		else if(latestMinor > currentMinor)
			return true;
		else if(latestMinor < currentMinor)
			return false;
		else if(latestPatch > currentPatch)
			return true;
		else if(latestPatch < currentPatch)
			return false;
		else if(latestPreRelease > currentPreRelease || latestPreRelease == 0
			&& currentPreRelease > 0)
			return true;
		else
			return false;
	}
	
	public void update()
	{
		try
		{
			String url;
			if(currentPreRelease > 0)
				url =
					json.get(0).getAsJsonObject().get("html_url").getAsString();
			else
				url =
					"https://github.com/Wurst-Imperium/Wurst-Client/releases/latest";
			Desktop.getDesktop().browse(new URI(url));
		}catch(Exception e)
		{
			logger.error("Could not open update link!", e);
		}
	}
	
	public boolean isOutdated()
	{
		return outdated;
	}
	
	public String getCurrentVersion()
	{
		return currentVersion;
	}
	
	public String getLatestVersion()
	{
		return latestVersion;
	}
}
