/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.gui.error;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiError extends GuiScreen
{
	private ResourceLocation bugTexture = new ResourceLocation("wurst/bug.png");

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "§nError!§r", width / 2, height / 4, 0xffffffff);
		drawCenteredString(fontRendererObj, "An error occurred while rendering Tracers.", width / 2, height / 4 + 10, 0xffffffff);
        mc.getTextureManager().bindTexture(bugTexture);
        drawScaledCustomSizeModalRect(width / 3 * 2, height / 3, 0, 0, 256, 256, 96, 96, 256, 256);
		//mc.displayGuiScreen(null);
	}
}
