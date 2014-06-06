package net.aslettemark.esabot.command;

import java.util.ArrayList;

import net.aslettemark.esabot.ESABot;

public class NoteCommand extends CommandExecutor {

    public NoteCommand(ESABot bot) {
        super(bot);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (command.length() > 2) {
            final String target = command.split(" ")[1].toLowerCase();
            final String note = command.replaceFirst("note " + target + " ", "");
            ArrayList<String> notes = new ArrayList<String>();
            final String[] disallowed = { "\n", "\r" };
            for (final String rule : disallowed) {
                if (command.contains(rule)) {
                    this.bot.sendMessage(sender, "There was an error in your note.");
                    return;
                }
            }
            if (target.length() > 30) {
                this.bot.sendMessage(sender, "Invalid nickname.");
                return;
            }
            if (this.bot.handler.hasNotes(target)) {
                notes = this.bot.notes.get(target);
                notes.add("<" + sender + "> " + note);
                this.bot.notes.put(target, notes);
            } else {
                notes.add("<" + sender + "> " + note);
                this.bot.notes.put(target, notes);
            }
            if (pm) {
                this.bot.sendMessage(sender, "Note noted.");
            } else {
                this.bot.sendMessage(channel, sender + ": Note noted.");
            }
            this.bot.handler.saveNotes();
        } else {
            this.bot.sendMessage(sender, "Syntax: note <target> <message>");
        }
    }
}
