package rommie;

public class RommieMain {

    public static void main(String[] args) throws Exception {

        // Now start our bot up.
        Rommie bot = new Rommie();

        // Enable debugging output.
        bot.setVerbose(true);

        // Connect to the IRC server.
        bot.connect("irc.esper.net", 5555);

        // Join the #pircbot channel.
        bot.joinChannel("#StoneWaves");

    }
}
