package rommie.commands;

import org.jibble.pircbot.User;

import rommie.Rommie;

public class CommandPart extends CommandBase {

	public CommandPart()
	{
		super("part", 1);
	}
	@Override
	public void performCommand(User user, String channel, String[] args, String message, Rommie instance) throws InterruptedException {
		if (args.length > 1) {
			this.sendUsageMessage(channel, instance);
		} else {
			instance.sendMessage(channel, "Leaving channel as commanded");
            Thread.currentThread().sleep(2000);
            instance.partChannel(channel);
		}
	}

	@Override
	public String getUsageHelp() {
		return "part";
	}

}