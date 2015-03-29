package tk.wurst_client.bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.minecraft.client.main.Main;
import tk.wurst_client.Client;
import tk.wurst_client.bot.commands.Command;
import tk.wurst_client.bot.commands.CommandManager;

public class WurstBot implements Runnable
{
	private static boolean enabled = false;
	private CommandManager commandManager;
	
	public WurstBot()
	{
		commandManager = new CommandManager();
	}
	
	public static void main(String[] args)
	{
		enabled = true;
		System.out.println("Starting Wurst-Bot...");
		Main.main(new String[]{"--version", "mcp", "--accessToken", "0",
			"--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties",
			"{}"});
	}
	
	@Override
	public void run()
	{
		try
		{
			BufferedReader br =
				new BufferedReader(new InputStreamReader(System.in));
			System.out.println();
			System.out
				.println("           +++++++++++++++++++++++++++++++++++++++++++++++           ");
			System.out
				.println("       +++#++++##++++#+#+++++++#++######+++++######+#######+++       ");
			System.out
				.println("     +++++#++++##++++#+#+++++++#++#+++++##++#++++++++++#++++++++     ");
			System.out
				.println("    +++++++#++#++#++#++#+++++++#++#######++++######++++#+++++++++    ");
			System.out
				.println("     ++++++#++#++#++#+++#+++++#+++#+++##+++++++++++#+++#++++++++     ");
			System.out
				.println("       +++++##++++##+++++#####++++#+++++##+++######++++#++++++       ");
			System.out
				.println("           +++++++++++++++++++++++++++++++++++++++++++++++           ");
			System.out.println();
			System.out.println("Wurst-Bot v" + Client.wurst.CLIENT_VERSION);
			while(true)
			{
				String input = br.readLine();
				String commandName = input.split(" ")[0];
				String[] args;
				if(input.contains(" "))
					args = input.substring(input.indexOf(" ") + 1).split(" ");
				else
					args = new String[0];
				Command command = commandManager.getCommandByName(commandName);
				if(command != null)
					try
					{
						command.execute(args);
					}catch(Command.SyntaxError e)
					{
						if(e.getMessage() != null)
							System.err.println("Syntax error: "
								+ e.getMessage());
						else
							System.err.println("Syntax error!");
						command.printSyntax();
					}catch(Command.Error e)
					{
						System.err.println(e.getMessage());
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				else
					System.err.println("\"" + commandName
						+ "\" is not a valid command.");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean isEnabled()
	{
		return enabled;
	}
}
