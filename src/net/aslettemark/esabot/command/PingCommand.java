package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class PingCommand extends Command {

    public PingCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (pm) {
            this.bot.sendMessage(sender, "Pong!");
        }
        this.bot.sendMessage(channel, sender + ": Pong!");
    }
}
