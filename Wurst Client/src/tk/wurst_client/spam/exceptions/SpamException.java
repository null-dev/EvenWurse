package tk.wurst_client.spam.exceptions;

public class SpamException extends Exception
{
	public final int line;
	
	public SpamException(String message, int line)
	{
		super(message);
		this.line = line;
	}

	public String getHelp()
	{
		return "<html><center>Error! No help available.<br>Please report this at <a href=\"\">wurst-client.tk/bugs</a>!";
	}
}
