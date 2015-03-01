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

import com.wolfram.alpha.*;
import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.Strings;
import net.aslettemark.esabot.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WolframCommand extends Command {

    public WolframCommand(ESABot bot, String command) {
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
        String input = StringUtils.combineSplit(1, command.split(" "), " ");

        WAEngine engine = new WAEngine();
        engine.setAppID(this.bot.wolframAPPID);
        engine.addFormat("plaintext");
        WAQuery query = engine.createQuery(input);

        int wantedlines = 3;
        int count = 1;
        try {
            WAQueryResult result = engine.performQuery(query);
            if(result.isError()) {
                System.out.println("Error");
            } else if (!result.isSuccess()) {
                this.bot.sendMessage(ret, String.format(msg, "Query not understood or no available results"));
            } else {
                //Woo. We're through.
                for (WAPod pod : result.getPods()) {
                    if (!pod.isError()) {
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if ((element instanceof WAPlainText) && (count <= wantedlines) && !(((WAPlainText) element).getText().equalsIgnoreCase(""))) {
                                    this.bot.sendMessage(ret, String.format(msg, ((WAPlainText) element).getText()));
                                    count++;
                                }
                            }
                        }
                    }
                }
                this.bot.sendMessage(ret, String.format(msg, "Want more? http://www.wolframalpha.com/input/?i=" + URLEncoder.encode(input, "UTF-8")));
            }
        } catch (WAException e) {
            e.printStackTrace();
            this.bot.sendMessage(ret, String.format(msg, Strings.QUERY_ERROR));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
