package rommie.commands;

import org.jibble.pircbot.User;

import rommie.Rommie;

public class CommandBoop extends CommandBase {

	public CommandBoop()
	{
		super("boop", 3);
	}
	@Override
	public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
	{
		if (args.length < 2) {
			instance.sendAction(channel, "boops everyone");
		}
		else{
			int starting_point = message.indexOf("boop")+"boop ".length();
			String message_to_send = message.substring(starting_point).trim();
			instance.sendAction(channel, "boops " + message_to_send);
		}
	}

	@Override
	public String getUsageHelp() {
		return "boop <target>. Supply no target to boop everyone.";
	}

}