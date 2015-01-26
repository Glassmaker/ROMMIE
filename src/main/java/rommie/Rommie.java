package rommie;

import org.jibble.pircbot.PircBot;

public class Rommie extends PircBot {

    private static final String CMD_PREFIX= "!";
    private String creator = "StoneWaves";

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

            if (command == null) {
                return;
            }

            if( command.equals( "time" ) ) {
                sendMessage(channel, new java.util.Date().toString() );
            }

            //this is just an example; is silly and pointless XP
            //usage is "tell person message"
            if( command.equals( "tell" ) ) {
                if( arguments.length < 3 ) {
                    sendMessage( channel, "Usage is tell person name." );
                }
                else
                {
                    String nick = arguments[1];
                    int starting_point = message.indexOf( arguments[1] ) + arguments[1].length() + 1;
                    String message_to_send = message.substring( starting_point );
                    sendMessage( channel, arguments[1] + ": " + message_to_send );
                }
            }
        }
    }






    protected void onJoin(String channel, String sender, String login, String hostname){
        if(sender.equalsIgnoreCase(creator)) {
            sendMessage(channel, "Creator!");
        }
    }
}
