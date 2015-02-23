package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;

public class CommandHug extends CommandBase{

    public CommandHug()
{
    super("hug", 3);
}

    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
    {
        if (args.length < 2) {
            instance.sendAction(channel, "hugs everyone");
        }
        else{
            int starting_point = message.indexOf("fox")+"fox ".length();
            String message_to_send = message.substring(starting_point).trim();
            instance.sendAction(channel, "hugs " + message_to_send);
        }
    }

    @Override
    public String getUsageHelp() {
        return "hug <target>. Supply no target to hug everyone.";
    }

}
