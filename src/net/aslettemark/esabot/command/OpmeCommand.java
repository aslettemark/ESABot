package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class OpmeCommand extends Command {

    public OpmeCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        String[] split = command.split(" ");
        if ((!pm && split.length > 1) || (pm && split.length != 2)) {
            this.bot.sendMessage(sender, Strings.INSUFFICIENT_ARGUMENTS);
            return;
        }
        if (!this.bot.handler.isHerder(sender)) {
            this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
            return;
        }
        String chan = channel;
        if (pm) {
            chan = split[1];
        }
        this.bot.op(chan, sender);
    }
}
