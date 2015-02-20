package rommie.commands;

import org.jibble.pircbot.User;

import rommie.Rommie;

public class CommandFox extends CommandBase {

	public CommandFox()
	{
		super("fox", 3);
	}

	@Override
	public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
	{
		if (args.length < 2) {
			instance.sendAction(channel, "throws foxes at everyone");
		}
		else{
			int starting_point = message.indexOf("fox")+"fox ".length();
			String message_to_send = message.substring(starting_point).trim();
			instance.sendAction(channel, "throws a fox at " + message_to_send);
		}
	}

	@Override
	public String getUsageHelp() {
		return "fox <target>. Supply no target to throw foxes at everyone.";
	}

}