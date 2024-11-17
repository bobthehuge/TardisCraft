package net.bobthehuge.tardiscraft.commands;

public enum CommandTarget {
    NONE,
    ME,
    OTHERS,
    ALL;

    public static CommandTarget getCommandTarget(String str) {
        return switch (str) {
            case "NONE" -> NONE;
            case "ME" -> ME;
            case "OTHERS" -> OTHERS;
            case "ALL" -> ALL;
            default -> null;
        };
    }
}
