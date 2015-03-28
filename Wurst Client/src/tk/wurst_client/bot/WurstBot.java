package tk.wurst_client.bot;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import tk.wurst_client.Client;
import net.minecraft.client.main.Main;

public class WurstBot implements Runnable
{
	private static boolean enabled = false;
	
    public static void main(String[] args)
    {
    	enabled = true;
    	new Thread(new WurstBot()).start();
        Main.main(new String[] {"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"});
    }

	@Override
	public void run()
	{
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println();
			System.out.println("           +++++++++++++++++++++++++++++++++++++++++++++++           ");
			System.out.println("       +++#++++##++++#+#+++++++#++######+++++######+#######+++       ");
			System.out.println("     +++++#++++##++++#+#+++++++#++#+++++##++#++++++++++#++++++++     ");
			System.out.println("    +++++++#++#++#++#++#+++++++#++#######++++######++++#+++++++++    ");
			System.out.println("     ++++++#++#++#++#+++#+++++#+++#+++##+++++++++++#+++#++++++++     ");
			System.out.println("       +++++##++++##+++++#####++++#+++++##+++######++++#++++++       ");
			System.out.println("           +++++++++++++++++++++++++++++++++++++++++++++++           ");
			System.out.println();
			System.out.println("Starting WurstBot v" + Client.wurst.CLIENT_VERSION);
			while(true)
			{
				System.out.println(br.readLine());
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
