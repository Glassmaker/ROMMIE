package rommie;

import static rommie.modules.RandomNumber.RandomNumber.generateRandom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import rommie.commands.CommandBase;
import rommie.commands.CommandRequest;
import rommie.modules.Logger.Logging;

public class Rommie extends PircBot {

    //Load in properties from config file
    private final String TIMEOUT_DIR = RommieMain.config.getProperty("TIMEOUT_DIR");
    private final String TIMEOUT_FILE = RommieMain.config.getProperty("TIMEOUT_FILE");
    private final String BOT_NAME = RommieMain.config.getProperty("BOT_NAME");
    private final String POTATO = RommieMain.config.getProperty("DISCONNECT_PASSWORD");
    private static ArrayList<String> CREATOR = new ArrayList<>(Arrays.asList(RommieMain.config.get("CREATOR").toString().split(",")));
    //private static String CREATOR = RommieMain.config.getProperty("CREATOR");
    public static String CMD_PREFIX = RommieMain.config.getProperty("CMD_PREFIX");
    private String MESSAGE_CHANNEL = RommieMain.config.getProperty("MESSAGE_CHANNEL");
    public static final String DATA_PATH = RommieMain.config.getProperty("DATA_PATH");

    private final DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    private final Date DATE = new Date();
    private boolean USER_EXISTS = false;
    private boolean USER_ACTIVE = false;
    private final HashMap<String, User[]> channelUserList = new HashMap<>();





    //Config variables
    private boolean STATE_PREFIX = false;
    private boolean GREETING = true;
    private boolean FOX_MESSAGE = true;
    private boolean POTATO_MESSAGE = true;
    private int NEXT_TIMER = generateRandom(1440);

    //Random fox array
    private static final String[] Fox = new String[] {"https://i.imgur.com/WWI5fx6.jpg",
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

    /** Saves the currently pending command requests, to be handled next tick.
     */
    public List<CommandRequest> requestedCommands = new ArrayList<CommandRequest>();
    /** The list of commands Rommie can perform
     */
    public List<CommandBase> commands = new ArrayList<CommandBase>();



    //Main Rommie method
    public Rommie(){
        this.setName(BOT_NAME);
        //this.setMessageDelay(1000);


        this.setupCommands();

        //Timer task
        TimerTask tt = new TimerTask()
        {
            public void run()
            {
                //TODO Do something when timer is complete
                sendMessage("#StoneWaves", "The next event has been scheduled for " + NEXT_TIMER + " minutes form now.");
                //Picks a random minute delay up to a day
                NEXT_TIMER = generateRandom(1440);
            }
        };
        //Run the timer (<timer task>, <sec>*60000, <sec>*60000)
        //<sec>*60000 = amount of minutes till running again
        new Timer().schedule(tt, NEXT_TIMER*60000, NEXT_TIMER*60000);

    }

    public void setupCommands()
    {
        this.commands.clear();

        File commandDir = new File("src/main/java/rommie/commands");
        if(commandDir.exists()) {
            for(String listed : commandDir.list())
                if(listed.endsWith(".java")) {

                    String className = listed.substring(0,listed.lastIndexOf(".java"));
                    try {

                        Class<?> commandClass = Class.forName("rommie.commands."+className);

                        boolean hasEmptyConstructor = false;
                        for(Constructor<?> constructor : commandClass.getConstructors())
                            if(constructor.getParameterTypes().length<1)
                                hasEmptyConstructor = true;

                        if(commandClass!=null && !Modifier.isAbstract(commandClass.getModifiers()) && CommandBase.class.isAssignableFrom(commandClass) && hasEmptyConstructor) {
                            this.commands.add((CommandBase) commandClass.newInstance());
                        }

                    }catch(Exception e){
                        System.out.println("Caught exception loading command "+className);
                        System.out.println(e.getStackTrace());
                        for(int i = 0; i> CREATOR.size(); i++)
                            sendMessage(CREATOR.get(i), "Caught exception loading command "+className);
                    }


                }
        }

        //		this.commands.add(new CommandJoin());
        //		this.commands.add(new CommandPart());
        //		this.commands.add(new CommandHelp());
        //		this.commands.add(new CommandReloadCommands());
        //		this.commands.add(new CommandFox());
        //		this.commands.add(new CommandBoop());
    }

    //What happens when someone sends a message in a channel
    public void onMessage(String channel, String sender, String login, String hostname, String message) {

        //Goes and sees if we have a command that matches the message structure
        try {
            commandCheck(channel, sender, message);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        Logging.logMessage(channel, message, sender);

        //--------------------------------------------------------------------------------------------------------------

        //Toggle configs
        config(channel, sender, message);

        //--------------------------------------------------------------------------------------------------------------

        //Random fox messages
        generalMessage(channel, sender, message);

        //--------------------------------------------------------------------------------------------------------------

        //Send message to CREATOR 0 (StoneWaves) from specified channel
        if (channel.equalsIgnoreCase(MESSAGE_CHANNEL)) {
            //Creator 0 should be StoneWaves
            sendMessage(CREATOR.get(0), dateFormatTime.format(DATE) + " " + channel + " <" + sender + "> " + message + "\n");
        }
    }

    protected void onAction(String sender, String login, String hostname, String target, String action) {
        //TODO Find out what channel the action was sent from
        //Logging.logMessage(channel, action, sender);
    }

    //Generates a user list and stores it in a hash map
    @Override
    protected void onUserList(String channel, User[] users) {
        channelUserList.put(channel, users);
    }

    //These are run randomly based on a random number generator
    void generalMessage(String channel, String sender, String message){

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
    void config(String channel, String sender, String message){
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
    void commandCheck(String channel, String sender, String message) throws IOException, InterruptedException {
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
            for(CommandBase cc : this.commands)
                if(cc.matches(command)) {

                    User user = null;
                    for(User u : this.getUsers(channel))
                        if(u.getNick().equalsIgnoreCase(sender))
                            user = u;
                    CommandRequest request = new CommandRequest(cc, user, channel, message);
                    int level = cc.getCommandLevel();
                    if(level==0)
                        this.sendMessage("NickServ", "info " + sender);
                    else if(level==1)
                        request.status = user.isOp() ? CommandRequest.RequestStatus.APPROVED : CommandRequest.RequestStatus.DENIED;
                    else if(level==2)
                        request.status = (user.isOp()||user.hasVoice()) ? CommandRequest.RequestStatus.APPROVED : CommandRequest.RequestStatus.DENIED;
                    else if(level>=3)
                        request.status = CommandRequest.RequestStatus.APPROVED;
                    this.requestedCommands.add(request);
                }

            performPendingCommands();

            //TODO Transform all this into Commands
			/*


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
			 */
        }//This brace closes the cmd loop

    }

    void performPendingCommands() throws InterruptedException, IOException {
        Iterator<CommandRequest> iterator = this.requestedCommands.iterator();
        while(iterator.hasNext()){
            CommandRequest request = iterator.next();

            switch(request.status){
                case APPROVED:
                    String[] arguments = request.message.split(" ");
                    request.command.performCommand(request.user, request.channel, arguments, request.message, this);
                    iterator.remove();
                    break;
                case DENIED:
                    this.sendMessage(request.channel, request.user.getNick()+", you have no permission to use this command");
                    iterator.remove();
                    break;
                case PENDING:
                default:
                    // Do Nothing because the request is still pending, Derp!
                    break;
            }
        }
    }

    //Used to check if a user exists when NickServ is polled
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice){
        //Tells me if a user does not exist with NickServ
        String CHECK_CHANNEL = "";
        String CHECK_USER = "";

        String unformattedMessage = Colors.removeFormattingAndColors(notice);
        String name = "";
        String account = "";

        if(notice.contains("is not registered.") && sourceNick.equalsIgnoreCase("NickServ")){
            USER_EXISTS = false;
            sendMessage(CHECK_CHANNEL, "User is not registered.");

            name = unformattedMessage.substring(0, unformattedMessage.indexOf(" is not registered."));
            //checkUser();
        }
        //This tells me if a user exists with NickServ
        else if(notice.contains("Information on") && notice.contains(CHECK_USER)){
            USER_EXISTS = true;
            sendMessage(CHECK_CHANNEL, "User is registered");

            String keyPhrase = " (account ";
            int indx = unformattedMessage.indexOf(keyPhrase);
            if(indx>0){
                name = unformattedMessage.substring("Information on ".length(), indx);
                account = unformattedMessage.substring(indx+keyPhrase.length(), unformattedMessage.length()-2);
            }
            //checkUser();
        }
        //Tells me if a user is logged in currently with NickServ
        else if(notice.contains("Last seen  : now") && sourceNick.equalsIgnoreCase("NickServ")){
            USER_ACTIVE = true;
            sendMessage(CHECK_CHANNEL, notice);
            //checkUser();
        }


        for(CommandRequest request : this.requestedCommands)
            if(request.user.getNick().equalsIgnoreCase(name))
            {
                System.out.println("Account: "+account+", Creator: "+CREATOR+", match: "+CREATOR.contains(account));
                if(CREATOR.contains(account)){
                    request.status = CommandRequest.RequestStatus.APPROVED;
                }
                else{
                    request.status = CommandRequest.RequestStatus.DENIED;
                }
            }
        try {
            performPendingCommands();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Log timeouts to file
    //Called when a timeout is logged
    public void saveTimeout(User sender, Date date) {
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
        Logging.logMessage(sender, message, sender);

        //Command to quit IRC
        String[] arguments = message.split(" ");

        //--------------------------------------------------------------------------------------------------------------

        //Chat relay for PM's
        if (CREATOR.contains(sender)) {
            //Channel switch
            if (message.startsWith("#")) {
                MESSAGE_CHANNEL = message;
            }
            //Kill command
            else if (message.startsWith(CMD_PREFIX + "disconnect") && CREATOR.contains(sender) && arguments[1].equalsIgnoreCase(POTATO)){
                String[] channels = getChannels();
                for (String channel : channels) {
                    sendMessage(channel, "Quitting IRC ");
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
            for(int i = 0; i> CREATOR.size(); i++)
                sendMessage(CREATOR.get(i), "PM form "+ sender + " - " +message);
        }

    }

    //What to do when someone joins a channel (Bot & other users)
    protected void onJoin(String channel, String sender, String login, String hostname) {

        //Not sure what to do with these yet
        //Maybe storing files for each channel
        new File(DATA_PATH + channel).mkdirs();
        new File(DATA_PATH).mkdirs();

        //--------------------------------------------------------------------------------------------------------------

        //Log things
        Logging.logJoin(channel, " joined the channel.", sender);

        //--------------------------------------------------------------------------------------------------------------

        //State prefix when we join if true
        if(sender.equals(getNick()) && STATE_PREFIX) {
            sendMessage(channel, "The current command prefix is " + STATE_PREFIX);
        }

        //--------------------------------------------------------------------------------------------------------------

        //Send greeting when someone joins if true
        if(!sender.equals(getNick()) && GREETING) {
            sendMessage(channel, "o/ " + sender);
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
        if(CREATOR.contains(sourceNick) && targetNick.equals(getNick())){
            joinChannel(channel);
        }
        else{
            sendMessage(sourceNick, "I'm sorry but I can't do that.");
        }
    }

    //What is done when we get kicked
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason){

        Logging.logKick(channel, " was kicked from the channel.", recipientNick);

        if(recipientNick.equalsIgnoreCase(getNick())) {
            for(int i = 0; i> CREATOR.size(); i++)
                sendMessage(CREATOR.get(i), "I was kicked by " + kickerNick + " from " + channel + " for: " + Colors.PURPLE + reason);
            joinChannel(channel);
        }
    }

    //Makes sure creator name is valid
    //Start with StoneWaves
    protected void onNickChange(String oldNick, String login, String hostname, String newNick){
        //		if(oldNick.equalsIgnoreCase(CREATOR)){
        //			CREATOR = newNick;
        //
        //		}
    }

    //What happens when someone parts a channel
    protected void onPart(String channel, String sender, String login, String hostname){
        Logging.logPart(channel, " left the channel.", sender);
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