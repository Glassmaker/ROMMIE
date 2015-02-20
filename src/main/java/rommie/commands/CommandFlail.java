package rommie.commands;

import org.jibble.pircbot.User;
import rommie.Rommie;

public class CommandFlail extends CommandBase {

    public CommandFlail()
    {
        super("flail", 3);
    }
    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
    {
        if (args.length < 2) {
            instance.sendAction(channel, "flails at everyone");
        }
        else{
            int starting_point = message.indexOf("flail")+"flail ".length();
            String message_to_send = message.substring(starting_point).trim();
            instance.sendAction(channel, "flails at " + message_to_send);
        }
    }

    @Override
    public String getUsageHelp() {
        return "flail <target>. Supply no target to flail at everyone.";
    }

}