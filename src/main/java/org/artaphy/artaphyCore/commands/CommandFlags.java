package org.artaphy.artaphyCore.commands;

public class CommandFlags {
    public static final String ON = "-on";
    public static final String OFF = "-off";

    public static boolean hasOnFlag(String[] args) {
        return containsFlag(args, ON);
    }

    public static boolean hasOffFlag(String[] args) {
        return containsFlag(args, OFF);
    }

    private static boolean containsFlag(String[] args, String flag) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase(flag)) {
                return true;
            }
        }
        return false;
    }
}
