package rommie;

import com.google.gson.Gson;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import rommie.modules.GoogleResults.GoogleResults;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rommie extends PircBot {

    //Load in properties from config file
    private static final String TIMEOUT_DIR = RommieMain.config.getProperty("TIMEOUT_DIR");
    private static final String TIMEOUT_FILE = RommieMain.config.getProperty("TIMEOUT_FILE");
    private static final String BOT_NAME = RommieMain.config.getProperty("BOT_NAME");
    private static final String DATA_PATH = RommieMain.config.getProperty("DATA_PATH");
    private static final String POTATO = RommieMain.config.getProperty("DISCONNECT_PASSWORD");

    private static String CREATOR = RommieMain.config.getProperty("CREATOR");
    private static String CMD_PREFIX = RommieMain.config.getProperty("CMD_PREFIX");
    String MESSAGE_CHANNEL = RommieMain.config.getProperty("MESSAGE_CHANNEL");


    //Locally set and changed variables
    DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    Date DATE = new Date();
    int FOX_COUNT = 0;
    String CHECK_USER = "";
    boolean USER_EXISTS = false;
    boolean USER_ACTIVE = false;


    //Main Rommie method
    public Rommie(){
        this.setName(BOT_NAME);
        this.setMessageDelay(1000);
    }

    //What happens when someone sends a message in a channel
    public void onMessage(String channel, String sender, String login, String hostname, String message) {

        //Goes and sees if we have a command that matches the message structure
        try {
            commandCheck(channel, sender, message);
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
    public void commandCheck(String channel, String sender, String message) throws IOException {
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
                    for (String ignored : channels) {
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

            if (command.equalsIgnoreCase("google")){
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


                    if(results.getResponseData().getResults().size() == 0){
                        sendMessage(channel, sender + " : No results were found for your search of " + query);
                    }
                    else {
                        // Show title and URL of each results
                        String ResultTitle = results.getResponseData().getResults().get(0).getTitle();
                        String ResultURL = results.getResponseData().getResults().get(0).getUrl();
                        String ResultContent = results.getResponseData().getResults().get(0).getContent();

                        //TODO Returns results with tags (need to remove)
                        String ResultOutput = sender + " : " + ResultURL + " -- " + ResultTitle + " : " + ResultContent;

                        sendMessage(channel, ResultOutput);
                    }
                }
                log("Google Search command issued");
            }

            //--------------------------------------------------------------------------------------------------------------

            if (message.equalsIgnoreCase("users")){
                String[] channels = getChannels();
                for (int c = 0; c < channels.length; c++) {
                    User[] users = getUsers(channels[c]);
                    for (int u = 0; u < users.length; u++) {
                        sendMessage(channel, channels[c] + " - " + String.valueOf(users[u]));
                    }
                }
            }

            //----------------------------------------------------------------------------------------------------------

            if (command.equalsIgnoreCase("check")) {
                if (arguments.length < 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "check <User>");
                } else {
                    CHECK_USER = arguments[1];
                    sendMessage("NickServ", "info " + arguments[1]);
                }
            }

        }//This brace closes the cmd loop
    }

    //Used to check if a user exists when NickServ is polled
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice){
        //Tells me if a user does not exist with NickServ
        if(notice.contains("is not registered.") && sourceNick.equalsIgnoreCase("NickServ")){
            USER_EXISTS = false;
            sendMessage("#StoneWaves", notice);
            //checkUser();
        }
        //This tells me if a user exists with NickServ
        else if(notice.contains("Information on")){
            USER_EXISTS = true;
            sendMessage("#StoneWaves", notice);
            //checkUser();
        }
        //Tells me if a user is logged in currently with NickServ
        if(notice.contains("Last seen  : now") && sourceNick.equalsIgnoreCase("NickServ")){
            USER_ACTIVE = true;
            sendMessage("#StoneWaves", notice);
            //checkUser();
        }
    }

    //Log timeouts to file
    //Called when a timeout is logged
    protected void saveTimeout( String sender, Date date ) {
        BufferedWriter myOutFile;
        try {
            //Set up the file writer
            myOutFile = new BufferedWriter( new FileWriter( TIMEOUT_DIR + TIMEOUT_FILE, true ) );
            myOutFile.write(sender + " logged a new timeout at " + date + "\n" );
            myOutFile.close();
        } catch( IOException f )
        {
            f.printStackTrace();
        }
    }

    //Called when the bot gets a PM
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {

        //Command to quit IRC
        String[] arguments = message.split(" ");

        //--------------------------------------------------------------------------------------------------------------

        //Chat relay for PM's
        if (sender.equalsIgnoreCase(CREATOR)) {
            //Channel switch
            if (message.startsWith("#")) {
                MESSAGE_CHANNEL = message;
            }
            //Kill command
            else if (message.startsWith(CMD_PREFIX + "disconnect") && sender.equalsIgnoreCase(CREATOR) && arguments[1].equalsIgnoreCase(POTATO)){
                String[] channels = getChannels();
                for (int i = 0; i < channels.length; i++) {
                    sendMessage(channels[i], "Quitting IRC ");
                }
                log("Disconnect command issued");
                disconnect();
                System.exit(0);
            }
            //Relay
            else {
                sendMessage(MESSAGE_CHANNEL, message);
            }
        }

        else {
            sendMessage(sender, "I am not authorised to talk to you");
            sendMessage(CREATOR, "PM form "+ sender + " - " +message);
        }




    }

    //What to do when joining a channel
    protected void onJoin(String channel, String sender, String login, String hostname) {

        //Not sure what to do with these yet
        new File(DATA_PATH + channel).mkdirs();
        new File(DATA_PATH).mkdirs();

        if(sender.equals(getNick())) {
            //sendMessage(channel, "The current command prefix is " + CMD_PREFIX);
        }
    }

    //Go mad with power on OP
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient){
        if(recipient.equals(getNick())){
            //sendAction(channel, "goes mad with power");
        }
    }

    //Join a channel on invite by CREATOR
    protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel){
        if(sourceNick.equals(CREATOR) && targetNick.equals(getNick())){
            joinChannel(channel);
        }
        else{
            sendMessage(sourceNick, "I'm sorry but I can't do that.");
        }
    }

    //What is done when we get kicked
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason){
        if(recipientNick.equalsIgnoreCase(getNick())) {
            sendMessage(CREATOR, "I was kicked by " + kickerNick + " from " + channel + " for: " + Colors.PURPLE + reason);
            joinChannel(channel);
        }
    }

    //Makes sure creator name is valid
    //Start with StoneWaves
    protected void onNickChange(String oldNick, String login, String hostname, String newNick){
        if(oldNick.equalsIgnoreCase(CREATOR)){
            CREATOR = newNick;
        }
    }

    //Try to reconnect when we disconnect
    protected void onDisconnect(){
        try {
            reconnect();
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
    }
}