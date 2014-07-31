package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

import static net.aslettemark.esabot.Strings.DEFAULT_TOPIC_MASK;

public class JoinCommand extends CommandExecutor {

    public JoinCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if(!this.bot.handler.isHerder(sender)) {
            this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
            return;
        }
        String[] split = command.split(" ");
        if(split.length < 2) {
            this.bot.sendMessage(sender, "Usage: join <channel>");
            return;
        }
        this.bot.joinChannel(split[1]);
        this.bot.topicmask.put(split[1], DEFAULT_TOPIC_MASK.replaceAll("%channel", split[1]));
    }
}
