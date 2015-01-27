package tk.wurst_client.spam.tag;

import java.util.ArrayList;

import tk.wurst_client.spam.exceptions.InvalidTagException;
import tk.wurst_client.spam.exceptions.SpamException;
import tk.wurst_client.spam.tag.tags.*;

public class TagManager
{
	private final ArrayList<Tag> activeTags = new ArrayList<Tag>();
	
	public Tag getTagByName(String name, int line) throws SpamException
	{
		for(int i = 0; i < activeTags.size(); i++)
		{
			if(activeTags.get(i).getName().equals(name))
				return activeTags.get(i);
		}
		throw(new InvalidTagException(name, line));
	}
	
	public ArrayList<Tag> getActiveTags()
	{
		return activeTags;
	}
	
	public String process(TagData tagData) throws SpamException
	{
		Tag tag = getTagByName(tagData.getTagName(), tagData.getTagLine());
		String processedTag = tag.process(tagData);
		return processedTag;
	}
	
	public TagManager()
	{
		this.activeTags.add(new Random());
		this.activeTags.add(new Repeat());
		this.activeTags.add(new Var());
	}
}
