package net.aslettemark.esabot.handler;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.command.Command;
import net.aslettemark.esabot.command.CommandExecutor;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import java.io.IOException;

public class IRCHandler {

    public ESABot bot;

    public IRCHandler(ESABot bot) {
        this.bot = bot;
    }

    /**
     * @param nick The nick to check
     * @return Wether the specified nick is a bot herder
     */
    public boolean isHerder(String nick) {
        return this.bot.herders.contains(nick);
    }

    /**
     * Connects to the network the bot is configured to use
     */
    public void doConnect() {
        try {
            this.bot.connectWithNoB(this.bot.network, this.bot.port, null);
            System.out.println("Connected to " + this.bot.network + " on port " + this.bot.port);
        } catch (final NickAlreadyInUseException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final IrcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Assigns a command to the specified CommandExecutor
     *
     * @param command Command to assign
     * @param cmd    CommandExecutor to assign to
     */
    public void assignCommand(Command cmd) {
        this.bot.commands.add(cmd);
    }

    /**
     * @param nick The nick to check
     * @return Wether the specified nick has any unread notes or not
     */
    public boolean hasNotes(String nick) {
        return this.bot.notes.containsKey(nick);
    }

    /**
     * Identifies with NickServ
     */
    public void nickservAuth() {
        this.bot.sendMessage("NickServ", "identify " + this.bot.nick + " " + this.bot.nickpass);
    }

}
