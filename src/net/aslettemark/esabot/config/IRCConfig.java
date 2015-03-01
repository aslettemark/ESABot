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

package net.aslettemark.esabot.config;

import net.aslettemark.esabot.ESABot;
import net.aslettemark.esabot.handler.ConfigHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class IRCConfig extends Config{

    private ESABot bot;

    private String network;
    private String nick;
    private String nsPass;
    private String channels;
    private String herdPass;
    private String wolframID;

    public IRCConfig(ESABot bot) throws IOException {
        this.bot = bot;
        this.setFile(new File("config/config.cfg"));
        this.setHandler(new ConfigHandler(bot, this));
        this.getHandler().checkFile();
        this.interpretFile(this.getHandler().readFile());
        this.enforce();
    }

    @Override
    public void populate() {
        String nl = System.getProperty("line.separator");
        String rnick = "abc" + UUID.randomUUID().toString().substring(0, 6);
        String defaultConfig = "network=irc.esper.net" + nl + "nick=" + rnick + nl + "password=DONOTAUTH" + nl + "channels=#testingbots,#aksels" + nl + "adminpassword=admin" + nl + "wolfram-APPID=<id>";
        this.getHandler().reWrite(defaultConfig);
    }

    @Override
    public void enforce() {
        this.bot.network = this.getNetwork();
        this.bot.nick = this.getNick();
        this.bot.nickpass = this.getNsPass();
        this.bot.wolframAPPID = this.getWolframID();
        this.bot.channels = this.getChannels().split(",");

        for (final String s : this.getHerdPass().split(",")) {
            this.bot.herdpass.add(s);
        }
    }

    @Override
    public void interpretFile(ArrayList<String> file) {
        for (String line : file) {
            String value = line.split("=")[1];
            switch (line.split("=")[0]) {
                case "network":
                    this.setNetwork(value);
                    break;
                case "nick":
                    this.setNick(value);
                    break;
                case "password":
                    this.setNsPass(value);
                    break;
                case "channels":
                    this.setChannels(value);
                    break;
                case "adminpassword":
                    this.setHerdPass(value);
                    break;
                case "wolfram-APPID":
                    this.setWolframID(value);
                    break;
                case "#":
                    break;
                default:
                    System.out.println("You have an unwanted line in your config file");
                    break;
            }
        }
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNsPass() {
        return nsPass;
    }

    public void setNsPass(String nsPass) {
        this.nsPass = nsPass;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getHerdPass() {
        return herdPass;
    }

    public void setHerdPass(String herdPass) {
        this.herdPass = herdPass;
    }

    public String getWolframID() {
        return wolframID;
    }

    public void setWolframID(String wolframID) {
        this.wolframID = wolframID;
    }
}
