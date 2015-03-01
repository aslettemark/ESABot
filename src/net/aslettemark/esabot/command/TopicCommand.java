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
import net.aslettemark.esabot.Strings;

public class TopicCommand extends Command {

    public TopicCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        if (!pm) {
            if (this.bot.handler.isHerder(sender)) {
                final String topic = command.substring(6);
                this.bot.setTopic(channel, this.bot.topicmask.get(channel).replace("%topic", topic));
            } else {
                this.bot.sendMessage(sender, Strings.ACTION_NOT_ALLOWED);
            }
        } else {
            this.bot.sendMessage(sender, "Not in a channel.");
        }
    }

}
