/*
 * Copyright © 2014 - 2015 Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
//import saint.eventstuff.Event;
//import saint.eventstuff.events.PreMotion;
//import saint.modstuff.Module;

import tk.wurst_client.mods.Mod.Category;
import tk.wurst_client.mods.Mod.Info;

@Info(category = Category.EXPLOITS,
	description = "Paralyze your enemies by walking to them.",
	name = "Paralyze")
public class Paralyze extends Mod
{	

@Override
	public void onUpdate()
	{
/*if ((event instanceof PreMotion)) { //??*/
      for (int i = 0; i < 40001; i++) {
        mc.getNetHandler().addToSendQueue(
          new C03PacketPlayer(mc.thePlayer.onGround));
      }
    }
}
