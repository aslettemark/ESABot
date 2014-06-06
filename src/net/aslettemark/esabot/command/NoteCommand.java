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
            final String[] targets = target.split(",");
            final String note = command.replaceFirst("note " + command.split(" ")[1] + " ", "");
            ArrayList<String> notes;
            final String[] disallowed = { "\n", "\r" };
            for (final String rule : disallowed) {
                if (command.contains(rule)) {
                    this.bot.sendMessage(sender, "There was an error in your note.");
                    return;
                }
            }
            for (final String tar : targets) {
                if (tar.length() > 30) {
                    this.bot.sendMessage(sender, "Invalid nickname.");
                    return;
                }
            }
            for (final String tar : targets) {
                if (this.bot.handler.hasNotes(tar)) {
                    notes = this.bot.notes.get(tar);
                    notes.add("<" + sender + "> " + note);
                    this.bot.notes.put(tar, notes);
                } else {
                    notes = new ArrayList<String>();
                    notes.add("<" + sender + "> " + note);
                    this.bot.notes.put(tar, notes);
                }
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
