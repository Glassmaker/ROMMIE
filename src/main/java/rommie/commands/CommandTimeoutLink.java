package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;
import rommie.modules.Quote.Quote;

public class CommandTimeoutLink extends CommandBase {

    public CommandTimeoutLink()
    {
        super("timeoutlink", 3);
    }

    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
    {
        if (args.length > 1) {
            this.sendUsageMessage(channel, instance);
        } else {
            instance.sendMessage(channel, "https://www.dropbox.com/s/ix1biwtoip75uy4/Timeouts.txt?dl=0");
        }
    }

    @Override
    public String getUsageHelp() {
        return "TimeOutLink. Gives a link to the timeouts logged on the FoxStone Server";
    }
}
