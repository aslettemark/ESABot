package net.aslettemark.esabot;

import net.aslettemark.esabot.command.*;
import net.aslettemark.esabot.handler.FileHandler;
import net.aslettemark.esabot.handler.IRCHandler;
import org.jibble.pircbot.PircBot;

import java.util.ArrayList;
import java.util.HashMap;

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
    public FileHandler fileHandler;

    public ESABot(String[] args) {
        //set variables
        this.network = args[0];
        this.nick = args[1];
        this.nickpass = args[2];
        this.handler = new IRCHandler(this);
        this.fileHandler = new FileHandler(this);
        this.setAutoNickChange(true);
        this.setName(this.nick);
        this.setVersion("ESABot v13.3 BUILD 7");

        //handle files
        this.fileHandler.checkFiles();
        this.fileHandler.loadNotes();

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
        this.handler.nickservAuth();

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
        h.assignCommand("ping", new PingCommand(this));
        h.assignCommand("kick", new KickCommand(this));
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
            this.notes.remove(sender.toLowerCase());
            this.fileHandler.saveNotes();
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
     * Triggers when the bot receives a notice
     *
     * @param sourceNick     The nick of the user that sent the notice.
     * @param sourceLogin    The login of the user that sent the notice.
     * @param sourceHostname The hostname of the user that sent the notice.
     * @param target         The target of the notice, be it our nick or a channel name.
     * @param notice         The notice message.
     */
    @Override
    public void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        System.out.println("NOTICE " + sourceNick + " > " + notice);
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
}
