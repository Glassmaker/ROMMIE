package rommie.modules.OfflineTell;

import org.jibble.pircbot.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rommie.Rommie;

import java.io.*;
import java.util.Iterator;

public class OfflineTell extends Rommie {

    protected void checkTells(String user, String channel){

    }

    public static void createTell(String user, String message, String channel) throws IOException {
        //checkOnline(user, channel);

        try {

            //new writer
            FileWriter file = new FileWriter("test.json");
            //new JSON object
            JSONObject obj = new JSONObject();
            //populate object
            obj.put("name", user);
            obj.put("channel", channel);
            obj.put("message", message);

            //write object
            file.write(obj.toJSONString());
            //file.flush();
            file.close();
            System.out.print(obj);

        } catch (IOException e) {
            e.printStackTrace();
        }


        readMessage(user, channel);

    }




    //Checks if a user exists in the channel
    protected void checkOnline(String User, String Channel){
        User[] users = getUsers(Channel);
        if(User.contains(User)){
            sendMessage(Channel, "That user is online");
        }
        else{
            return;
        }
    }

    protected static void readMessage(String User, String Channel){
        JSONParser parser = new JSONParser();



        try {

            Object obj = parser.parse(new FileReader("test.json"));

            JSONObject jsonObject = (JSONObject) obj;

            if(User.equalsIgnoreCase((String)jsonObject.get("name")) && (Channel.equalsIgnoreCase((String)jsonObject.get("channel"))) ){

                String name = (String) jsonObject.get("name");
                System.out.println(name);

                String message = (String) jsonObject.get("message");
                System.out.println(message);

                String channel = (String) jsonObject.get("channel");
                System.out.println(channel);

            }


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
