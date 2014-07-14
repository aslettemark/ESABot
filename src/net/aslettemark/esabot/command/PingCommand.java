package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class PingCommand extends CommandExecutor {

    public PingCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if(pm) {
            return;
        }
        this.bot.sendMessage(channel, sender + ": Pong!");
    }
}
