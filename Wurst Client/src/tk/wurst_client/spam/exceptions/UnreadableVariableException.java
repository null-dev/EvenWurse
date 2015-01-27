package tk.wurst_client.spam.exceptions;

public class UnreadableVariableException extends UnreadableElementException
{
	public UnreadableVariableException(String var, int line)
	{
		super(var, line);
	}
}
