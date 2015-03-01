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

import net.aslettemark.esabot.config.Config;
import net.aslettemark.esabot.config.IRCConfig;
import net.aslettemark.esabot.ESABot;

import java.io.*;
import java.util.ArrayList;

public class ConfigHandler {

    private ESABot bot;
    private Config config;
    private File configFile;

    public ConfigHandler(ESABot bot, Config config) {
        this.bot = bot;
        this.config = config;
        this.configFile = config.getFile();
    }

    public void checkFile() throws IOException {
        if(!configFile.exists()) {
            File folder = new File("config");
            if(!folder.exists()) {
                folder.mkdir();
                System.out.println("Created config folder");
            }
            configFile.createNewFile();
            System.out.println("Created config file");
            this.bot.files.put("config", config.getFile().getCanonicalPath());
            this.config.populate();
        } else {
            //
        }
    }

    public void reWrite(String content) {
        final String fileName = this.bot.files.get("config");
        try {
            final FileWriter fileWriter = new FileWriter(fileName);
            final BufferedWriter buffer = new BufferedWriter(fileWriter);
            buffer.write(content);
            buffer.close();
            fileWriter.close();
        } catch (final IOException e) {
            System.out.println("Error writing to file " + fileName);
        }
    }

    public ArrayList<String> readFile() throws IOException {
        final String fileName = this.configFile.getCanonicalPath();
        String line;
        try {
            final FileReader reader = new FileReader(fileName);
            final BufferedReader buffer = new BufferedReader(reader);
            final ArrayList<String> file = new ArrayList<String>();
            while ((line = buffer.readLine()) != null) {
                file.add(line);
            }
            buffer.close();
            reader.close();
            return file;
        } catch (final FileNotFoundException ex) {
            System.out.println("Not able to open file " + fileName);
        } catch (final IOException e) {
            System.out.println("Encountered an error reading file " + fileName);
        }
        return null;
    }

}
