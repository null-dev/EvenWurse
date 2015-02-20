/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.alts;

import java.security.KeyPair;

public class Encryption
{
	private static KeyPair key;

	public static String encrypt(String string)
	{
		return string;
	}

	public static String decrypt(String string)
	{
		return string;
	}
	
	private static KeyPair getKey()
	{
		checkKey();
		return key;
	}
	
	private static void generateKey()
	{
		
	}
	
	private static boolean hasKey()
	{
		return false;
	}
	
	private static void checkKey()
	{
		if(!hasKey())
			generateKey();
	}
}
