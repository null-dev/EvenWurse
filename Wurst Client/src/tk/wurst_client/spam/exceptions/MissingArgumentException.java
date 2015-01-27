package tk.wurst_client.spam.exceptions;

import tk.wurst_client.spam.tag.Tag;

public class MissingArgumentException extends TagException
{
	public MissingArgumentException(String message, int line, Tag tag)
	{
		super(message, line, tag);
	}
}
