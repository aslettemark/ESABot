package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public abstract class Command implements CommandExecutor {

    public ESABot bot = null;
    public String command = null;

    public Command(ESABot bot, String cmd) {
        this.bot = bot;
        this.command = cmd;
    }

    public abstract void execute(String channel, String sender, String login, String hostname, String command, boolean pm);


}
