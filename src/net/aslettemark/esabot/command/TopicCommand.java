package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class TopicCommand extends Command {

    public TopicCommand(ESABot bot, String command) {
        super(bot, command);
    }
    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (!pm) {
            if (this.bot.handler.isHerder(sender)) {
                final String topic = command.substring(6);
                this.bot.setTopic(channel, this.bot.topicmask.get(channel).replace("%topic", topic));
            } else {
                this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
            }
        } else {
            this.bot.sendMessage(sender, "Not in a channel.");
        }
    }

}
