package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;

public class CommandBug extends CommandBase {

    public CommandBug()
    {
        super("bug", 3);
    }

    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
    {
        if (args.length > 1) {
            this.sendUsageMessage(channel, instance);
        } else {
            instance.sendMessage(channel, "Got a problem? Report it here.. https://github.com/StoneWaves/ROMMIE/issues/new");
        }
    }

    @Override
    public String getUsageHelp() {
        return "Bug. Provides the link to Rommie's issue page";
    }
}