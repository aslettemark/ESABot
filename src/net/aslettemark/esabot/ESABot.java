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

package net.aslettemark.esabot;

import net.aslettemark.esabot.command.*;
import net.aslettemark.esabot.config.IRCConfig;
import net.aslettemark.esabot.handler.FileHandler;
import net.aslettemark.esabot.handler.IRCHandler;
import org.jibble.pircbot.PircBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static net.aslettemark.esabot.Strings.CTCP_VERSION;
import static net.aslettemark.esabot.Strings.DEFAULT_TOPIC_MASK;

public class ESABot extends PircBot {

    public String nick;
    public String nickpass;
    public String network;
    public String wolframAPPID;

    public String[] channels;
    public int port = 6667;

    public ArrayList<String> herders = new ArrayList<String>();
    public ArrayList<String> herdpass = new ArrayList<String>();
    public ArrayList<Command> commands = new ArrayList<Command>();
    //public ArrayList<String> ops = new ArrayList<String>();

    //Key = user with note, value = list of notes
    public HashMap<String, ArrayList<String>> notes = new HashMap<String, ArrayList<String>>();
    public HashMap<String, String> topicmask = new HashMap<String, String>();
    public HashMap<String, String> files = new HashMap<String, String>();

    public IRCHandler handler;
    public FileHandler fileHandler;
    public IRCConfig config;

    public ESABot(String[] args) {
        //set variables
        this.handler = new IRCHandler(this);
        this.fileHandler = new FileHandler(this);
        try {
            this.config = new IRCConfig(this);
        } catch (IOException e) {
            System.out.println("Unable to create config folder/file");
            e.printStackTrace();
            this.dispose();
        }
        this.setAutoNickChange(true);
        this.setName(this.nick);
        this.setVersion(CTCP_VERSION);

        //handle files
        this.fileHandler.checkFiles();
        this.fileHandler.loadNotes();

        //do the connection, set up automatic variables
        this.handler.doConnect();
        this.handler.nickservAuth();

        for (final String c : this.channels) {
            this.joinChannelAndWorkMagic(c);
        }

        //assign commands
        final IRCHandler h = this.handler;
        h.assignCommand(new KillCommand(this, "kill"));
        h.assignCommand(new AuthCommand(this, "auth"));
        h.assignCommand(new TopicCommand(this, "topic"));
        h.assignCommand(new TopicMaskCommand(this, "topicmask"));
        h.assignCommand(new HerdersCommand(this, "herders"));
        h.assignCommand(new DeAuthCommand(this, "deauth"));
        h.assignCommand(new NoteCommand(this, "note"));
        h.assignCommand(new NotesCommand(this, "notes"));
        h.assignCommand(new NickServCommand(this, "nickserv"));
        h.assignCommand(new PingCommand(this, "ping"));
        h.assignCommand(new KickCommand(this, "kick"));
        h.assignCommand(new JoinCommand(this, "join"));
        h.assignCommand(new PartCommand(this, "part"));
        h.assignCommand(new OpmeCommand(this, "opme"));
        h.assignCommand(new WolframCommand(this, "wolfram"));
        h.assignCommand(new WolframCommand(this, "w"));
        h.assignCommand(new DocsCommand(this, "docs"));

        MainClass.takingInput = true;
    }

    /**
     * Triggers when there is a message in a channel.
     * The function passes all the data received to the command handling system.
     */
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if(MainClass.listeningChannels.get(channel)) {
            MainClass.print(channel + " <" + sender + "> " + message);
        }
        if (this.handler.hasNotes(sender.toLowerCase())) {
            this.sendMessage(channel, sender + ", you have notes!");
            for (final String s : this.notes.keySet()) {
                if (s.equalsIgnoreCase(sender)) {
                    if (this.notes.get(s).size() > 4) {
                        this.sendMessage(channel, "Too many notes, sending in PM.");
                        for (final String note : this.notes.get(s)) {
                            this.sendMessage(sender, "Note: " + note);
                        }
                    } else {
                        for (final String note : this.notes.get(s)) {
                            this.sendMessage(channel, sender + ": " + note);
                        }
                    }
                }
            }
            this.notes.remove(sender.toLowerCase());
            this.fileHandler.saveNotes();
        }
        if (message.startsWith(this.nick + ": ")) {
            for (Command cmd : this.commands) {
                if (cmd.command.equalsIgnoreCase(message.split(" ")[1])) {
                    cmd.execute(channel, sender, login, hostname, message.replaceFirst(this.nick + ": ", ""), false);
                }
            }
        } else if (message.startsWith(".")) {
            for (Command cmd : this.commands) {
                if (cmd.command.equalsIgnoreCase(message.split(" ")[0].replaceFirst(".", ""))) {
                    cmd.execute(channel, sender, login, hostname, message.replaceFirst(".", ""), false);
                }
            }
        }
    }

    /**
     * Triggers when the bot receives a private message.
     * The function passes all the data received to the command handling system.
     */
    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        for (Command cmd : this.commands) {
            if (cmd.command.equalsIgnoreCase(message.split(" ")[0])) {
                cmd.execute(null, sender, login, hostname, message, true);
            }
        }
        System.out.println("PM " + sender + " > " + message);
        System.out.print("> ");
    }

    /**
     * Triggers when the bot receives a notice
     */
    @Override
    public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        System.out.println("NOTICE " + sourceNick + " > " + notice);
        System.out.print("> ");
    }

    /**
     * Triggers on kick, used for herder protection.
     */
    @Override
    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        if (this.herders.contains(recipientNick) && !(kickerNick.equalsIgnoreCase(this.nick))) {
            this.deOp(channel, kickerNick);
        }
    }

    /**
     * Triggers when the bot disconnects from IRC
     */
    @Override
    public void onDisconnect() {
        this.handler.doConnect();
        for (final String c : this.channels) {
            this.joinChannel(c);
        }
        this.handler.nickservAuth();
    }

    public void joinChannelAndWorkMagic(String c) {
        this.joinChannel(c);
        this.topicmask.put(c, DEFAULT_TOPIC_MASK.replaceAll("%channel", c));
        MainClass.listeningChannels.put(c, false);
    }
}
