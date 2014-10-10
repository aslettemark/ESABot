package net.aslettemark.esabot.command;

public interface CommandExecutor {

    /**
     * Function that is called when the corresponding command is used
     *
     * @param channel  The channel the command was executed in, if in pm channel = null
     * @param sender   The nickname of the command executor
     * @param login    The login of the command executor
     * @param hostname The hostname of the command executor
     * @param command  The command with removed prefixes
     * @param pm       Wether the command was called in a PM
     */
    public abstract void execute(String channel, String sender, String login, String hostname, String command, boolean pm);

}
