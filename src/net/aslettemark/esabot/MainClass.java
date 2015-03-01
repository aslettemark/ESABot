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

import net.aslettemark.esabot.util.StringUtils;

import java.util.HashMap;
import java.util.Scanner;

public class MainClass {

    public static ESABot bot;
    public static boolean takingInput = false;
    public static HashMap<String, Boolean> listeningChannels = new HashMap<>();

    public static void main(String[] args) {
        MainClass.bot = new ESABot(args);
        while(takingInput) {
            Scanner scan = new Scanner(System.in);
            String command = scan.nextLine();
            String[] split = command.split(" ");

            switch(split[0]) {
                case "stop":
                    System.exit(0);
                case "exit":
                    System.exit(0);
                case "msg":
                    if (split.length > 2) {
                        String msg = StringUtils.combineSplit(2, split, " ");
                        bot.sendMessage(split[1], msg);
                        print(split[1] + " <" + bot.nick + "> " + msg);
                    } else {
                        print("msg <#channel/nick> <message>");
                    }
                    break;
                case "toggle":
                    if (split.length > 1) {
                        if(listeningChannels.containsKey(split[1])) {
                            listeningChannels.put(split[1], !listeningChannels.get(split[1]));
                            print("Listening to " + split[1] + ": " + listeningChannels.get(split[1]));
                        } else {
                            print("We're not in that channel!");
                        }
                    } else {
                        print("toggle <channel>");
                    }
                    break;
                default:
                    print("Unrecognized command");
            }
        }
    }

    public static void print(String message) {
        System.out.println(message);
    }

}