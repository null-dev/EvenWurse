package tk.wurst_client.module.modules;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.StringUtils;
import tk.wurst_client.Client;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class MassTPA extends Module
{
	public MassTPA()
	{
		super
		(
			"MassTPA",
			"Sends a TPA requests to all players.\n"
			+ "Stops if someone accepts.",
			0,
			Category.CHAT
		);
	}
	
	private float speed = 1F;
	private int i;
	private String[] players;
	
	public void onEnable()
	{
		i = 0;
		Iterator itr = Minecraft.getMinecraft().getNetHandler().getPlayerInfo().iterator();
		ArrayList<String> playerList = new ArrayList<String>();
		while(itr.hasNext())
			playerList.add(StringUtils.stripControlCodes(((NetworkPlayerInfo)itr.next()).getPlayerNameForReal()));
		players = playerList.toArray(new String[playerList.size()]);
			
	}
	
	public void onReceivedMessage(String message)
	{
		if(!this.getToggled() || message.startsWith("§c[§6" + Client.Wurst.CLIENT_NAME + "§c]§f "))
			return;
		if(message.toLowerCase().contains("/help") || message.toLowerCase().contains("permission"))
		{
			Client.Wurst.chat.message("§4§lERROR:§f This server doesn't have TPA.");
			this.setToggled(false);
		}else if((message.toLowerCase().contains("accepted") && message.toLowerCase().contains("request")) || (message.toLowerCase().contains("akzeptiert") && message.toLowerCase().contains("anfrage")))
		{
			Client.Wurst.chat.message("Someone accepted your TPA request. Stopping.");
			this.setToggled(false);
		}
	}
	
	public void onUpdate()
	{
		if(this.getToggled())
		{
			updateMS();
			if(hasTimePassedS(speed))
			{
				String name = players[i];
				if(!name.equals(Minecraft.getMinecraft().thePlayer.getName()))
					Minecraft.getMinecraft().thePlayer.sendChatMessage("/tpa " + name);
				updateLastMS();
				i++;
				if(i == players.length)
					this.setToggled(false);
			}
		}
	}
}
