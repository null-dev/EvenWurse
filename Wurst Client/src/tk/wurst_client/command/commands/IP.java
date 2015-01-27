package tk.wurst_client.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import tk.wurst_client.Client;
import tk.wurst_client.command.Command;

public class IP extends Command
{
	private static String[] commandHelp =
	{
		"Tells you the IP of the server you are currently",
		"playing on or copies it to your clipboard.",
		".ip",
		".ip copy"
	};
	
	public IP()
	{
		super("ip", commandHelp);
	}
	
	public void onEnable(String input, String[] args)
	{
		if(args == null)
			Client.Wurst.chat.message("IP: " + Client.Wurst.currentServerIP);
		else if(args[0].toLowerCase().equals("copy"))
		{
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(Client.Wurst.currentServerIP), null);
			Client.Wurst.chat.message("IP copied to clipboard.");
		}else
			commandError();
	}
}
