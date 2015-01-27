package tk.wurst_client.spam.exceptions;

public class InvalidVariableException extends ExceptionWithDefaultHelp
{
	public InvalidVariableException(String varname, int line)
	{
		super("There is no variable called \"" + varname + "\".", line);
	}
}
