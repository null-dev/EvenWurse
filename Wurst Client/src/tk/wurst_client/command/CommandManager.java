package tk.wurst_client.command;

import java.util.ArrayList;

import tk.wurst_client.command.commands.*;

public class CommandManager
{
	public ArrayList<Command> activeCommands = new ArrayList<Command>();
	
	public CommandManager()
	{
		this.activeCommands.add(new AddAlt());
		this.activeCommands.add(new Annoy());
		this.activeCommands.add(new Bind());
		this.activeCommands.add(new Clear());
		this.activeCommands.add(new Drop());
		this.activeCommands.add(new Enchant());
		this.activeCommands.add(new FastBreakMod());
		this.activeCommands.add(new Features());
		this.activeCommands.add(new Friends());
		this.activeCommands.add(new GM());
		this.activeCommands.add(new Help());
		this.activeCommands.add(new IP());
		this.activeCommands.add(new NukerMod());
		this.activeCommands.add(new RenameForceOPEvenThoughTheNameIsTechnicallyCorrect());
		this.activeCommands.add(new RV());
		this.activeCommands.add(new Say());
		this.activeCommands.add(new SearchMod());
		this.activeCommands.add(new SpammerMod());
		this.activeCommands.add(new Taco());
		this.activeCommands.add(new ThrowMod());
		this.activeCommands.add(new Toggle());
		this.activeCommands.add(new TP());
		this.activeCommands.add(new VClip());
		this.activeCommands.add(new XRay());
	}
}