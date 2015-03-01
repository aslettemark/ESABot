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
