package net.aslettemark.esabot;

import java.io.*;
import java.util.ArrayList;

import net.aslettemark.esabot.command.*;

import org.jibble.pircbot.*;

public class IRCHandler {

    public ESABot bot;

    public IRCHandler(ESABot bot) {
        this.bot = bot;
    }

    /**
     * Returns wether the specified nick is a bot herder
     * 
     * @param nick
     *            The nick to check
     * @return
     */
    public boolean isHerder(String nick) {
        if (bot.herders.contains(nick)) {
            return true;
        }
        return false;
    }

    /**
     * Connects to the network the bot is configured to use
     */
    public void doConnect() {
        try {
            this.bot.connectWithNoB(this.bot.network, this.bot.port, null);
            System.out.println("Connected to " + this.bot.network + " on port " + this.bot.port);
        } catch (NickAlreadyInUseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads unread notes from the note file.
     */
    public void loadNotes() {
        String fileName = this.bot.files.get("notes");
        String line = null;
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(reader);
            String target = "";
            ArrayList<String> file = new ArrayList<String>();
            while ((line = buffer.readLine()) != null) {
                file.add(line);
            }
            for (int i = 0; i < file.size(); i++) {
                line = file.get(i);
                if (!line.startsWith("  ")) {
                    target = line.replace(System.getProperty("line.separator"), "");
                    bot.notes.put(target, new ArrayList<String>());
                }
            }
            for (int i = 0; i < file.size(); i++) {
                line = file.get(i);
                if (line.startsWith("  ")) {
                    bot.notes.get(target).add(line.replaceFirst("  ", ""));
                } else {
                    target = line.replace(System.getProperty("line.separator"), "");
                }
            }
            buffer.close();
            reader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Not able to open file " + fileName);
        } catch (IOException e) {
            System.out.println("Encountered an error reading file " + fileName);
        }
    }

    /**
     * Updates the note savefile
     */
    public void saveNotes() {
        String fileName = this.bot.files.get("notes");
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter buffer = new BufferedWriter(fileWriter);
            for (String target : this.bot.notes.keySet()) {
                buffer.write(target);
                buffer.newLine();
                for (String note : this.bot.notes.get(target)) {
                    buffer.write("  " + note);
                    buffer.newLine();
                }
            }
            buffer.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file " + fileName);
        }
    }

    /**
     * Verifies that all needed files exist, and creates them if not
     */
    public void checkFiles() {
        String network = bot.network.replace(".", "-");
        File notes = new File("data/" + network + "/notes.txt");
        File data = new File("data");
        File net = new File("data/" + network);
        if (!data.exists()) {
            data.mkdir();
            net.mkdir();
        }
        if (notes.exists() && !notes.isDirectory()) {
            System.out.println("Verified notes.txt for " + network);
        } else {
            System.out.println("Could not verify notes.txt for " + network);
            try {
                notes.createNewFile();
                System.out.println("Created new file: data/" + network + "/notes.txt");
            } catch (IOException e) {
                System.out.println("Was not able to create notes.txt for " + network);
                e.printStackTrace();
            }
        }
        this.bot.files.put("notes", net + "/notes.txt");
    }

    /**
     * Assigns a command to the specified CommandExecutor
     * 
     * @param command
     *            Command to assign
     * @param exec
     *            CommandExecutor to assign to
     */
    public void assignCommand(String command, CommandExecutor exec) {
        this.bot.commandExecutors.put(command, exec);
    }

    /**
     * Returns wether the specified nick has any unread notes
     * 
     * @param nick
     *            The nick to check
     * @return
     */
    public boolean hasNotes(String nick) {
        if (this.bot.notes.containsKey(nick)) {
            return true;
        }
        return false;
    }

}
