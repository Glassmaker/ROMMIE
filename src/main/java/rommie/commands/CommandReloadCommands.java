package rommie.commands;

import org.jibble.pircbot.User;

import rommie.Rommie;

public class CommandReloadCommands extends CommandBase {

	public CommandReloadCommands()
	{
		super("reloadCommands", 0);
	}
	@Override
	public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
	{
		instance.setupCommands();
//		instance.sendMessage(channel, "Commands reloaded");
	}

	@Override
	public String getUsageHelp() {
		return "reloadCommands";
	}

}