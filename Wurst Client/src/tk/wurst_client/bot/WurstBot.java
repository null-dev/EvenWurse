package tk.wurst_client.bot;
import net.minecraft.client.main.Main;

public class WurstBot
{
	private static boolean enabled = false;
	
    public static void main(String[] args)
    {
    	enabled = true;
        Main.main(new String[] {"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"});
    }

	public static boolean isEnabled()
	{
		return enabled;
	}
}
