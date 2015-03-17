/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.error;

import net.minecraft.client.gui.GuiScreen;

public class GuiError extends GuiScreen
{
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "§nError!§r", width / 2, height / 4, 0xffffffff);
		drawCenteredString(fontRendererObj, "An error occurred while...", width / 2, height / 4 + 10, 0xffffffff);
		//mc.displayGuiScreen(null);
	}
}
