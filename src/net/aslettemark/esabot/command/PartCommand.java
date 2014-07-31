package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class PartCommand extends CommandExecutor {

    public PartCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if(!this.bot.handler.isHerder(sender)) {
            this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
            return;
        }
        String[] split = command.split(" ");
        if(split.length < 2) {
            this.bot.sendMessage(sender, "Usage: part <channel>");
            return;
        }
        this.bot.partChannel(split[1]);
    }
}
