package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class DeAuthCommand extends CommandExecutor {

    public DeAuthCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (this.bot.handler.isHerder(sender)) {
            this.bot.herders.remove(sender);
            this.bot.sendMessage(sender, "De-Authed");
        }
    }

}
