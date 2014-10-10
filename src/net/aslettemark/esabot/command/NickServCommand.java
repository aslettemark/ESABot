package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class NickServCommand extends Command {

    public NickServCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (pm && this.bot.handler.isHerder(sender)) {
            if (command.split(" ").length == 1) {
                this.bot.handler.nickservAuth();
                this.bot.sendMessage(sender, "Sent message.");
            } else {
                this.bot.sendMessage("NickServ", command.toLowerCase().replaceFirst("nickserv ", ""));
                this.bot.sendMessage(sender, "Sent message.");
            }
        } else {
            this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
        }
    }

}
