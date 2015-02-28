package net.aslettemark.esabot.config;

import net.aslettemark.esabot.handler.ConfigHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Config {

    private ConfigHandler handler;
    private File file;

    public ConfigHandler getHandler() {
        return this.handler;
    }
    public File getFile() {
        return this.file;
    }

    public void setHandler(ConfigHandler handler) {
        this.handler = handler;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void populate() {

    }

    public void enforce() {

    }

    public ArrayList<String> read() {
        try {
            return this.getHandler().readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void interpretFile(ArrayList<String> file) {

    }
}
