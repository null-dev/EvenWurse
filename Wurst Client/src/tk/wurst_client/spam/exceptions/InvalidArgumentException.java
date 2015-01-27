package tk.wurst_client.spam.exceptions;

import tk.wurst_client.spam.tag.Tag;

public class InvalidArgumentException extends TagException
{
	public InvalidArgumentException(String message, int line, Tag tag)
	{
		super(message, line, tag);
	}
}
