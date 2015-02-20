package rommie.commands;

import org.jibble.pircbot.User;

import rommie.Rommie;

public class CommandJoin extends CommandBase {

	public CommandJoin()
	{
		super("join", 0);
	}
	@Override
	public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
	{
		if (args.length < 1) {
			this.sendUsageMessage(channel, instance);
		} else {
			instance.joinChannel(args[1]);
			instance.sendMessage(channel, "Joined " + args[1]);
		}
	}

	@Override
	public String getUsageHelp() {
		return "join <ChannelName>";
	}

}