package rommie.modules.Logger;

import rommie.Rommie;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {

    //TODO Log messages from Rommie and not just other users

    //Log a channel message
    public static void logMessage(String channel, String message, String sender){

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

    //Log a channel action
    public static void logAction(String channel, String message, String sender){

        DateFormat logDateFormat = new SimpleDateFormat("E - dd.MM.yyyy");
        DateFormat messageDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        BufferedWriter myOutFile;
        try {
            //Set up the file writer
            myOutFile = new BufferedWriter( new FileWriter(Rommie.DATA_PATH + channel +  "\\" + logDateFormat.format(date) + ".txt" , true ) );
            myOutFile.write("[" + messageDateFormat.format(date) + "] *" + sender + " " + message + "\n" );
            myOutFile.close();
        } catch( IOException f )
        {
            f.printStackTrace();
        }
    }

    //Log a channel join
    public static void logJoin(String channel, String message, String sender){

        DateFormat logDateFormat = new SimpleDateFormat("E - dd.MM.yyyy");
        DateFormat messageDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        BufferedWriter myOutFile;
        try {
            //Set up the file writer
            myOutFile = new BufferedWriter( new FileWriter(Rommie.DATA_PATH + channel +  "\\" + logDateFormat.format(date) + ".txt" , true ) );
            myOutFile.write("[" + messageDateFormat.format(date) + "] " + sender + " joined the channel.\n" );
            myOutFile.close();
        } catch( IOException f )
        {
            f.printStackTrace();
        }
    }

    //Log a channel part
    public static void logPart(String channel, String message, String sender){

        DateFormat logDateFormat = new SimpleDateFormat("E - dd.MM.yyyy");
        DateFormat messageDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        BufferedWriter myOutFile;
        try {
            //Set up the file writer
            myOutFile = new BufferedWriter( new FileWriter(Rommie.DATA_PATH + channel +  "\\" + logDateFormat.format(date) + ".txt" , true ) );
            myOutFile.write("[" + messageDateFormat.format(date) + "] " + sender + " left the channel.\n" );
            myOutFile.close();
        } catch( IOException f )
        {
            f.printStackTrace();
        }
    }

    //Log a channel kick
    public static void logKick(String channel, String message, String sender){

        DateFormat logDateFormat = new SimpleDateFormat("E - dd.MM.yyyy");
        DateFormat messageDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        BufferedWriter myOutFile;
        try {
            //Set up the file writer
            myOutFile = new BufferedWriter( new FileWriter(Rommie.DATA_PATH + channel +  "\\" + logDateFormat.format(date) + ".txt" , true ) );
            myOutFile.write("[" + messageDateFormat.format(date) + "] " + sender + " was kicked from the channel.\n" );
            myOutFile.close();
        } catch( IOException f )
        {
            f.printStackTrace();
        }
    }
}
