/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.alts;

public class Alt
{
	public String name;
	public String password;
	public boolean cracked;
	
	public Alt(String name, String password)
	{
		if(password == null)
			password = "";
		this.name = name;
		this.password = password;
		if(password.length() == 0)
			cracked = true;
		else
			cracked = false;
	}
}
