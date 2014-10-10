package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class TopicMaskCommand extends Command {

    public TopicMaskCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (!pm) {
            if (this.bot.handler.isHerder(sender)) {
                this.bot.topicmask.remove(channel);
                this.bot.topicmask.put(channel, command.substring(10));
            } else {
                this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
            }
        } else {
            this.bot.sendMessage(sender, "Not in a channel.");
        }
    }

}
