package rommie;

import java.util.ArrayList;

public class RommieMain {

    public static void main(String[] args) throws Exception {

        // Now start our bot up.
        Rommie bot = new Rommie();

        // Enable debugging output.
        bot.setVerbose(true);

        // Connect to the IRC server.
        bot.connect("irc.esper.net", 5555);

        //identify with nickserv
        bot.identify("andromeda");

        // Join channels.
        bot.joinChannel("#StoneWaves");
        bot.joinChannel("#Rommie");

    }
}
