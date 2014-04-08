package me.aslettemark.esabot;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class IRCHandler {
	
	public ESABot bot;
	
	public IRCHandler(ESABot bot) {
		this.bot = bot;
	}
	
	public void command(String channel, String sender, String login, String hostname, String command, boolean verbose) {
		if(command.equalsIgnoreCase("kill")) {
			if(isHerder(sender)) {
				bot.sendMessage(channel, "I love you");
				bot.sendAction(channel, "is kill");
				try {
					Thread.sleep(1100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				bot.disconnect();
				bot.dispose();
			} else {
				bot.sendMessage(channel, "Not allowed");
			}
		}
		
		//Easter egg to be removed later
		if(command.equalsIgnoreCase("portal") && (sender.equalsIgnoreCase("Kermi") || this.isHerder(sender))) {
			bot.sendMessage(channel, "I'm doing Science and I'm still alive.");
			bot.sendMessage(channel, "I feel FANTASTIC and I'm still alive.");
			bot.sendMessage(channel, "While you're dying I'll be still alive.");
			bot.sendMessage(channel, "And when you're dead I will be still alive.");
			bot.sendMessage(channel, "STILL ALIVE");
			bot.sendMessage(channel, "STILL ALIVE");
		}
	}
	/*
	public boolean isOp(String nick, String channel) {
		if(bot.ops.contains(channel + ":" + nick)) {
			return true;
		}
		return false;
	}*/
	
	public boolean isHerder(String nick) {
		if(bot.herders.contains(nick)) {
			return true;
		}
		return false;
	}
	
	public void doConnect() {
		try {
			this.bot.connectWithNoB(this.bot.network, 6667, null);
		} catch (NickAlreadyInUseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IrcException e) {
			e.printStackTrace();
		}
	}

}
