package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;
import rommie.modules.Quote.Quote;

public class CommandQuote extends CommandBase {

    public CommandQuote()
    {
        super("quote", 3);
    }

    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
    {
        if (args.length > 1) {
            this.sendUsageMessage(channel, instance);
        } else {
            instance.sendMessage(channel, Quote.quote());
        }
    }

    @Override
    public String getUsageHelp() {
        return "Quote. Gives a random quote from iheartquotes.com";
    }
}

