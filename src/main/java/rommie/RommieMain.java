package rommie;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RommieMain {

    public static Properties config = new Properties();

    public static void main(String[] args) throws Exception {

        //Load the config file
        try {
            config.load(new FileInputStream("Rommie.properties"));
        } catch (IOException ioex) {
            System.err.println("Error loading config file: Rommie.properties");
            System.exit(0);
        }

        // Now start our bot up.
        Rommie bot = new Rommie();

        // Enable debugging output.
        bot.setVerbose(true);

        // Connect to the IRC server.
        bot.connect(config.getProperty("server"), Integer.parseInt(config.getProperty("port")));

        //identify with nickserv
        bot.identify(config.getProperty("ident"));

        // Join channels.
        bot.joinChannel("#StoneWaves");
        bot.joinChannel("#Rommie");
        //bot.joinChannel("#FetishCraft");
        //bot.joinChannel("#Kihira");

    }
}
