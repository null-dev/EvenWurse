package tk.wurst_client.spam.exceptions;

public class UnreadableElementException extends ExceptionWithDefaultHelp
{
	public UnreadableElementException(String element, int line)
	{
		super("This could not be read: " + (element.contains("\n") ? element.substring(0, element.indexOf("\n")) : element), line);
	}
}
