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

package net.aslettemark.esabot.handler;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.command.Command;
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
            this.bot.connect(this.bot.network, this.bot.port);
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
     * @param cmd Command to assign
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
        if(!this.bot.nickpass.equalsIgnoreCase("DONOTAUTH")) {
            this.bot.sendMessage("NickServ", "identify " + this.bot.nick + " " + this.bot.nickpass);
        } else {
            System.out.println("Skipping nickserv auth");
        }
    }

}
