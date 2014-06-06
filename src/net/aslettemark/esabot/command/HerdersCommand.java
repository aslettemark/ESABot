package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class HerdersCommand extends CommandExecutor {

    public HerdersCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (pm && this.bot.handler.isHerder(sender)) {
            for (String s : this.bot.herders) {
                this.bot.sendMessage(sender, s);
            }
        }
    }

}
