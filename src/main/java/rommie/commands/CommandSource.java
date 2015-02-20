package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;

public class CommandSource extends CommandBase {

    public CommandSource()
    {
        super("source", 3);
    }

    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
    {
        if (args.length > 1) {
            this.sendUsageMessage(channel, instance);
        } else {
            instance.sendMessage(channel, "https://github.com/StoneWaves/ROMMIE");
        }
    }

    @Override
    public String getUsageHelp() {
        return "Source. Provides the link to Rommie's source code";
    }
}
