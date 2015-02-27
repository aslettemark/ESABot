package net.aslettemark.esabot.command;

import com.wolfram.alpha.*;
import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;
import net.aslettemark.esabot.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WolframCommand extends Command {

    public WolframCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        String ret = channel;
        String msg = sender + ": %s";
        if(pm) {
            ret = sender;
            msg = "%s";
        }
        String input = StringUtils.combineSplit(1, command.split(" "), " ");

        WAEngine engine = new WAEngine();
        engine.setAppID(this.bot.wolframAPPID);
        engine.addFormat("plaintext");
        WAQuery query = engine.createQuery(input);

        int wantedlines = 3;
        int count = 1;
        try {
            WAQueryResult result = engine.performQuery(query);
            if(result.isError()) {
                System.out.println("Error");
            } else if (!result.isSuccess()) {
                this.bot.sendMessage(ret, String.format(msg, "Query not understood or no available results"));
            } else {
                //Woo. We're through.
                for (WAPod pod : result.getPods()) {
                    if (!pod.isError()) {
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if ((element instanceof WAPlainText) && (count <= wantedlines) && !(((WAPlainText) element).getText().equalsIgnoreCase(""))) {
                                    this.bot.sendMessage(ret, String.format(msg, ((WAPlainText) element).getText()));
                                    count++;
                                }
                            }
                        }
                    }
                }
                this.bot.sendMessage(ret, String.format(msg, "Want more? http://www.wolframalpha.com/input/?i=" + URLEncoder.encode(input, "UTF-8")));
            }
        } catch (WAException e) {
            e.printStackTrace();
            this.bot.sendMessage(ret, String.format(msg, Strings.QUERY_ERROR));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
