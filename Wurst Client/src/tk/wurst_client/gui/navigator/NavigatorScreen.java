package tk.wurst_client.gui.navigator;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.darkstorm.minecraft.gui.util.RenderUtil;
import org.lwjgl.input.Mouse;

import tk.wurst_client.WurstClient;
import tk.wurst_client.font.Fonts;
import tk.wurst_client.navigator.NavigatorItem;

public class NavigatorScreen extends GuiScreen
{
	private int scroll = 0;
	private static ArrayList<NavigatorItem> navigatorDisplayList =
		new ArrayList<>();
	private GuiTextField searchBar;
	
	@Override
	public void initGui()
	{
		searchBar =
			new GuiTextField(0, Fonts.segoe22, width / 2 - 100, 32, 200, 20);
		searchBar.setEnableBackgroundDrawing(false);
		searchBar.setMaxStringLength(128);
		searchBar.setFocused(true);
		
		WurstClient.INSTANCE.navigator.copyNavigatorList(navigatorDisplayList);
	}
	
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
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		
		String oldText = searchBar.getText();
		searchBar.textboxKeyTyped(typedChar, keyCode);
		String newText = searchBar.getText();
		
		if(newText.isEmpty())
		{
			WurstClient.INSTANCE.navigator
				.copyNavigatorList(navigatorDisplayList);
		}else if(!newText.equals(oldText))
		{
			WurstClient.INSTANCE.navigator.getSearchResults(
				navigatorDisplayList, newText.toLowerCase());
		}
	}
	
	@Override
	public void updateScreen()
	{
		scroll += Mouse.getDWheel() / 10;
		if(scroll > 0)
			scroll = 0;
		else
		{
			int maxScroll =
				-navigatorDisplayList.size() / 3 * 20 + height - 120;
			if(maxScroll > 0)
				maxScroll = 0;
			if(scroll < maxScroll)
				scroll = maxScroll;
		}
		searchBar.updateCursorCounter();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		// search bar
		Fonts.segoe22.drawString("Search: ", width / 2 - 150, 32, 0xffffff);
		searchBar.drawTextBox();
		
		// GL settings
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		
		// feature list
		int x = width / 2 - 50;
		RenderUtil.scissorBox(0, 59, width, height - 42);
		glEnable(GL_SCISSOR_TEST);
		for(int i = 0; i < navigatorDisplayList.size(); i++)
		{
			int y = 60 + (i / 3) * 20 + scroll;
			if(y < 40)
				continue;
			if(y > height - 40)
				break;
			int xi = 0;
			switch(i % 3)
			{
				case 0:
					xi = x - 104;
					break;
				case 1:
					xi = x;
					break;
				case 2:
					xi = x + 104;
					break;
			}
			Rectangle area = new Rectangle(xi, y, 100, 16);
			if(area.contains(mouseX, mouseY))
				glColor4f(0.375F, 0.375F, 0.375F, 0.5F);
			else
				glColor4f(0.25F, 0.25F, 0.25F, 0.5F);
			glBegin(GL_QUADS);
			{
				glVertex2d(area.x, area.y);
				glVertex2d(area.x + area.width, area.y);
				glVertex2d(area.x + area.width, area.y + area.height);
				glVertex2d(area.x, area.y + area.height);
			}
			glEnd();
			RenderUtil.boxShadow(area.x, area.y, area.x + area.width, area.y
				+ area.height);
			glEnable(GL_TEXTURE_2D);
			try
			{
				String modName = navigatorDisplayList.get(i).getName();
				Fonts.segoe15.drawString(modName, area.x
					+ (area.width - Fonts.segoe15.getStringWidth(modName)) / 2,
					area.y + 2, 0xffffff);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			glDisable(GL_TEXTURE_2D);
		}
		glDisable(GL_SCISSOR_TEST);
		
		// GL resets
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
	}
}
