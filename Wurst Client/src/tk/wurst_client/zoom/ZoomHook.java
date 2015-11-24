/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.zoom;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import tk.wurst_client.WurstClient;

public class ZoomHook
{
	private static float scrollLevel = WurstClient.INSTANCE.options.zoom.level;
	
	public static float changeFovBasedOnZoom(float fov)
	{
		if(Keyboard.isKeyDown(WurstClient.INSTANCE.options.zoom.keybind))
		{
			if(WurstClient.INSTANCE.options.zoom.scroll)
			{
				int scroll = Mouse.getDWheel();
				if(scroll > 0)
					scrollLevel =
						Math.min(Math.round(scrollLevel * 11F) / 10F, 10F);
				else if(scroll < 0)
					scrollLevel =
						Math.max(Math.round(scrollLevel * 9F) / 10F, 1F);
				fov /= scrollLevel;
			}else
				fov /= WurstClient.INSTANCE.options.zoom.level;
		}else if(scrollLevel != WurstClient.INSTANCE.options.zoom.level)
			scrollLevel = WurstClient.INSTANCE.options.zoom.level;
		return fov;
	}
}
