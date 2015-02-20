package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;
import rommie.modules.Quote.Quote;

public class CommandSetPrefix extends CommandBase {

    public CommandSetPrefix()
    {
        super("setprefix", 0);
    }
    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
    {
        if (args.length < 2 | args.length > 2) {
            instance.sendMessage(channel, "Creator only command. Usage : " + Rommie.CMD_PREFIX + "prefix <character>");
        } else {
            Rommie.CMD_PREFIX = args[1];
            String[] channels = instance.getChannels();
            for (String ignored : channels) {
                instance.sendMessage(channel, "My command prefix has been changed to " + Rommie.CMD_PREFIX);
            }
        }
    }

    @Override
    public String getUsageHelp() {
        return "setprefix. Sets the command prefix";
    }
}