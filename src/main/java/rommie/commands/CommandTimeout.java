package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;

import java.util.Date;

public class CommandTimeout extends CommandBase {

    public CommandTimeout()
    {
        super("timeout", 2);
    }

    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
    {
        if (args.length > 1) {
            this.sendUsageMessage(channel, instance);
        }
            else{
                instance.saveTimeout(user, new Date());
                instance.sendMessage(channel, "The date and time has been noted.");
            }
    }

    @Override
    public String getUsageHelp() {
        return "timeout. Logs timeouts for the FoxStone server.";
    }
}

