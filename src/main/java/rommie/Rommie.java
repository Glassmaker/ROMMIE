package rommie;

import org.jibble.pircbot.PircBot;

public class Rommie extends PircBot {

    private static final String CMD_PREFIX= ">";

    public Rommie() {
        this.setName("Rommie");
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith(CMD_PREFIX)) {
            message = message.substring(CMD_PREFIX.length()); //Strips command prefix


            //NickServ stuff
            String[] NickServInput = {"auth"};
            String[] NickServOutput = {"identify andromeda"};

            for (int index = 0; index < NickServInput.length; index++) {
                if (message.equalsIgnoreCase(NickServInput[index]) & sender.equals("StoneWaves")) {

                    if (message.equalsIgnoreCase(NickServInput[index])) {

                        String time = new java.util.Date().toString();
                        String out = NickServOutput[index];
                        sendMessage("nickserv", out);
                    }
                }
            }



                //Action replies
                String[] inputAction = {"fox"};
                String[] outputAction = {"throws foxes at $sender" };

                for (int index = 0; index < inputAction.length; index++) {
                    if (message.equalsIgnoreCase(inputAction[index])) {

                        String out = outputAction[index];
                        out = out.replace("$sender", sender);
                        sendAction(channel, out);
                    }
                }



                //Message replies
                String[] inputMessage = {"time"};
                String[] outputMessage = {"$sender : The time is now $time"};

                for (int index = 0; index < inputMessage.length; index++) {
                    if (message.equalsIgnoreCase(inputMessage[index])) {

                        String time = new java.util.Date().toString();
                        String out = outputMessage[index];
                        out = out.replace("$sender", sender);
                        out = out.replace("$time", time);
                        sendMessage(channel, out);
                    }
                }
            }
        }

    }
