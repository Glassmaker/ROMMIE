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
                command = arguments[0];
            }

            if (command == null) {
                return;
            }

        }
    }






    protected void onJoin(String channel, String sender, String login, String hostname){
        if(sender.equalsIgnoreCase(creator)) {
            sendMessage(channel, "Creator!");
        }
    }
}
