/*
 * Copyright � 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.navigator;

import tk.wurst_client.analytics.AnalyticsManager;
import tk.wurst_client.analytics.DoNothingAnalyticsManagerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Navigator
{
	private ArrayList<NavigatorItem> navigatorList = new ArrayList<>();
	private final HashMap<String, Long> clicksMap = new HashMap<>();
	//FIXME No analytics
//	public AnalyticsManager analytics = new GoogleAnalyticsManagerImpl("UA-52838431-7",
//		"navigator.client.wurst-client.tk");
	public AnalyticsManager analytics = new DoNothingAnalyticsManagerImpl();

	public Navigator() {}
	
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
		list.addAll(navigatorList.stream().filter(mod -> mod.getName().toLowerCase().contains(query)
				|| mod.getDescription().toLowerCase().contains(query)).collect(Collectors.toList()));
		list.sort(new SearchResultsComparator(query));
	}
	
	public long getClicks(String feature)
	{
		Long clicks = clicksMap.get(feature);
		if(clicks == null)
			clicks = 0L;
		return clicks;
	}
	
	public void addClick(String feature)
	{
		Long clicks = clicksMap.get(feature);
		if(clicks == null)
			clicks = 0L;
		clicks++;
		clicksMap.put(feature, clicks);
	}
	
	public void setClicks(String feature, long clicks)
	{
		clicksMap.put(feature, clicks);
	}
	
	public Iterator<Entry<String, Long>> getClicksIterator()
	{
		return clicksMap.entrySet().iterator();
	}
	
	public void sortFeatures()
	{
		navigatorList.sort((o1, o2) -> {
            long clicks1 = getClicks(o1.getName());
            long clicks2 = getClicks(o2.getName());
            if(clicks1 < clicks2)
                return 1;
            else if(clicks1 > clicks2)
                return -1;
            else
                return 0;
        });
	}

	public ArrayList<NavigatorItem> getNavigatorList() {
		return navigatorList;
	}
}
