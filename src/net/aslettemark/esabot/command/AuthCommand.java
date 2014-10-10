package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class AuthCommand extends Command {

    public AuthCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (pm) {
            if (this.bot.herdpass.contains(command.split(" ")[1])) {
                this.bot.herders.add(sender);
                this.bot.sendMessage(sender, "Added to herders");
            } else {
                this.bot.sendMessage(sender, Strings.UNRECOGNIZED_PASSWORD);
            }
        } else {
            this.bot.sendMessage(sender, "Auth is not a public command.");
        }
    }

}
