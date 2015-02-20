package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;

public class CommandTable extends CommandBase{

    public CommandTable()
    {
        super("table", 3);
    }

    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance) throws InterruptedException {
        if (args.length > 1) {
            this.sendUsageMessage(channel, instance);
        } else {
            instance.sendMessage(channel, "(╯°□°）╯︵ ┻━┻");
        }
    }

    @Override
    public String getUsageHelp() {
        return "Table. For use when annoyed!";
    }
}
