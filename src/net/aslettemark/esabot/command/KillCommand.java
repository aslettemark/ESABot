package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

public class KillCommand extends CommandExecutor {

    public KillCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (this.bot.handler.isHerder(sender)) {
            if (!pm) {
                this.bot.sendMessage(channel, "Goodbye");
                try {
                    Thread.sleep(1100);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                this.bot.disconnect();
                this.bot.dispose();
            } else {
                this.bot.disconnect();
                this.bot.dispose();
            }
        } else {
            if (pm) {
                this.bot.sendMessage(sender, "Not allowed");
            } else {
                this.bot.sendMessage(channel, "Not allowed");
            }
        }
    }

}
