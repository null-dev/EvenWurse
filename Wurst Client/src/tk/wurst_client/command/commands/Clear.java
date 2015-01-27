package tk.wurst_client.command.commands;

import net.minecraft.client.Minecraft;
import tk.wurst_client.command.Command;

public class Clear extends Command
{
	private static String[] commandHelp =
	{
		"Clears the chat completely.",
		".clear"
	};
	
	public Clear()
	{
		super("clear", commandHelp);
	}
	
	public void onEnable(String input, String[] args)
	{
		if(args == null)
			Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
		else
			commandError();
	}
}
