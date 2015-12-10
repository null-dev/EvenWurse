package tk.wurst_client.gui.navigator;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;

public class NavigatorScreen extends GuiScreen
{
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException
	{
		super.mouseClicked(x, y, button);
	}
	
	@Override
	public void mouseReleased(int x, int y, int button)
	{
		super.mouseReleased(x, y, button);
	}
	
	@Override
	public void drawScreen(int par2, int par3, float par4)
	{
		super.drawScreen(par2, par3, par4);
	}
}
