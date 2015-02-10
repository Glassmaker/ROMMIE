package rommie;

import com.google.gson.Gson;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import rommie.modules.GoogleResults.GoogleResults;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Rommie extends PircBot {

    //Load in properties from config file
    private static final String TIMEOUT_DIR = RommieMain.config.getProperty("TIMEOUT_DIR");
    private static final String TIMEOUT_FILE = RommieMain.config.getProperty("TIMEOUT_FILE");
    private static final String BOT_NAME = RommieMain.config.getProperty("BOT_NAME");
    private static final String CREATOR = RommieMain.config.getProperty("CREATOR");
    private static final String DATA_PATH = RommieMain.config.getProperty("DATA_PATH");

    //Locally set and changed variables
    private static String CMD_PREFIX = ">";
    String MESSAGE_CHANNEL = "#Rommie";
    DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    Date DATE = new Date();
    int FOX_COUNT = 0;

    public Rommie(){
        this.setName(BOT_NAME);
        this.setMessageDelay(1000);
    }

    //What happens when someone sends a message in a channel
    public void onMessage(String channel, String sender, String login, String hostname, String message) {

        //Goes and sees if we have a command that matches the message structure
        try {
            commandCheck(channel, sender, login, hostname, message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //--------------------------------------------------------------------------------------------------------------

        //Quack like a duck
        if (message.contains("quack") | message.contains("Quack")) {
            sendMessage(channel, "Quack, Quack.");
            sendMessage(channel, "I'm a duck!");
        }

        //--------------------------------------------------------------------------------------------------------------

        //Send message to CREATOR from specified channel
        if (channel.equalsIgnoreCase(MESSAGE_CHANNEL)) {
            sendMessage(CREATOR, dateFormatTime.format(DATE) + " " + channel + " <" + sender + "> " + message);
        }
    }

    //List of valid commands
    //Called from onMessage
    public void commandCheck(String channel, String sender, String login, String hostname, String message) throws IOException {
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
                log("Time command issued");
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
                log("Tell command issued");
            }

            //----------------------------------------------------------------------------------------------------------

            //Command to join a channel
            if (command.equalsIgnoreCase("join") & sender.equalsIgnoreCase(CREATOR)) {
                if (arguments.length < 1) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "join <ChannelName>");
                } else {
                    joinChannel(arguments[1]);
                    sendMessage(channel, "Joined " + arguments[1]);
                }
                log("Join command issued");
            }

            //----------------------------------------------------------------------------------------------------------

            //Command to part a channel
            if (command.equalsIgnoreCase("part") & sender.equalsIgnoreCase(CREATOR)) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "part");
                } else {
                    sendMessage(channel, "Leaving channel as commanded");
                    partChannel(channel);
                }
                log("Part command issued");
            }

            //----------------------------------------------------------------------------------------------------------

            //Command to quit IRC
            if (command.equalsIgnoreCase("disconnect") & sender.equalsIgnoreCase(CREATOR)) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "disconnect");
                } else {
                    sendMessage(channel, "Disconnecting from IRC");
                    System.exit(0);

                }
                log("Disconnect command issued");
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
                log("Fox throwing command issued");
            }

            //----------------------------------------------------------------------------------------------------------

            //Flip a table
            if (command.equalsIgnoreCase("table")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "table");
                } else {
                    sendMessage(channel, "(╯°□°）╯︵ ┻━┻");
                }
                log("Table flip command issued");
            }

            //----------------------------------------------------------------------------------------------------------

            //Kicks a user
            if (command.equalsIgnoreCase("kick") & sender.equalsIgnoreCase(CREATOR)) {
                if (arguments.length < 2 | arguments.length > 2) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "kick <User>");
                } else {
                    kick(channel, arguments[1]);
                    sendMessage(channel, arguments[1] + " was kicked by " + sender);
                }
                log("Kick command issued");
            }

            //----------------------------------------------------------------------------------------------------------

            //Bans a user
            if (command.equalsIgnoreCase("ban") & sender.equalsIgnoreCase(CREATOR)) {
                if (arguments.length < 2 | arguments.length > 2) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "ban <User>");
                } else {
                    ban(channel, arguments[1]);
                    sendMessage(channel, arguments[1] + " was banned from " + channel + " by " + sender);
                }
                log("Ban command issued");
            }

            //----------------------------------------------------------------------------------------------------------

            //Sets the command prefix
            if (command.equalsIgnoreCase("prefix") & sender.equalsIgnoreCase(CREATOR)) {
                if (arguments.length < 2 | arguments.length > 2) {
                    sendMessage(channel, "Creator only command. Usage : " + CMD_PREFIX + "prefix <character>");
                } else {
                    CMD_PREFIX = arguments[1];
                    String[] channels = getChannels();
                    for(int x=0; x < channels.length; x++) {
                        sendMessage(channel, "My command prefix has been changed to " + CMD_PREFIX);
                    }
                }
                log("Command prefix change issued");
            }

            //----------------------------------------------------------------------------------------------------------

            //Flail
            if (command.equalsIgnoreCase("flail")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "flail");
                } else {
                    sendAction(channel, "flails");
                }
                log("Flail command issued");
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
                log("Timeout command issued");
            }
            //----------------------------------------------------------------------------------------------------------

            //Timeout link
            if (command.equalsIgnoreCase("timeoutlink")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "timeoutlink");
                }
                else{
                    sendMessage(channel, "https://www.dropbox.com/s/ix1biwtoip75uy4/Timeouts.txt?dl=0");
                }
                log("Timeout link command issued");
            }

            //----------------------------------------------------------------------------------------------------------

            //Changes the topic of a Channel
            //This is really hacky and needs rewritten
            if (command.equalsIgnoreCase("topic") && sender.equals(CREATOR)) {
                if (arguments.length < 2) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "topic <Topic>");
                } else {
                    int starting_point = message.indexOf(arguments[1]) + 1;
                    String message_to_send = message.substring(starting_point);

                    setTopic(channel, arguments[1] + " " + message_to_send);
                }
                log("Topic command issued");
            }

            //----------------------------------------------------------------------------------------------------------

            if (command.equalsIgnoreCase("google") && sender.equals(CREATOR)) {
                if (arguments.length < 2) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "google <Search Phrase>");
                } else {
                    String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
                    int starting_point = message.indexOf(arguments[1]);
                    String query = message.substring(starting_point);
                    String charset = "UTF-8";

                    URL url = new URL(address + URLEncoder.encode(query, charset));
                    Reader reader = new InputStreamReader(url.openStream(), charset);
                    GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);

                    // Show title and URL of each results
                    String ResultTitle =  results.getResponseData().getResults().get(0).getTitle();
                    String ResultURL = results.getResponseData().getResults().get(0).getUrl();
                    String ResultContent =  results.getResponseData().getResults().get(0).getContent();

                    //TODO Returns results with tags (need to remove)
                    String ResultOutput = sender + " : " + ResultURL + " -- " + ResultTitle + " : " + ResultContent;

                    sendMessage(channel, ResultOutput);
                }
                log("Google Search command issued");
            }



            //--------------------------------------------------------------------------------------------------------------

        }//This brace closes the cmd loop
    }

    //Log timeouts to file
    //Called when a timeout is logged
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
    //Called when the bot gets a PM
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        if (sender.equalsIgnoreCase(CREATOR)) {
            if (message.startsWith("#")) {
                MESSAGE_CHANNEL = message;
            } else {
                sendMessage(MESSAGE_CHANNEL, message);
            }
        } else {
            sendMessage(sender, "I am not authorised to talk to you");
            sendMessage(CREATOR, "PM form "+ sender + " - " +message);
        }
    }

    //What to do when joining a channel
    protected void onJoin(String channel, String sender, String login, String hostname) {

        //Not sure what to do with these yet
        new File(DATA_PATH + channel).mkdirs();
        new File(DATA_PATH).mkdirs();
        sendMessage(channel, "The current command prefix is " + CMD_PREFIX);
    }

    //Go mad with power on OP
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient){
        if(recipient.equals(BOT_NAME)){
            sendAction(channel, "goes mad with power");
        }
    }

    //Join a channel on invite by CREATOR
    public void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel){
        if(sourceNick.equals(CREATOR) && targetNick.equals(BOT_NAME)){
            joinChannel(channel);
        }
        else{
            sendMessage(sourceNick, "I'm sorry but I can't do that.");
        }
    }

    //What is done when we get kicked
    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason){
        if(recipientNick.equalsIgnoreCase(BOT_NAME)) {
            sendMessage(CREATOR, "I was kicked by " + kickerNick + " from " + channel + " for: " + Colors.PURPLE + reason);
            joinChannel(channel);
        }
    }

    //Try to reconnect when we disconnect
    protected void onDisconnect(){
        try {
            reconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }
    }
}