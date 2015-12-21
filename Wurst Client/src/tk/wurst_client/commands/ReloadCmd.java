package tk.wurst_client.commands;

import tk.wurst_client.WurstClient;
import tk.wurst_client.utils.ModuleUtils;

/**
 * Project: EvenWurse
 * Created: 21/12/15
 * Author: nulldev
 */
@Cmd.Info(name = "reload", help = "Unloads and loads again all custom modules.", syntax = "")
public class ReloadCmd extends Cmd {

    @Override
    public void execute(String[] args) throws Error {
        ModuleUtils.reloadModules();
        WurstClient.INSTANCE.chat.success("Successfully reloaded all custom modules!");
    }
}
