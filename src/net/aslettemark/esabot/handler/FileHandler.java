package net.aslettemark.esabot.handler;

import net.aslettemark.esabot.ESABot;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private ESABot bot;

    public FileHandler(ESABot b) {
        this.bot = b;
    }

    /**
     * Loads unread notes from the note file.
     */
    public void loadNotes() {
        final String fileName = this.bot.files.get("notes");
        String line;
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

}
