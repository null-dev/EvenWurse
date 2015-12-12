/*
 * Copyright © 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.navigator;

import java.lang.reflect.Field;
import java.util.ArrayList;

import tk.wurst_client.WurstClient;
import tk.wurst_client.mods.ModManager;

public class Navigator
{
	private ArrayList<NavigatorItem> navigatorList = new ArrayList<>();
	
	public Navigator()
	{
		Field[] fields = ModManager.class.getFields();
		try
		{
			for(int i = 0; i < fields.length; i++)
			{
				Field field = fields[i];
				if(field.getName().endsWith("Mod"))
					navigatorList.add((NavigatorItem)field
						.get(WurstClient.INSTANCE.mods));
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void copyNavigatorList(ArrayList<NavigatorItem> list)
	{
		if(!list.equals(navigatorList))
		{
			list.clear();
			list.addAll(navigatorList);
		}
	}
	
	public void getSearchResults(ArrayList<NavigatorItem> list, String query)
	{
		list.clear();
		for(NavigatorItem mod : navigatorList)
			if(mod.getName().toLowerCase().contains(query)
				|| mod.getDescription().toLowerCase().contains(query))
				list.add(mod);
		list.sort(new SearchResultsComparator(query));
	}
}
