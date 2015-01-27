package tk.wurst_client.spam.exceptions;

import tk.wurst_client.spam.tag.Tag;

public class TagException extends SpamException
{
	public final Tag tag;
	
	public TagException(String message, int line, Tag tag)
	{
		super(message, line);
		this.tag = tag;
	}
	
	@Override
	public String getHelp()
	{
		return tag.getHelp();
	}
}
