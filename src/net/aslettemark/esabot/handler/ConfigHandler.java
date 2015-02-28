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
