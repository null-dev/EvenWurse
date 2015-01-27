package tk.wurst_client.spam.exceptions;


public class InvalidTagException extends ExceptionWithDefaultHelp
{
	public InvalidTagException(String tagname, int line)
	{
		super("There is no tag called \"" + tagname + "\".", line);
	}
}
