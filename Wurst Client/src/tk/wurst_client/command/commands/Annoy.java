package tk.wurst_client.command.commands;

import tk.wurst_client.command.Command;
import tk.wurst_client.module.modules.AnnoyCMD;

public class Annoy extends Command
{
	private static String[] commandHelp =
		{
			"Annoys a player by repeating everything he says.",
			".annoy",
			".annoy <player>"
		};
	
	public Annoy()
	{
		super("annoy", commandHelp);
	}
	
	public void onEnable(String input, String[] args)
	{
		if(args == null)
		{
			AnnoyCMD.onToggledByCommand(null);
			return;
		}else
			AnnoyCMD.onToggledByCommand(args[0]);
	}
}
