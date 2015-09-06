/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.options;

import java.security.SecureRandom;

import tk.wurst_client.WurstClient;

public class Options
{
	public boolean autoReconnect = false;
	public boolean cleanupFailed = true;
	public boolean cleanupOutdated = true;
	public boolean cleanupRename = true;
	public boolean cleanupUnknown = true;
	public boolean forceOPDontWait = false;
	public boolean middleClickFriends = true;
	public boolean spamFont = false;
	public boolean wurstNews = true;
	
	public int autoLeaveMode = 0;
	public int modListMode = 0;
	public int autobuildMode = 1;
	public int fastbreakMode = 0;
	public int forceOPDelay = 1000;
	public int ghostHandID = 54;
	public int nukerMode = 0;
	public int searchID = 116;
	public int serverFinderThreads = 64;
	public int spamDelay = 1000;
	public int throwAmount = 16;
	
	public String forceOPList = WurstClient.INSTANCE.fileManager.wurstDir
		.getPath();
	
	public Options.GoogleAnalytics google_analytics =
		new Options.GoogleAnalytics();
	
	public class GoogleAnalytics
	{
		public boolean enabled = true;
		public int id = new SecureRandom().nextInt() & 0x7FFFFFFF;
		public long first_launch = System.currentTimeMillis() / 1000L;
		public long last_launch = System.currentTimeMillis() / 1000L;
		public int launches = 0;
	}
	
	public Options.Target target = new Options.Target();
	
	public class Target
	{	
		public boolean players = true;
		public boolean animals = true;
		public boolean monsters = true;
		public boolean golems = true;
		public boolean sleeping_players = true;
		public boolean invisible_players = true;
		public boolean invisible_mobs = true;
	}
}
