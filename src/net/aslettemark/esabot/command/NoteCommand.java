/*
 *  Copyright (C) 2014-2015 Aksel H. Slettemark http://aslettemark.net/
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.aslettemark.esabot.command;

import net.aslettemark.esabot.ESABot;

import java.util.ArrayList;

public class NoteCommand extends Command {

    public NoteCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (command.length() > 2) {
            final String target = command.split(" ")[1].toLowerCase();
            final String[] targets = target.split(",");
            final String note = command.replaceFirst("note " + command.split(" ")[1] + " ", "");
            ArrayList<String> notes;
            final String[] disallowed = {"\n", "\r"};
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
            this.bot.fileHandler.saveNotes();
        } else {
            this.bot.sendMessage(sender, "Syntax: note <target> <message>");
        }
    }
}
