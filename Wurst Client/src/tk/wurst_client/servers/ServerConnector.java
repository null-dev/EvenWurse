package tk.wurst_client.servers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tk.wurst_client.utils.EmptyFutureListener;

public class ServerConnector
{
    private static final AtomicInteger threadNumber = new AtomicInteger(0);
    public static final Logger logger = LogManager.getLogger();
    public NetworkManager networkManager;
    private final GuiScreen prevMenu;
	public Minecraft mc;
	public Connection connection;
	public String lastIP;
	public int lastPort;
    
    public ServerConnector(GuiScreen prevMenu, Minecraft mc)
    {
        this.mc = mc;
        this.prevMenu = prevMenu;
    }
    
    public enum Connection
    {
    	SUCCESSFUL,
    	FAILED;
    }
    
    public void connect(final String ip, final int port)
    {
        connect(ip, port, this.mc.getSession());
    }
    
    public void connect(final String ip, final int port, final Session session)
    {
    	this.lastIP = ip;
    	this.lastPort = port;
        new Thread("Wurst Server Connector #" + threadNumber.incrementAndGet())
        {
            public void run()
            {
                try
                {
                	logger.info("Connecting to " + ip + ", " + port);
                    ServerConnector.this.networkManager = NetworkManager.provideLanClient(InetAddress.getByName(ip), port);
                    ServerConnector.this.networkManager.setNetHandler(new NetHandlerLoginClient(ServerConnector.this.networkManager, null, ServerConnector.this.prevMenu));
                    ServerConnector.this.networkManager.sendPacket((Packet)new C00Handshake(47, ip, port, EnumConnectionState.LOGIN), new EmptyFutureListener());
                    ServerConnector.this.networkManager.sendPacket(new C00PacketLoginStart(session.getProfile()), new EmptyFutureListener());
                    logger.info("Connection successful");
                    connection = Connection.SUCCESSFUL;
                }catch(UnknownHostException var2)
                {
                    logger.error("Couldn\'t connect to server", var2);
                    connection = Connection.FAILED;
                }catch(Exception var3)
                {
                	logger.error("Couldn\'t connect to server", var3);
                	connection = Connection.FAILED;
                }
            }
        }.start();
	}
}
