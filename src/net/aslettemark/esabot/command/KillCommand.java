package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class KillCommand extends Command {

    public KillCommand(ESABot bot, String command) {
        super(bot, command);
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
            this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
        }
    }

}
