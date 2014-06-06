package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class NickServCommand extends CommandExecutor {

    public NickServCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (pm && this.bot.handler.isHerder(sender)) {
            if (command.split(" ").length == 1) {
                this.bot.sendMessage("NickServ", "identify " + this.bot.nickpass);
                this.bot.sendMessage(sender, "Sent message.");
            } else {
                this.bot.sendMessage("NickServ", command.toLowerCase().replaceFirst("nickserv ", ""));
                this.bot.sendMessage(sender, "Sent message.");
            }
        } else {
            this.bot.sendMessage(sender, "Not allowed.");
        }
    }

}
