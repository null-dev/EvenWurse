/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

import tk.wurst_client.WurstClient;
import tk.wurst_client.commands.Cmd.SyntaxError;
import tk.wurst_client.events.ChatOutputEvent;
import tk.wurst_client.events.listeners.ChatOutputListener;

public class CmdManager implements ChatOutputListener
{
	private final TreeMap<String, Cmd> cmds = new TreeMap<String, Cmd>(
		new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2)
			{
				return o1.compareToIgnoreCase(o2);
			}
		});
	
	public final AddAltCmd addAltCmd = new AddAltCmd();
	public final AnnoyCmd annoyCmd = new AnnoyCmd();
	public final AuthorCmd authorCmd = new AuthorCmd();
	public final BindsCmd bindsCmd = new BindsCmd();
	public final BlinkCmd blinkCmd = new BlinkCmd();
	public final ClearCmd clearCmd = new ClearCmd();
	public final DamageCmd damageCmd = new DamageCmd();
	public final DropCmd dropCmd = new DropCmd();
	public final EnchantCmd enchantCmd = new EnchantCmd();
	public final FastBreakCmd fastBreakCmd = new FastBreakCmd();
	public final FeaturesCmd featuresCmd = new FeaturesCmd();
	public final FollowCmd followCmd = new FollowCmd();
	public final FriendsCmd friendsCmd = new FriendsCmd();
	public final GetPosCmd getPosCmd = new GetPosCmd();
	public final GhostHandCmd ghostHandCmd = new GhostHandCmd();
	public final GiveCmd giveCmd = new GiveCmd();
	public final GmCmd gmCmd = new GmCmd();
	public final GoToCmd goToCmd = new GoToCmd();
	public final HelpCmd HhelpCmd = new HelpCmd();
	public final InvseeCmd invseeCmd = new InvseeCmd();
	public final IpCmd ipCmd = new IpCmd();
	public final JumpCmd jumpCmd = new JumpCmd();
	public final LeaveCmd leaveCmd = new LeaveCmd();
	public final NothingCmd nothingCmd = new NothingCmd();
	public final NukerCmd nukerCmd = new NukerCmd();
	public final PathCmd pathCmd = new PathCmd();
	public final PotionCmd potionCmd = new PotionCmd();
	public final ProtectCmd protectCmd = new ProtectCmd();
	public final RenameCmd renameCmd = new RenameCmd();
	public final RepairCmd repairCmd = new RepairCmd();
	public final RvCmd rvCmd = new RvCmd();
	public final SvCmd svCmd = new SvCmd();
	public final SayCmd sayCmd = new SayCmd();
	public final SearchCmd searchCmd = new SearchCmd();
	public final SpammerCmd spammerCmd = new SpammerCmd();
	public final TacoCmd tacoCmd = new TacoCmd();
	public final TCmd tCmd = new TCmd();
	public final ThrowCmd throwCmd = new ThrowCmd();
	public final TpCmd tpCmd = new TpCmd();
	public final VClipCmd vClipCmd = new VClipCmd();
	public final WmsCmd wmsCmd = new WmsCmd();
	public final XRayCmd xRayCmd = new XRayCmd();
	
	public CmdManager()
	{
		try
		{
			for(Field field : CmdManager.class.getFields())
			{
				if(field.getName().endsWith("Cmd"))
				{
					Cmd cmd = (Cmd)field.get(this);
					cmds.put(cmd.getName(), cmd);
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onSentMessage(ChatOutputEvent event)
	{
		String message = event.getMessage();
		if(message.startsWith("."))
		{
			event.cancel();
			String input = message.substring(1);
			String commandName = input.split(" ")[0];
			String[] args;
			if(input.contains(" "))
				args = input.substring(input.indexOf(" ") + 1).split(" ");
			else
				args = new String[0];
			Cmd cmd = getCommandByName(commandName);
			if(cmd != null)
				try
				{
					cmd.execute(args);
				}catch(SyntaxError e)
				{
					if(e.getMessage() != null)
						WurstClient.INSTANCE.chat.message("§4Syntax error:§r "
							+ e.getMessage());
					else
						WurstClient.INSTANCE.chat.message("§4Syntax error!§r");
					cmd.printSyntax();
				}catch(Cmd.Error e)
				{
					WurstClient.INSTANCE.chat.error(e.getMessage());
				}catch(Exception e)
				{
					WurstClient.INSTANCE.events.handleException(e, cmd,
						"executing", "Exact input: `" + event.getMessage()
							+ "`");
				}
			else
				WurstClient.INSTANCE.chat.error("\"." + commandName
					+ "\" is not a valid command.");
		}
	}
	
	public Cmd getCommandByName(String name)
	{
		return cmds.get(name);
	}
	
	public Collection<Cmd> getAllCommands()
	{
		return cmds.values();
	}
	
	public int countCommands()
	{
		return cmds.size();
	}
}
