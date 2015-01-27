package tk.wurst_client.spam;

import java.util.HashMap;

public class VarManager
{
	private final HashMap<String, String> spammerVars = new HashMap<String, String>();
	private final HashMap<String, String> userVars = new HashMap<String, String>();
	
	public VarManager()
	{
		spammerVars.put("lt", "<");
		spammerVars.put("gt", ">");
		spammerVars.put("sp", " ");
		spammerVars.put("br", "\n");
	}
	
	public HashMap<String, String> getSpammerVars()
	{
		return spammerVars;
	}
	
	public void clearUserVars()
	{
		userVars.clear();
	}
	
	public void addUserVar(String name, String value)
	{
		userVars.put(name, value);
	}

	public String getValueOfVar(String varName)
	{
		if(varName.startsWith("_"))
			return spammerVars.get(varName.substring(1));
		else
			return userVars.get(varName);
	}
}
