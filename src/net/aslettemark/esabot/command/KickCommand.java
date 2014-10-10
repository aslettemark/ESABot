package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class KickCommand extends Command {

    private ESABot bot;

    public KickCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (!this.bot.handler.isHerder(sender)) {
            this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
            return;
        }
        String[] split = command.split(" ");
        int args = 2;
        if (pm) {
            args = 3;
        }
        if (split.length < args) {
            if (pm) {
                this.bot.sendMessage(sender, "Usage: kick <user> <channel>");
                return;
            } else {
                this.bot.sendMessage(channel, sender + ": kick <user>");
                return;
            }
        }
        if (pm) {
            this.bot.kick(split[2], split[1]);
            this.bot.sendMessage(sender, "Attempted to kick " + split[1]);
            return;
        }
        this.bot.kick(channel, split[1]);
    }
}
