package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;

public class CommandSetTopic extends CommandBase {

    public CommandSetTopic()
    {
        super("settopic", 1);
    }

    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
    {
        if (args.length < 2) {
            this.sendUsageMessage(channel, instance);
        }else {
            int starting_point = message.indexOf(args[1]) + 1;
            String message_to_send = message.substring(starting_point);

            instance.setTopic(channel, args[1] + "  " + message_to_send);
        }
    }

    @Override
    public String getUsageHelp() {
        return "setTopic <topic>. Sets the channel topic.";
    }

}