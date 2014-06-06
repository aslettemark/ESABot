package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class TopicCommand extends CommandExecutor {

    public TopicCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (!pm) {
            if (this.bot.handler.isHerder(sender)) {
                final String topic = command.substring(6);
                this.bot.setTopic(channel, this.bot.topicmask.get(channel).replace("%topic", topic));
            }
        } else {
            this.bot.sendMessage(sender, "Not in a channel.");
        }
    }

}
