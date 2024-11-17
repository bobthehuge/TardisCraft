package net.bobthehuge.tardiscraft.activities;

public enum WarpState {
    INIT,
    INGAME,
    ENDING;

    public static WarpState getWarpState(String state) {
        switch (state) {
            case "INIT": return INIT;
            case "INGAME": return INGAME;
            case "ENDING": return ENDING;
            default: return null;
        }
    }
}
