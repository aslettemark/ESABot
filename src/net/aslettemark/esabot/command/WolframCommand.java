package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;
import net.aslettemark.esabot.util.StringUtils;

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
        String toencode = StringUtils.combineSplit(1, command.split(" "), " ");
        try {
            this.bot.sendMessage(ret, String.format(msg, "http://www.wolframalpha.com/input/?i=" + URLEncoder.encode(toencode, "UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
            this.bot.sendMessage(ret, String.format(msg, Strings.QUERY_ERROR));
        }
    }
}
