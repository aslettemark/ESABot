package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class HerdersCommand extends Command {

    public HerdersCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (pm && this.bot.handler.isHerder(sender)) {
            for (final String s : this.bot.herders) {
                this.bot.sendMessage(sender, s);
            }
        } else {
            this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
        }
    }

}
