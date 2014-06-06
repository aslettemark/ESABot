package net.aslettemark.esabot;

import java.util.ArrayList;
import java.util.HashMap;

import net.aslettemark.esabot.command.*;

import org.jibble.pircbot.PircBot;

public class ESABot extends PircBot {

    public String nick;
    public String nickpass;
    public String network;
    public String[] channels;
    public int port = 6667;

    public ArrayList<String> herders = new ArrayList<String>();
    public ArrayList<String> herdpass = new ArrayList<String>();
    //public ArrayList<String> ops = new ArrayList<String>();

    //K = user with note, value = list of notes in format <sender> note
    public HashMap<String, ArrayList<String>> notes = new HashMap<String, ArrayList<String>>();
    public HashMap<String, CommandExecutor> commandExecutors = new HashMap<String, CommandExecutor>();
    public HashMap<String, String> topicmask = new HashMap<String, String>();
    public HashMap<String, String> files = new HashMap<String, String>();

    public IRCHandler handler;

    public ESABot(String[] args) {
        //set variables
        this.network = args[0];
        this.nick = args[1];
        this.nickpass = args[2];
        this.handler = new IRCHandler(this);
        this.setAutoNickChange(true);
        this.setName(this.nick);
        this.setVersion("ESABot v13.3 BUILD 7");

        //handle files
        this.handler.checkFiles();
        this.handler.loadNotes();

        //do the connection, set up automatic variables
        this.handler.doConnect();
        this.channels = args[3].split(",");
        for (final String c : this.channels) {
            this.joinChannel(c);
            this.topicmask.put(c, "Welcome to " + c + " | %topic");
        }
        for (final String s : args[4].split(",")) {
            this.herdpass.add(s);
        }

        //assign commands
        final IRCHandler h = this.handler;
        h.assignCommand("kill", new KillCommand(this));
        h.assignCommand("auth", new AuthCommand(this));
        h.assignCommand("topic", new TopicCommand(this));
        h.assignCommand("topicmask", new TopicMaskCommand(this));
        h.assignCommand("herders", new HerdersCommand(this));
        h.assignCommand("deauth", new DeAuthCommand(this));
        h.assignCommand("note", new NoteCommand(this));
        h.assignCommand("notes", new NotesCommand(this));
        h.assignCommand("nickserv", new NickServCommand(this));
    }

    /**
     * Triggers when there is a message in a channel.
     * The function passes all the data received to the command handling system.
     */
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
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
            this.notes.remove(sender);
            this.handler.saveNotes();
        }
        if (message.startsWith(this.nick + ": ")) {
            this.commandExecutors.get(message.split(" ")[1]).execute(channel, sender, login, hostname, message.replaceFirst(this.nick + ": ", ""), false);
        } else if (message.startsWith(".")) {
            this.commandExecutors.get(message.split(" ")[0].replaceFirst(".", "")).execute(channel, sender, login, hostname, message.replaceFirst(".", ""), false);
        }
    }

    /**
     * Triggers when the bot receives a private message.
     * The function passes all the data received to the command handling system.
     */
    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        this.commandExecutors.get(message.split(" ")[0]).execute(null, sender, login, hostname, message, true);
        System.out.println("PM " + sender + " > " + message);
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

    @Override
    public void onDisconnect() {
        this.handler.doConnect();
        for (final String c : this.channels) {
            this.joinChannel(c);
        }
    }
}
