package rommie.commands;

import com.google.gson.Gson;
import org.jibble.pircbot.User;
import rommie.Rommie;
import rommie.modules.GoogleResults.GoogleResults;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;

public class CommandGoogle  extends CommandBase {

    public CommandGoogle()
    {
        super("google", 3);
    }
    @Override
    public void performCommand(User user, String channel, String[] args, String message, Rommie instance) throws IOException {
        if (args.length < 2) {
            this.sendUsageMessage(channel, instance);
        } else {
            String address = "http://ajax.googleapis.com/ajax/services/search/web?start=0&rsz=small&v=1.0&q=";

            int starting_point = message.indexOf(args[1]);
            String query = message.substring(starting_point);
            String charset = "UTF-8";

            URL url = new URL(address + URLEncoder.encode(query, charset));
            Reader reader = new InputStreamReader(url.openStream(), charset);
            GoogleResults results = new Gson().fromJson(reader, GoogleResults.class);


            if(results.getResponseData().getResults().size() == 0){
                instance.sendMessage(channel, user + " : No results were found for your search of " + query);
            }
            else {
                // Show title and URL of each results
                String ResultTitle = results.getResponseData().getResults().get(0).getTitle();
                String ResultURL = results.getResponseData().getResults().get(0).getUrl();
                String ResultContent = results.getResponseData().getResults().get(0).getContent();

                String ResultOutput = user + " : " + ResultURL + " -- " + ResultTitle + " : " + ResultContent;

                // Remove tags on the returned results


                ResultOutput = ResultOutput.replaceAll("&#39;", "'");
                ResultOutput = ResultOutput.replaceAll("</b>", "");
                ResultOutput = ResultOutput.replaceAll("<b>", "");

                instance.sendMessage(channel, ResultOutput);
            }
        }
    }

    @Override
    public String getUsageHelp() {
        return "google <query>. Get the first result for your query.";
    }

}
