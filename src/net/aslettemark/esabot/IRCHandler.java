package net.aslettemark.esabot;

import net.aslettemark.esabot.command.CommandExecutor;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import java.io.*;
import java.util.ArrayList;

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
        if (this.bot.herders.contains(nick)) {
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
        } catch (final NickAlreadyInUseException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final IrcException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads unread notes from the note file.
     */
    public void loadNotes() {
        final String fileName = this.bot.files.get("notes");
        String line = null;
        try {
            final FileReader reader = new FileReader(fileName);
            final BufferedReader buffer = new BufferedReader(reader);
            String target = "";
            final ArrayList<String> file = new ArrayList<String>();
            while ((line = buffer.readLine()) != null) {
                file.add(line);
            }
            for (int i = 0; i < file.size(); i++) {
                line = file.get(i);
                if (!line.startsWith("  ")) {
                    target = line.replace(System.getProperty("line.separator"), "");
                    this.bot.notes.put(target, new ArrayList<String>());
                }
            }
            for (int i = 0; i < file.size(); i++) {
                line = file.get(i);
                if (line.startsWith("  ")) {
                    this.bot.notes.get(target).add(line.replaceFirst("  ", ""));
                } else {
                    target = line.replace(System.getProperty("line.separator"), "");
                }
            }
            buffer.close();
            reader.close();
        } catch (final FileNotFoundException ex) {
            System.out.println("Not able to open file " + fileName);
        } catch (final IOException e) {
            System.out.println("Encountered an error reading file " + fileName);
        }
    }

    /**
     * Updates the note savefile
     */
    public void saveNotes() {
        final String fileName = this.bot.files.get("notes");
        try {
            final FileWriter fileWriter = new FileWriter(fileName);
            final BufferedWriter buffer = new BufferedWriter(fileWriter);
            for (final String target : this.bot.notes.keySet()) {
                buffer.write(target);
                buffer.newLine();
                for (final String note : this.bot.notes.get(target)) {
                    buffer.write("  " + note);
                    buffer.newLine();
                }
            }
            buffer.close();
            fileWriter.close();
        } catch (final IOException e) {
            System.out.println("Error writing to file " + fileName);
        }
    }

    /**
     * Verifies that all needed files exist, and creates them if not
     */
    public void checkFiles() {
        final String network = this.bot.network.replace(".", "-");
        final File notes = new File("data/" + network + "/notes.txt");
        final File data = new File("data");
        final File net = new File("data/" + network);
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
            } catch (final IOException e) {
                System.out.println("Was not able to create notes.txt for " + network);
                e.printStackTrace();
            }
        }
        this.bot.files.put("notes", net + "/notes.txt");
    }

    /**
     * Assigns a command to the specified CommandExecutor
     *
     * @param command Command to assign
     * @param exec    CommandExecutor to assign to
     */
    public void assignCommand(String command, CommandExecutor exec) {
        this.bot.commandExecutors.put(command, exec);
    }

    /**
     * @param nick The nick to check
     * @return Wether the specified nick has any unread notes or not
     */
    public boolean hasNotes(String nick) {
        if (this.bot.notes.containsKey(nick)) {
            return true;
        }
        return false;
    }

    /**
     * Identifies with NickServ
     */
    public void nickservAuth() {
        this.bot.sendMessage("NickServ", "identify " + this.bot.nickpass);
    }

}
