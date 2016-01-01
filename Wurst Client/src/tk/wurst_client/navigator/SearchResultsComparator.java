/*
 * Copyright � 2014 - 2015 | Alexander01998 and contributors
 * All rights reserved.
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package tk.wurst_client.navigator;

import java.util.Comparator;

public class SearchResultsComparator implements Comparator<NavigatorItem>
{
	private String query;
	
	public SearchResultsComparator(String query)
	{
		this.query = query;
	}
	
	@Override
	public int compare(NavigatorItem o1, NavigatorItem o2)
	{
		int result = compareNext(o1.getName(), o2.getName());
		if(result != 0)
			return result;
		
		result = compareNext(o1.getDescription(), o2.getDescription());
		return result;
	}
	
	private int compareNext(String o1, String o2)
	{
		int index1 = o1.toLowerCase().indexOf(query);
		int index2 = o2.toLowerCase().indexOf(query);
		
		if(index1 == index2)
			return 0;
		else if(index1 == -1)
			return 1;
		else if(index2 == -1)
			return -1;
		else
			return index1 - index2;
	}
}
