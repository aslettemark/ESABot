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
