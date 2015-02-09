package rommie;

import org.jibble.pircbot.PircBot;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Rommie extends PircBot {
    public static final String TIMEOUT_DIR = "C:\\Users\\christophera\\Dropbox\\FoxStone Timeouts\\";
    public static final String TIMEOUT_FILE = "Timeouts.txt";
    public static final String BOT_NAME = "Rommie";

    private static String CMD_PREFIX = ">";
    private String creator = "StoneWaves";
    String MESSAGE_CHANNEL = "#Rommie";
    private static final String DATA_PATH = ".\\Files\\";
    DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    int FOX_COUNT = 0;

    public Rommie() throws FileNotFoundException {
        this.setName(BOT_NAME);

        //file writwer

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
                    // sendMessage(channel, "Disconnecting from IRC");
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
                    FOX_COUNT = FOX_COUNT + 1;
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Lists all commands
            if (command.equalsIgnoreCase("count")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "count");
                } else {
                   sendMessage(channel, "A total of " + FOX_COUNT + " foxes have been thrown.");
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

            //Flail
            if (command.equalsIgnoreCase("flail")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "flail");
                } else {
                    sendAction(channel, "flails");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Log timeouts
            if (command.equalsIgnoreCase("timeout")) {
                    if (arguments.length > 1) {
                        sendMessage(channel, "Usage : " + CMD_PREFIX + "timeout");
                    }
                    else{
                        saveTimeout(sender, new Date());
                        sendMessage(channel, "The date and time has been noted.");
                    }
            }
            //----------------------------------------------------------------------------------------------------------
        }//This brace closes the cmd loop


        //--------------------------------------------------------------------------------------------------------------

        //Quack like a duck
        if (message.contains("quack") | message.contains("Quack")) {
            sendMessage(channel, "Quack, Quack.");
            sendMessage(channel, "I'm a duck!");
        }

        //--------------------------------------------------------------------------------------------------------------

        if (channel.equalsIgnoreCase(MESSAGE_CHANNEL)) {
            sendMessage(creator, dateFormatTime.format(date) + " " + channel + " <" + sender + "> " + message);
        }
    }

    private void saveTimeout( String sender, Date date ) {
        BufferedWriter myOutFile;
        try {
            //Set up the file writer
            myOutFile = new BufferedWriter( new FileWriter( TIMEOUT_DIR + TIMEOUT_FILE, true ) );
            myOutFile.write(sender + " logged a new timeout at " + date + "\n" );
            myOutFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch( IOException f )
        {
            f.printStackTrace();
        }
    }

    //Chat relay from PM
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        if (sender.equalsIgnoreCase(creator)) {
            if (message.startsWith("#")) {
                MESSAGE_CHANNEL = message;
            } else {
                sendMessage(MESSAGE_CHANNEL, message);
            }
        } else {
            sendMessage(sender, "I am not authorised to talk to you");
            sendMessage(creator, "PM form "+ sender + " - " +message);
        }
    }

    protected void onJoin(String channel, String sender, String login, String hostname) {

        new File(DATA_PATH + channel).mkdirs();
        new File(DATA_PATH).mkdirs();
    }
}