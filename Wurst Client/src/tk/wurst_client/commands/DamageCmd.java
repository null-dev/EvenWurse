/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.client.*;
import net.minecraft.network.play.client.*;

@Cmd.Info(help = "Damages you with given amount.", name = "damage", syntax = {"[amount]"})
public class DamageCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
        if (args.length == 0)
        	syntaxError();	
        if (!isNumeric((args[0])))
        	syntaxError("Amount must be a number.");
		final Minecraft mc = Minecraft.getMinecraft();
        final double dmg = Double.parseDouble(args[0]);
        final double x = mc.thePlayer.posX;
        final double y = mc.thePlayer.posY;
        final double z = mc.thePlayer.posZ;
        final C03PacketPlayer.C04PacketPlayerPosition GroundFalse = new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.3, z, false);
        final C03PacketPlayer.C04PacketPlayerPosition setDamage = new C03PacketPlayer.C04PacketPlayerPosition(x, y - 3.1 - dmg, z, false);
        final C03PacketPlayer.C04PacketPlayerPosition GroundTrue = new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true);        
        if(Minecraft.getMinecraft().isIntegratedServerRunning()
    			&& Minecraft.getMinecraft().thePlayer.sendQueue.getPlayerInfo()
    				.size() == 1)
        	error("Cannot damage when in singleplayer.");
        else if (dmg >= 0 && dmg < 40)
        
        {	if (!mc.thePlayer.isOnLadder() && mc.thePlayer.onGround && !mc.thePlayer.capabilities.isCreativeMode)
        mc.getNetHandler().addToSendQueue(GroundFalse);
        mc.getNetHandler().addToSendQueue(setDamage);
        mc.getNetHandler().addToSendQueue(GroundTrue);
        }
		else
			syntaxError("Amount is too low or too high.");
	}
}
