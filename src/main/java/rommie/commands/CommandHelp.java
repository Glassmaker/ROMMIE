package rommie.commands;

import java.util.Iterator;

import org.jibble.pircbot.User;

import rommie.Rommie;

public class CommandHelp extends CommandBase {

	public CommandHelp()
	{
		super("help", 3);
	}
	@Override
	public void performCommand(User user, String channel, String[] args, String message, Rommie instance)
	{
		if (args.length < 2) {
			String s = "Available Commands: ";
			Iterator<CommandBase> iterator = instance.commands.iterator();
			while(iterator.hasNext()) {
				s += iterator.next().getCommandName() + (iterator.hasNext()?", ":"");
			}
			instance.sendMessage(channel, s);
		}
		else{
			for(CommandBase command : instance.commands) {
				if(command.matches(args[1])) {
					command.sendUsageMessage(channel, instance);
				}
			}
		}
	}

	@Override
	public String getUsageHelp() {
		return "help <Command> for usage information on a specific command. Specifying no command returns a list of registered commands.";
	}

}