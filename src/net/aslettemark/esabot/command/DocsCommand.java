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
import net.aslettemark.esabot.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DocsCommand extends Command {

    public DocsCommand(ESABot bot, String command) {
        super(bot, command);
    }

    @Override
    public void execute(String channel, String sender, String login, String hostname, String command, boolean pm) {
        String ret = channel;
        String msg = sender + ": %s";
        if(pm) {
            ret = sender;
            msg = "%s";
        }
        if(command.split(" ").length < 3) {
            this.bot.sendMessage(ret, String.format(msg, Strings.USAGE_DOCS));
        }

        String language = command.split(" ")[1];
        String query = StringUtils.combineSplit(2, command.split(" "), " ");

        switch (language) {
            case "java":
                try {
                    this.bot.sendMessage(ret, String.format(msg, "http://javadocs.org/" + URLEncoder.encode(query, "UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    this.bot.sendMessage(ret, String.format(msg, Strings.USAGE_DOCS));
                    e.printStackTrace();
                }
                break;
            default:
                this.bot.sendMessage(ret, String.format(msg, "Language not supported"));
                this.bot.sendMessage(ret, String.format(msg, "docs java <class to look up>"));
        }
    }
}