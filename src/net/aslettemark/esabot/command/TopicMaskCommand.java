package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class TopicMaskCommand extends CommandExecutor {

    public TopicMaskCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (!pm) {
            if (this.bot.handler.isHerder(sender)) {
                this.bot.topicmask.remove(channel);
                this.bot.topicmask.put(channel, command.substring(10));
            }
        } else {
            this.bot.sendMessage(sender, "Not in a channel.");
        }
    }

}
