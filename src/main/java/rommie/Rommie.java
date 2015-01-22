package rommie;

import com.sun.xml.internal.ws.resources.SenderMessages;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.Channel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Rommie extends PircBot {

    private static final String DataPath = ".\\Data\\";
    private HashMap<String, User[]> channelUserList = new HashMap<>();
    private static final String CMD_PREFIX= ">";

    private ArrayList Channels = new ArrayList();

    DateFormat dateFormatDay = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();


    public Rommie() {
        this.setName("Rommie");
    }

    //What happens when the bot receives a message with the cmd prefix
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith(CMD_PREFIX)) {
            message = message.substring(CMD_PREFIX.length()); //Strips command prefix

            //----------------------------------------------------------------------------------------------------------

            //Part the channel when commanded (SW only)
            if (message.equalsIgnoreCase("part") & sender.equals("StoneWaves")) {
                partChannel(channel, "As you command creator.");
            }
            else if(message.equalsIgnoreCase("part")){
                sendMessage(channel, "You do not command me!");
            }

            //Admin stuff (Currently unused)
            String[] AdminInput = {""};
            String[] AdminOutput = {""};

            for (int index = 0; index < AdminInput.length; index++) {
                if (message.equalsIgnoreCase(AdminInput[index]) & sender.equals("StoneWaves")) {

                    if (message.equalsIgnoreCase(AdminInput[index])) {

                        String out = AdminOutput[index];
                        //sendAction(channel, out);
                        //sendMessage(channel, out);
                    }
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Action replies
            String[] inputAction = {"fox"};
            String[] outputAction = {"throws foxes at everyone"};

            for (int index = 0; index < inputAction.length; index++) {
                if (message.equalsIgnoreCase(inputAction[index])) {

                    String out = outputAction[index];
                    out = out.replace("$sender", sender);
                    sendAction(channel, out);
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //Message replies
            String[] inputMessage = {
                    "time",
                    "Rommie",
                    "Duck"};

            String[] outputMessage = {
                    "$sender : The time is now $time",
                    "I'm a bot build by StoneWaves and based on the pircbot framework",
                    "Quack, Quack"};

            for (int index = 0; index < inputMessage.length; index++) {
                if (message.equalsIgnoreCase(inputMessage[index])) {

                    String time = new java.util.Date().toString();
                    String out = outputMessage[index];

                    out = out.replace("$sender", sender);
                    out = out.replace("$time", time);
                    sendMessage(channel, out);
                }
            }

            //----------------------------------------------------------------------------------------------------------

            //List all channels the bot is in
            if (message.equalsIgnoreCase("list")) {
                sendMessage(sender, "I'm in the following channels: ");
                for(String s : getChannels())
                    sendMessage(sender, s);
            }

            //----------------------------------------------------------------------------------------------------------

            //FC notice SFW
            if(message.equalsIgnoreCase("sfw") & channel.equalsIgnoreCase("#FetishCraft")){
                sendNotice(channel, "Atomic kids are around! Lets go SFW");
            }

            //----------------------------------------------------------------------------------------------------------

            //FC notice NSFW
            if(message.equalsIgnoreCase("nsfw") & channel.equalsIgnoreCase("#FetishCraft")){
                sendNotice(channel, "Atomic kids are gone, feel free to be NSFW");
            }

            //----------------------------------------------------------------------------------------------------------




        }

        //Testing line
        //Message me when a message appears in chat
        sendMessage("StoneWaves",  dateFormatTime.format(date) + " " + channel + " <" + sender + "> " + message);

    }

    @Override
    protected void onUserList(String channel, User[] users) {
        channelUserList.put(channel, users);
    }

    //What happens when we join a channel
    @Override
    protected void onJoin(String channel, String sender, String login, String hostname){
        if(sender.equalsIgnoreCase("rommie")){
            Channels.add(channel);
            File dir = new File(DataPath + channel);
            new File(String.valueOf(dir)).mkdirs();
            try {
                findDirectory();
                //new File(DataPath + channel + "\\Users.txt").createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        if(sender.equalsIgnoreCase("kihira")){
            sendMessage(channel, "<3");
        }


        if(sender.equalsIgnoreCase("StoneWaves")){
            sendMessage("StoneWaves", "Creator!");
        }

        sendMessage(channel, "hi");
    }

    //What happens when the bot gets an invite
    protected void onInvite(String nick, String srcNick, String srcLogin, String srcHost, String channel) {
        if (nick.equalsIgnoreCase("Rommie")) {
            joinChannel(channel);
            Channels.add(channel);

            File dir = new File(DataPath + channel);
            new File(String.valueOf(dir)).mkdirs();
        }
    }

    //Directory creation for later use
    private void findDirectory() throws IOException {

        File dir = new File(DataPath);
        new File(DataPath).mkdirs();

    }

    //What happens when the we gets a PM
    protected void  onPrivateMessage(String sender, String login, String hostname, String message){
        sendMessage(sender, "I'm not authorised to talk to you.");
    }

    //Disconnect code
    @Override
    protected void onDisconnect() {
        try {
            reconnect();
        } catch (IOException | IrcException e) {
            e.printStackTrace();
        }
    }

}
