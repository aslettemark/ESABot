package net.aslettemark.esabot;

import java.util.Scanner;

public class MainClass {

    public static ESABot bot;
    public static boolean takingInput = false;

    public static void main(String[] args) {
        MainClass.bot = new ESABot(args);
        while(takingInput) {
            Scanner scan = new Scanner(System.in);
            System.out.print("> ");
            String command = scan.nextLine();

            switch(command) {
                case "stop":
                    System.exit(0);
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("Unrecognized command");
            }
        }
    }
}