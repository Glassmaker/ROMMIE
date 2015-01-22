package rommie;

public class RommieMain {

    public static void main(String[] args) throws Exception {

        // Now start our bot up.
        Rommie bot = new Rommie();

        // Enable debugging output.
        bot.setVerbose(true);

        // Connect to the IRC server.
        bot.connect("irc.esper.net", 5555);

        //ident with nickserv
        bot.identify("andromeda");

        // Join channels.
        bot.joinChannel("#StoneWaves");
        bot.joinChannel("#Rommie");
        //bot.joinChannel("#flailbot");
        //bot.joinChannel("#FetishCraft");


    }
}
