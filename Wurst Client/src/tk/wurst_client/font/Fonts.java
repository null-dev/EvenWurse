/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.font;

import java.awt.Font;

public class Fonts
{
	public static UnicodeFontRenderer segoe22;
	public static UnicodeFontRenderer segoe18;
	public static UnicodeFontRenderer segoe15;
	
	public static void loadFonts()
	{
		segoe22 = new UnicodeFontRenderer(new Font("Segoe UI", Font.PLAIN, 22));
		segoe18 = new UnicodeFontRenderer(new Font("Segoe UI", Font.PLAIN, 18));
		segoe15 = new UnicodeFontRenderer(new Font("Segoe UI", Font.PLAIN, 15));
	}
}
