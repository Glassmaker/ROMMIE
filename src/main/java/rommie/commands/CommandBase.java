package rommie.commands;

import org.jibble.pircbot.User;

import rommie.Rommie;

public abstract class CommandBase {
	/**
	 * The required level a user has to be to use the command:<br>
	 * 0: Owner of the bot, authorized via NickServ<br>
	 * 1: Channel Operator<br>
	 * 2: Channel Voice<br>
	 * 3: Public
	 */
	final int commandLevel;
	/**
	 * The unique string identifier for this command
	 */
	final String commandName;
	public CommandBase(String commandName, int commandLevel) {
		this.commandName = commandName;
		this.commandLevel = commandLevel;
	}

	/**Whether the given command matches this class
	 */
	public boolean matches(String command) {
		return this.commandName.equalsIgnoreCase(command);
	}
	/**Returns the required command level<br>
	 * 0: Owner of the bot, authorized via NickServ<br>
	 * 1: Channel Operator<br>
	 * 2: Channel Voice<br>
	 * 3: Public
	 */
	public int getCommandLevel()
	{
		return commandLevel;
	}
	/**Returns the unique string identifier for this command
	 */
	public String getCommandName()
	{
		return commandName;
	}


	public abstract void performCommand(User user, String channel, String[] args, String message, Rommie instance);

	public void sendUsageMessage(String channel, Rommie instance)
	{
		instance.sendMessage(channel, getPermissionMessage()+"Usage : "+Rommie.CMD_PREFIX+getUsageHelp());
	}
	public String getPermissionMessage()
	{
		switch(commandLevel) {
		case 0:
			return "Creator only command. ";
		case 1:
			return "Operator only command. ";
		case 2:
			return "Voice only command. ";
		case 3:
		default:
			return "";
		}
	}
	public abstract String getUsageHelp();
}