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
	private String email;
	private String name;
	private String password;
	private boolean cracked;
	
	public Alt(String email, String password)
	{
		if(password == null || password.isEmpty())
		{	
			this.email = email;
			this.name = email;
			this.password = null;
			this.cracked = true;
		}else
		{
			this.email = email;
			this.name = LoginManager.getName(email, password);
			this.password = password;
			cracked = false;
		}
	}

	public Alt(String email, String name, String password)
	{
		if(password == null || password.isEmpty())
		{	
			this.email = email;
			this.name = email;
			this.password = null;
			this.cracked = true;
		}else
		{
			this.email = email;
			this.name = name;
			this.password = password;
			cracked = false;
		}
	}

	public Alt(String crackedName)
	{
		this.email = crackedName;
		this.name = crackedName;
		this.password = null;
		this.cracked = true;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getName()
	{
		if(name != null)
			return name;
		else if(email != null)
			return email;
		else
			return "";
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		if(password == null || password.isEmpty())
		{
			cracked = true;
			return "";
		}else
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isCracked()
	{
		return cracked;
	}

	public void setCracked(boolean cracked)
	{
		this.cracked = cracked;
	}
}
