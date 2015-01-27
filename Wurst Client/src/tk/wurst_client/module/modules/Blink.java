package tk.wurst_client.module.modules;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import tk.wurst_client.module.Category;
import tk.wurst_client.module.Module;

public class Blink extends Module
{
	public Blink()
	{
		super
		(
			"Blink",
			"Makes it harder for other players to see where you are.\n"
			+ "They will think you are lagging badly, because your\n"
			+ "position will only be updated every 3 seconds.",
			0,
			Category.MOVEMENT
		);
	}
	
	private static ArrayList<Packet> packets = new ArrayList<Packet>();
	
	public void onUpdate()
	{
		if(!this.getToggled())
			return;
		updateMS();
		if(hasTimePassedM(3000))
		{
			for(Packet packet : packets)
			{
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
			}
			packets.clear();
			updateLastMS();
		}
	}
	
	public static void addToBlinkQueue(Packet packet)
	{
		packets.add(packet);
	}
}
