package net.aslettemark.esabot;

import net.aslettemark.esabot.util.StringUtils;

import java.util.HashMap;
import java.util.Scanner;

public class MainClass {

    public static ESABot bot;
    public static boolean takingInput = false;
    public static HashMap<String, Boolean> listeningChannels = new HashMap<>();

    public static void main(String[] args) {
        System.out.print("> ");
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
                        print("<" + bot.nick + "> " + msg);
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
        System.out.print("> ");
    }

}