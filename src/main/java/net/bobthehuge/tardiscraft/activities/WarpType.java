package net.bobthehuge.tardiscraft.activities;

public enum WarpType {
    HUB,
    BEDWARS;

    public static WarpType getWarpType(String type) {
        switch (type.toLowerCase()) {
            case "hub": return HUB;
            case "bedwars": return BEDWARS;
            default: return null;
        }
    }
}
