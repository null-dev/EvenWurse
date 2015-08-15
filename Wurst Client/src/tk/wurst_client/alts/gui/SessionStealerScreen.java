package tk.wurst_client.alts.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;

import org.lwjgl.input.Keyboard;

import tk.wurst_client.utils.MiscUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class SessionStealerScreen extends GuiScreen
{
	protected GuiScreen prevMenu;
	protected GuiTextField usernameBox;
	protected GuiTextField tokenBox;
	
	protected String displayText = "";
	
	public SessionStealerScreen(GuiScreen par1GuiScreen)
	{
		prevMenu = par1GuiScreen;
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		usernameBox.updateCursorCounter();
		tokenBox.updateCursorCounter();
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 72 + 12,
			"Login to session"));
		buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96 + 12,
			"Search for tokens on Google"));
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12,
			"Cancel"));
		usernameBox =
			new GuiTextField(0, fontRendererObj, width / 2 - 100, 60, 200, 20);
		usernameBox.setText(mc.session.getUsername());
		usernameBox.setFocused(true);
		tokenBox =
			new GuiTextField(1, fontRendererObj, width / 2 - 100, 100, 200, 20);
		if (mc.session.getToken() != null)
			tokenBox.setText(mc.session.getToken());
	}
	
	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.enabled)
			if(button.id == 1)
				mc.displayGuiScreen(prevMenu);
			else if(button.id == 0) {
				String username = null, uuid = null;
				if (usernameBox.getText().length() <= 16) {
					username = usernameBox.getText();
					
					try {
						URLConnection connection = new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection();
						InputStream response = connection.getInputStream();
						JsonParser parser = new JsonParser();
						JsonElement root = parser.parse(new InputStreamReader(response));
						uuid = root.getAsJsonObject().get("id").getAsString();
					} catch (IOException | IllegalStateException e) {
						e.printStackTrace();
						displayText = "An error occurred while fetching UUID.";
					}
				} else {
					uuid = usernameBox.getText();
					
					try {
						URLConnection connection = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names").openConnection();
						InputStream response = connection.getInputStream();
						JsonParser parser = new JsonParser();
						JsonArray root = parser.parse(new InputStreamReader(response)).getAsJsonArray();
						username = root.get(root.size()-1).getAsJsonObject().get("name").getAsString();
					} catch (IOException | IllegalStateException e) {
						e.printStackTrace();
						displayText = "An error occurred while fetching username.";
					}
				}
				
				if (username != null && uuid != null) {
					mc.session = new Session(username, uuid, tokenBox.getText(), "mojang");
					mc.displayGuiScreen(prevMenu);
				}
			} else if(button.id == 2)
				MiscUtils.openLink("https://www.google.com/search?q=%22session+id+is+token%22+site:pastebin.com&tbs=qdr:m");
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char par1, int par2)
	{
		usernameBox.textboxKeyTyped(par1, par2);
		tokenBox.textboxKeyTyped(par1, par2);
		
		if(par2 == 28 || par2 == 156)
			actionPerformed((GuiButton)buttonList.get(0));
	}
	
	/**
	 * Called when the mouse is clicked.
	 *
	 * @throws IOException
	 */
	@Override
	protected void mouseClicked(int par1, int par2, int par3)
		throws IOException
	{
		super.mouseClicked(par1, par2, par3);
		usernameBox.mouseClicked(par1, par2, par3);
		tokenBox.mouseClicked(par1, par2, par3);
		if(usernameBox.isFocused() || tokenBox.isFocused())
			displayText = "";
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();

		drawCenteredString(fontRendererObj, "Session Stealer", width / 2, 20, 16777215);
		drawString(fontRendererObj, "Username or UUID", width / 2 - 100, 47,
			10526880);
		drawString(fontRendererObj, "Session Token", width / 2 - 100, 87, 10526880);
		drawCenteredString(fontRendererObj, displayText, width / 2, 142,
			16777215);
		
		usernameBox.drawTextBox();
		tokenBox.drawTextBox();
		
		super.drawScreen(par1, par2, par3);
	}
}
