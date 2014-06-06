package net.aslettemark.esabot.util;

public class StringUtils {

    /**
     * Function to combine a String[] into a String
     * 
     * @param startIndex
     *            The index of String[] to start with
     * @param string
     *            The String[] you wish to combine
     * @param separator
     *            The separator to fill in between each object in String[]
     * @return
     */
    public static String combineSplit(int startIndex, String[] string, String separator) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < string.length; i++) {
            builder.append(string[i]);
            builder.append(separator);
        }
        builder.setLength(builder.length() - separator.length());
        return builder.toString();
    }

}
