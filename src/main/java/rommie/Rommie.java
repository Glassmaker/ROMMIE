package rommie;

import com.google.gson.Gson;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import rommie.modules.GoogleResults.GoogleResults;
import rommie.modules.Logger.Logging;
import rommie.modules.Quote.Quote;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static rommie.modules.RandomNumber.RandomNumber.generateRandom;

public class Rommie extends PircBot {

    //Load in properties from config file
    private final String TIMEOUT_DIR = RommieMain.config.getProperty("TIMEOUT_DIR");
    private final String TIMEOUT_FILE = RommieMain.config.getProperty("TIMEOUT_FILE");
    private final String BOT_NAME = RommieMain.config.getProperty("BOT_NAME");
    private final String POTATO = RommieMain.config.getProperty("DISCONNECT_PASSWORD");

    private static String CREATOR = RommieMain.config.getProperty("CREATOR");
    private static String CMD_PREFIX = RommieMain.config.getProperty("CMD_PREFIX");
    String MESSAGE_CHANNEL = RommieMain.config.getProperty("MESSAGE_CHANNEL");
    public static final String DATA_PATH = RommieMain.config.getProperty("DATA_PATH");


    //Locally set and changed variables
    DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    Date DATE = new Date();
    int FOX_COUNT = 0;
    String CHECK_USER = "";
    boolean USER_EXISTS = false;
    boolean USER_ACTIVE = false;
    String CHECK_CHANNEL = "";
    private HashMap<String, User[]> channelUserList = new HashMap<>();


    //Random fox array
    public static String[] Fox = new String[] {"https://i.imgur.com/WWI5fx6.jpg",
                                               "https://i.imgur.com/JWhMlIe.gif",
                                               "https://i.imgur.com/7Cqvhxq.jpg",
                                               "https://i.imgur.com/GfP3OdP.gif",
                                               "https://i.imgur.com/R2tMDDl.jpg",
                                               "https://i.imgur.com/mAplyY5.jpg",
                                               "https://i.imgur.com/OSbysNl.gif",
                                               "https://i.imgur.com/uE3NuYu.jpg",
                                               "https://imgur.com/gallery/oczHumo",
                                               "https://i.imgur.com/D1kfA1Z.jpg",
                                               "https://i.imgur.com/sYFvsPU.jpg",
                                               "http://imgur.com/gallery/SKm5U",
                                               "https://i.imgur.com/xwdd4.jpg",
                                               "http://imgur.com/a/vDII7",
                                               "http://www.natursidan.se/wp-content/uploads/2013/04/K2_Hermann_Hirsch_Abendidylle.jpg"
    };


    //Config variables
    private boolean STATE_PREFIX = false;
    private boolean GREETING = true;
    private boolean FOX_MESSAGE = true;
    private boolean POTATO_MESSAGE = true;

    int timerCount = 0;
    //Main Rommie method
    public Rommie(){
        this.setName(BOT_NAME);
        this.setMessageDelay(1000);

        //Timer task
        TimerTask tt = new TimerTask()
        {
            public void run()
            {
               //TODO Do something when timer is complete

            }
        };
        //Run the timer (<timer task>, <sec>*1000, <sec>*1000)
        new Timer().schedule(tt, 10*1000, 10*1000);


    }

    //What happens when someone sends a message in a channel
    public void onMessage(String channel, String sender, String login, String hostname, String message) {

        //Goes and sees if we have a command that matches the message structure
        try {
            commandCheck(channel, sender, message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logging.log(channel, message, sender);

        //--------------------------------------------------------------------------------------------------------------

        //Toggle configs
        config(channel, sender, message);

        //--------------------------------------------------------------------------------------------------------------

        //Random fox messages
        generalMessage(channel, sender, message);

        //--------------------------------------------------------------------------------------------------------------

        //Send message to CREATOR from specified channel
        if (channel.equalsIgnoreCase(MESSAGE_CHANNEL)) {
            sendMessage(CREATOR, dateFormatTime.format(DATE) + " " + channel + " <" + sender + "> " + message + "\n");
        }
    }

    //Generates a user list and stores it in a hash map
    @Override
    protected void onUserList(String channel, User[] users) {
        channelUserList.put(channel, users);
    }

    //These are run randomly based on a random number generator
    public void generalMessage(String channel, String sender, String message){

        //Quack like a duck
        if (message.contains("quack") | message.contains("Quack") && !message.contains(CMD_PREFIX) && generateRandom(10) == 5) {
            sendMessage(channel, "Quack, Quack.");
            sendMessage(channel, "I'm a duck!");
        }

        //--------------------------------------------------------------------------------------------------------------

        //Random fox image
        if (message.contains("fox") && !message.contains(CMD_PREFIX) && generateRandom(10) == 5) {
                sendMessage(channel, Fox[generateRandom(Rommie.Fox.length)]);
        }

        //--------------------------------------------------------------------------------------------------------------

        //I'm a potato!
        if (message.contains("potato")  && !message.contains(CMD_PREFIX) && generateRandom(10) == 5){
            sendMessage(channel, "I'M A POTATO!");
        }

    }

    //Commands to change configs
    //Called from onMessage
    public void config(String channel, String sender, String message){
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

            //Turns on / off prefix stating when joining
            if (command.equalsIgnoreCase("toggleprefix")) {
                STATE_PREFIX = !STATE_PREFIX;
                sendMessage(channel, "Stating prefix on join has been toggled " + STATE_PREFIX);
            }

            //----------------------------------------------------------------------------------------------------------

            //Turns on / off greetings
            if (command.equalsIgnoreCase("togglegreeting")) {
                GREETING = !GREETING;
                sendMessage(channel, "Greetings have been toggled to " + GREETING);
            }

            //----------------------------------------------------------------------------------------------------------

            //Turns on / off Random foxes
            if (command.equalsIgnoreCase("togglefox")) {
                FOX_MESSAGE = !FOX_MESSAGE;
                sendMessage(channel, "Random foxes have been toggled to " + FOX_MESSAGE);
            }

            //----------------------------------------------------------------------------------------------------------

            //Turns on / off Random potatoes
            if (command.equalsIgnoreCase("togglepotato")) {
                POTATO_MESSAGE = !POTATO_MESSAGE;
                sendMessage(channel, "Random potatoes have been toggled to " + POTATO_MESSAGE);
            }
        }
    }

    //All commands are declared and run from here
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
            }

            //----------------------------------------------------------------------------------------------------------

            //Throw a fox
            //Called by sending "fox" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("fox")) {
                if (arguments.length < 2) {
                    sendAction(channel, "throws foxes at everyone");
                    FOX_COUNT = FOX_COUNT + 1;
                }
                else{
                    int starting_point = message.indexOf(arguments[1]) + arguments[1].length() + 1;
                    String message_to_send = message.substring(starting_point);
                    sendAction(channel, "throws a fox at " + arguments[1] + " " + message_to_send);
                    FOX_COUNT = FOX_COUNT + 1;
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Boops a user
            //Called by sending "boop" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("boop")) {
                if (arguments.length < 2) {
                    sendAction(channel, "boops everyone");
                    FOX_COUNT = FOX_COUNT + 1;
                }
                else{
                    int starting_point = message.indexOf(arguments[1]) + arguments[1].length() + 1;
                    String message_to_send = message.substring(starting_point);
                    sendAction(channel, "boops " + arguments[1] + message_to_send);
                    FOX_COUNT = FOX_COUNT + 1;
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Flip a table
            //Called by sending "table" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("table")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "table");
                } else {
                    sendMessage(channel, "(╯°□°）╯︵ ┻━┻");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Sets the command prefix
            //Called by sending "prefix <new prefix>" with the CMD_PREFIX appended
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
            }

            //----------------------------------------------------------------------------------------------------------

            //Causes Rommie to flail
            //Called by sending "flail" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("flail")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "flail");
                } else {
                    sendAction(channel, "flails");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Log timeouts for the FoxStone server
            //Called by sending "timeout" with the CMD_PREFIX appended
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

            //Gets a link to the Timeouts file
            //Called by sending "timeoutlink" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("timeoutlink")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "timeoutlink");
                }
                else{
                    sendMessage(channel, "https://www.dropbox.com/s/ix1biwtoip75uy4/Timeouts.txt?dl=0");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Changes the topic of a Channel
            ///Called by sending "topic <new topic>" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("topic") && sender.equals(CREATOR)) {
                if (arguments.length < 2) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "topic <Topic>");
                } else {
                    int starting_point = message.indexOf(arguments[1]) + 1;
                    String message_to_send = message.substring(starting_point);

                    setTopic(channel, arguments[1] + "  " + message_to_send);
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Allows a user to google a string from IEC
            //Called by sending "google <query>" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("google")){
                if (arguments.length < 2) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "google <Query>");
                } else {
                    String address = "http://ajax.googleapis.com/ajax/services/search/web?start=0&rsz=small&v=1.0&q=";

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

                        String ResultOutput = sender + " : " + ResultURL + " -- " + ResultTitle + " : " + ResultContent;

                        // Remove tags on the returned results
                        ResultOutput = ResultOutput.toString().replaceAll("</b>", "");
                        ResultOutput = ResultOutput.toString().replaceAll("<b>", "");

                        sendMessage(channel, ResultOutput);
                    }
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Gives a user the GitHub link
            //Called by sending "source" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("source")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "source");
                } else {
                    sendMessage(channel, "https://github.com/StoneWaves/ROMMIE");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Sends a photo of a blue footed boobie at a specified user
            //Called by sending "boob" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("boob")) {
                if (arguments.length < 2) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "boob + <target>");
                    FOX_COUNT = FOX_COUNT + 1;
                }
                else{
                    int starting_point = message.indexOf(arguments[1]) + 1;
                    String message_to_send = message.substring(starting_point);
                    setTopic(channel, arguments[1] + "  " + message_to_send + " : (.)(.) http://cdn.bleedingcool.net/wp-content/uploads/2013/09/blue-footed-booby.jpg");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Fetches the issues url
            //Called by sending "bug" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("bug")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "bug");
                } else {
                    sendMessage(channel, sender + " - You can report bugs here : https://github.com/StoneWaves/ROMMIE/issues");
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Fetches a random quote form I <3 quotes
            //Called by sending "quote" with the CMD_PREFIX appended
            if (command.equalsIgnoreCase("quote")) {
                if (arguments.length > 1) {
                    sendMessage(channel, "Usage : " + CMD_PREFIX + "quote");
                } else {
                    sendMessage(channel, Quote.quote());
                }
            }

        }//This brace closes the cmd loop
    }

    //Used to check if a user exists when NickServ is polled
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice){
        //Tells me if a user does not exist with NickServ
        if(notice.contains("is not registered.") && sourceNick.equalsIgnoreCase("NickServ")){
            USER_EXISTS = false;
            sendMessage(CHECK_CHANNEL, "User is not registered.");
            //checkUser();
        }
        //This tells me if a user exists with NickServ
        else if(notice.contains("Information on") && notice.contains(CHECK_USER)){
            USER_EXISTS = true;
            sendMessage(CHECK_CHANNEL, "User is registered");
            //checkUser();
        }
        //Tells me if a user is logged in currently with NickServ
        else if(notice.contains("Last seen  : now") && sourceNick.equalsIgnoreCase("NickServ")){
            USER_ACTIVE = true;
            sendMessage(CHECK_CHANNEL, notice);
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

        //Log all the things
        new File(DATA_PATH + sender).mkdirs();
        new File(DATA_PATH).mkdirs();
        Logging.log(sender, message, sender);

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

        //What if you arent the creator
        else {
            sendMessage(sender, "I am not authorised to talk to you");
            sendMessage(CREATOR, "PM form "+ sender + " - " +message);
        }

    }

    //What to do when someone joins a channel (Bot & other users)
    protected void onJoin(String channel, String sender, String login, String hostname) {

        //Not sure what to do with these yet
        //Maybe storing files for each channel
        new File(DATA_PATH + channel).mkdirs();
        new File(DATA_PATH).mkdirs();
        Logging.log(channel, " joined the channel.", sender);

        //--------------------------------------------------------------------------------------------------------------

        //State prefix when we join if true
        if(sender.equals(getNick()) && STATE_PREFIX == true) {
            sendMessage(channel, "The current command prefix is " + STATE_PREFIX);
        }

        //--------------------------------------------------------------------------------------------------------------

        //Send greeting when someone joins if true
        if(sender.equals(getNick()) && GREETING == true && !sender.equals(BOT_NAME)) {
            sendMessage(channel, "o/");
        }

        //--------------------------------------------------------------------------------------------------------------

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

        Logging.log(channel, " was kicked from the channel.", recipientNick);

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

    //What happens when someone parts a channel
    protected void onPart(String channel, String sender, String login, String hostname){
        Logging.log(channel, " left the channel.", sender);
        }

    //Try to reconnect when we disconnect
    protected void onDisconnect(){
        while(!isConnected()) {
            try {
                Thread.currentThread().sleep(15000); // sleeping for 15 seconds
                reconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}