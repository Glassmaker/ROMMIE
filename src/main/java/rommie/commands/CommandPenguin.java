package rommie.commands;

import org.jibble.pircbot.User;

import rommie.Rommie;

public class CommandPenguin extends CommandBase {

	public CommandPenguin()
	{
		super("penguin", 3);
	}

	@Override
	public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
	{
		if (args.length < 2) {
			instance.sendAction(channel, "starts the penguin invasion!");
		}
		else{
			int starting_point = message.indexOf("penguin")+"penguin ".length();
			String message_to_send = message.substring(starting_point).trim();
			instance.sendAction(channel, "turns " + message_to_send + " into her penguin slave.");
		}
	}

	@Override
	public String getUsageHelp() {
		return "penguin <target>. Without a target the penguins shall kill you all.";
	}

}
