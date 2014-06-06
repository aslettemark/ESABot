package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class AuthCommand extends CommandExecutor {

    public AuthCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (pm) {
            if (this.bot.herdpass.contains(command.split(" ")[1])) {
                this.bot.herders.add(sender);
                this.bot.sendMessage(sender, "Added to herders");
            }
        } else {
            this.bot.sendMessage(sender, "Auth is not a public command.");
        }
    }

}
