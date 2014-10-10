package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;

public class NotesCommand extends Command {

    public NotesCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (pm && this.bot.handler.isHerder(sender)) {
            for (final String s : this.bot.notes.keySet()) {
                for (final String note : this.bot.notes.get(s)) {
                    this.bot.sendMessage(sender, "To " + s + ": " + note);
                }
            }
        } else {
            this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
        }
    }
}
