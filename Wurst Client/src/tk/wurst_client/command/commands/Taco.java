package tk.wurst_client.command.commands;

import tk.wurst_client.Client;
import tk.wurst_client.command.Command;
import tk.wurst_client.module.modules.TacoCMD;

public class Taco extends Command
{
	private static String[] commandHelp =
	{
		"\"I love that little guy. So cute!\" -WiZARDHAX",
		".taco"
	};
	
	public Taco()
	{
		super("taco", commandHelp);
	}
	
	public void onEnable(String input, String[] args)
	{
		Client.Wurst.moduleManager.getModuleFromClass(TacoCMD.class).toggleModule();
	}
}
