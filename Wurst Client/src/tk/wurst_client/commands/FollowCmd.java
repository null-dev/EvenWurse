/*
 * Copyright © 2014 - 2015 | Alexander01998 | All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.commands;

import net.minecraft.entity.EntityLivingBase;
import tk.wurst_client.WurstClient;
import tk.wurst_client.mods.FollowMod;
import tk.wurst_client.utils.EntityUtils;

@Cmd.Info(help = "Toggles Follow or makes it target a specific entity.",
	name = "follow",
	syntax = {"[<entity>]"})
public class FollowCmd extends Cmd
{
	@Override
	public void execute(String[] args) throws Error
	{
		if(args.length > 1)
			syntaxError();
		FollowMod followMod =
			(FollowMod)WurstClient.INSTANCE.modManager.getModByClass(FollowMod.class);
		if(args.length == 0)
			followMod.toggle();
		else
		{
			if(followMod.isEnabled())
				followMod.setEnabled(false);
			EntityLivingBase entity = EntityUtils.searchEntityByName(args[0]);
			if(entity == null)
				error("Entity \"" + args[0] + "\" could not be found.");
			followMod.setEnabled(true);
			followMod.setEntity(entity);
		}
	}
}
