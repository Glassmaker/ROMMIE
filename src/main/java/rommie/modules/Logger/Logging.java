package rommie.modules.Logger;

import rommie.Rommie;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {

    public static void log(String channel, String message, String sender){

        DateFormat logDateFormat = new SimpleDateFormat("E - dd.MM.yyyy");
        DateFormat messageDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        BufferedWriter myOutFile;
        try {
            //Set up the file writer
            myOutFile = new BufferedWriter( new FileWriter(Rommie.DATA_PATH + channel +  "\\" + logDateFormat.format(date) + ".txt" , true ) );
            myOutFile.write("[" + messageDateFormat.format(date) + "] <" + sender + "> " + message + "\n" );
            myOutFile.close();
        } catch( IOException f )
        {
            f.printStackTrace();
        }
    }


}
