package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;

public class CommandPrefix extends CommandBase{

    public CommandPrefix()
    {
        super("prefix", 3);
    }

    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance) throws InterruptedException {
        if (args.length > 1) {
            this.sendUsageMessage(channel, instance);
        } else {
            instance.sendMessage(channel, "The current prefix is " + instance.CMD_PREFIX);
        }
    }

    @Override
    public String getUsageHelp() {
        return "Prefix. States the current command prefix";
    }
}
