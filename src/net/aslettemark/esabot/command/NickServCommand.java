package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class NickServCommand extends CommandExecutor {

    public NickServCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (pm && bot.handler.isHerder(sender)) {
            if (command.split(" ").length == 1) {
                bot.sendMessage("NickServ", "identify " + bot.nickpass);
                bot.sendMessage(sender, "Sent message.");
            } else {
                bot.sendMessage("NickServ", command.toLowerCase().replaceFirst("nickserv ", ""));
                bot.sendMessage(sender, "Sent message.");
            }
        } else {
            bot.sendMessage(sender, "Not allowed.");
        }
    }

}
