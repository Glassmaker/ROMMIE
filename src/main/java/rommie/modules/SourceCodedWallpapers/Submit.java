package rommie.modules.SourceCodedWallpapers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Submit {


    public static void Submit(String message, String[] arguments) {

        //sourcecoded.info/wallpaper/?title=Fox&url=&source=Rommie&url=www.potato.com&nsfw=&tags=fox#submit

        int starting_point = message.indexOf("uploadfox") + "uploadfox ".length();

        String message_to_send = message.substring(starting_point).trim();

        System.out.println(message_to_send);

        HttpURLConnection connection = null;
        try {
            // get URL content
            URL url = new URL("http://sourcecoded.info/wallpaper/submitGet.php?title=Fox&source=Rommie&url=" + message_to_send + "&nsfw=false&tags=fox");
            //URL url = new URL("http://www.google.com");
            URLConnection conn = url.openConnection();

        } catch (Exception e) {
            e.printStackTrace();;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }















        //System.out.println("sourcecoded.info/wallpaper/?title=Fox&url=" + message_to_send + "&source=Rommie&url=www.potato.com&nsfw=&tags=fox");

        //System.out.println("sourcecoded.info/wallpaper/submitGet.php?title=Fox&source=Rommie&url=" + message_to_send + "&nsfw=false&tags=fox");

    }

}
