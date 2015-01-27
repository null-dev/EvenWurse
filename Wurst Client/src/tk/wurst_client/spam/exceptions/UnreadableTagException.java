package tk.wurst_client.spam.exceptions;

public class UnreadableTagException extends UnreadableElementException
{
	public UnreadableTagException(String tag, int line)
	{
		super(tag, line);
	}
}
