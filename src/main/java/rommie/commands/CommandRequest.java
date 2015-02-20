package rommie.commands;

import org.jibble.pircbot.User;

public class CommandRequest
{
	public CommandBase command;
	public User user;
	public String channel;
	public String message;
	public RequestStatus status = RequestStatus.PENDING;
	public CommandRequest(CommandBase command, User user, String channel, String message) {
		this.command=command;
		this.user=user;
		this.channel=channel;
		this.message=message;
	}

	public static enum RequestStatus {
		PENDING,
		APPROVED,
		DENIED;
	}
}
