package rommie;

import org.jibble.pircbot.PircBot;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Rommie extends PircBot {

    private static String CMD_PREFIX = ">";
    private String creator = "StoneWaves";
    String MessageChannel = "#Rommie";
    private static final String DataPath = ".\\Files\\";
    DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    ArrayList ChannelTasks = new ArrayList();
    ArrayList tasks = new ArrayList();
    int foxcount = 0;
    String[] commands = {"time", "tell", "join", "part", "disconnect", "fox", "count", "table", "sfw", "nsfw", "kick", "ban", "prefix", "task", "jobs", "remove"};


    public Rommie() {
        this.setName("Rommie");
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {


        if (message.startsWith(CMD_PREFIX)) {
            message = message.substring(CMD_PREFIX.length()); //Strips command prefix

            String[] arguments = message.split(" ");
            String command = null;

            if (arguments.length > 0 && arguments[0].length() > 0) {
                command = arguments[0].toLowerCase().trim();
            }

            //----------------------------------------------------------------------------------------------------------

            if (command == null) {
                return;
            }

            //----------------------------------------------------------------------------------------------------------

            if (command.equalsIgnoreCase("time")) {
                sendMessage(channel, new java.util.Date().toString());
            }

            //----------------------------------------------------------------------------------------------------------

            //this is just an example; is silly and pointless XP
            //usage is "tell person message"
            if (command.equalsIgnoreCase("tell")) {
                if (arguments.length < 3) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "tell <User> <Message>");
                } else {
                    String nick = arguments[1];
                    int starting_point = message.indexOf(arguments[1]) + arguments[1].length() + 1;
                    String message_to_send = message.substring(starting_point);
                    sendMessage(channel, arguments[1] + ": " + message_to_send);
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Command to join a channel
            if (command.equalsIgnoreCase("join") & sender.equalsIgnoreCase(creator)) {
                if (arguments.length < 1) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "join <ChannelName>");
                } else {
                    joinChannel(arguments[1]);
                    sendMessage(channel, "Joined " + arguments[1]);
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Command to part a channel
            if (command.equalsIgnoreCase("part") & sender.equalsIgnoreCase(creator)) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "part");
                } else {
                    sendMessage(channel, "Leaving channel as commanded");
                    partChannel(channel);
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Command to quit IRC
            if (command.equalsIgnoreCase("disconnect") & sender.equalsIgnoreCase(creator)) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "quit");
                } else {
                    sendMessage(channel, "Disconnecting from IRC");
                    disconnect();
                    System.exit(0);
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Throw a fox at a user
            if (command.equalsIgnoreCase("fox")) {
                if (arguments.length < 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "fox <User>");
                } else {
                    sendAction(channel, "throws a fox at " + arguments[1]);
                    foxcount = foxcount + 1;
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Lists all commands
            if (command.equalsIgnoreCase("count")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "count");
                } else {
                   sendMessage(channel, "A total of " + foxcount + " foxes have been thrown.");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Flip a table
            if (command.equalsIgnoreCase("table")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "table");
                } else {
                    sendMessage(channel, "(╯°□°）╯︵ ┻━┻");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Sets topic to SFW
            if (command.equalsIgnoreCase("sfw") & channel.equalsIgnoreCase("#FetishCraft")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "This is channel specific to #FetishCraft. Usage : " + CMD_PREFIX + "sfw");
                } else {
                    setTopic(channel, "Currently SFW | Don't judge and be friendly <3 | We might also make a mod one day");
                    sendNotice(channel, "Channel is now SFW");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Sets topic to NSFW
            if (command.equalsIgnoreCase("nsfw") & channel.equalsIgnoreCase("#FetishCraft")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "This is channel specific to #FetishCraft. Usage : " + CMD_PREFIX + "sfw");
                } else {
                    setTopic(channel, "NSFW Channel | Don't judge and be friendly <3 | We might also make a mod one day");
                    sendNotice(channel, "Channel is now SFW");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Kicks a user
            if (command.equalsIgnoreCase("kick") & sender.equalsIgnoreCase(creator)) {
                if (arguments.length < 2 | arguments.length > 2) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "kick <User>");
                } else {
                    kick(channel, arguments[1]);
                    sendMessage(channel, arguments[1] + " was kicked by " + sender);
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Bans a user
            if (command.equalsIgnoreCase("ban") & sender.equalsIgnoreCase(creator)) {
                if (arguments.length < 2 | arguments.length > 2) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "ban <User>");
                } else {
                    ban(channel, arguments[1]);
                    sendMessage(channel, arguments[1] + " was banned from " + channel + " by " + sender);
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Sets the command prefix
            if (command.equalsIgnoreCase("prefix") & sender.equalsIgnoreCase(creator)) {
                if (arguments.length < 2 | arguments.length > 2) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "prefix <character>");
                } else {
                    CMD_PREFIX = arguments[1];
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Lists all commands
            if (command.equalsIgnoreCase("help")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "help");
                } else {
                    sendMessage(sender, "Here is a list of all my commands.");
                    sendMessage(sender, "The current command prefix is " + CMD_PREFIX);
                    for (int i = 0; i < commands.length; i++) {
                        sendMessage(sender, commands[i]);
                    }
                    sendMessage(sender, "For more information on any command type " + CMD_PREFIX + "<command>");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Add an item to the task list
            if (command.equalsIgnoreCase("task")) {
                if (arguments.length < 2) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "task <Item>");
                } else {

                    int starting_point = arguments[1].length() + 1;
                    String task = message.substring(starting_point);

                    ChannelTasks.add(channel);
                    tasks.add(task);

                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Add an item to the task list
            if (command.equalsIgnoreCase("jobs")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "jobs");
                } else {

                    String jobs = "";

                    sendMessage(channel, "These are the outstanding tasks.");

                    for (int i = 0; tasks.size() > i; i++) {
                        if (ChannelTasks.get(i).equals(channel)) {
                            jobs = (String.valueOf(i + ". " + tasks.get(i)));
                            sendMessage(channel, jobs);
                        }
                    }
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Add an item to the task list
            if (command.equalsIgnoreCase("remove")) {
                if (arguments.length < 1 | arguments.length > 1 ) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "remove <task id>");
                } else {
                    if (ChannelTasks.get(Integer.parseInt(arguments[1])).equals(channel)) {
                        tasks.remove(arguments[1]);
                        ChannelTasks.remove(arguments[1]);
                    } else {
                        sendMessage(channel, "This is not a valid id.");
                    }
                }
            }


        }//This brace closes the cmd loop


        //--------------------------------------------------------------------------------------------------------------

        //Quack like a duck
        if (message.contains("quack")) {
            sendMessage(channel, "Quack, Quack.");
            sendMessage(channel, "I'm a duck!");
        }

        //--------------------------------------------------------------------------------------------------------------

        if (channel.equalsIgnoreCase(MessageChannel)) {
            sendMessage(creator, dateFormatTime.format(date) + " " + channel + " <" + sender + "> " + message);
        }
    }


    //Chat relay from PM
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        if (sender.equalsIgnoreCase(creator)) {
            if (message.startsWith("#")) {
                MessageChannel = message;
            } else {
                sendMessage(MessageChannel, message);
            }
        } else {
            sendMessage(sender, "I am not authorised to talk to you");
        }
    }

    protected void onJoin(String channel, String sender, String login, String hostname) {

        new File(DataPath + channel).mkdirs();
        new File(DataPath).mkdirs();

        //--------------------------------------------------------------------------------------------------------------

        if (sender.equalsIgnoreCase(creator)) {
            sendMessage(channel, "Creator!");
        }
    }
}