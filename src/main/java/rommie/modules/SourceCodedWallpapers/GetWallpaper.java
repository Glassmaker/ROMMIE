package rommie.modules.SourceCodedWallpapers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rommie.Rommie;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetWallpaper {

    public static String webPage = "";

    public static String getWallpaper() throws InterruptedException {

        //TODO Clean up this code and get rid of all the logging and file outputs
        HttpURLConnection connection = null;

        try {
            // get URL content
            URL url = new URL("http://fox.chrisallen.me.uk/index.php");
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;

            //save to this filename
            String fileName = "./source.txt";
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            //use FileWriter to write file
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            while ((inputLine = br.readLine()) != null) {
                bw.write(inputLine);
                System.out.println(inputLine);
                if(inputLine.contains("url")){
                    webPage = inputLine;
                }
            }

            bw.close();
            br.close();

            System.out.println("Done ----------------------------------------------");

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
        return webPage;
    }

    public static String webpage() throws ParseException {
        String page = webPage.replaceAll("</body>", "");



        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(page);
        String url = (String) jsonObject.get("url");
        System.out.println(url);







        return url;
    }


}