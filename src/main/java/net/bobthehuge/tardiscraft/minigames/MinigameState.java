package net.bobthehuge.tardiscraft.minigames;

public enum MinigameState {
    LOBBY,
    PLAYING,
    ENDING,
    DESTROYING;

    public MinigameState getGameState(String state) {
        switch (state) {
            case "LOBBY": return LOBBY;
            case "PLAYING": return PLAYING;
            case "ENDING": return ENDING;
            default: return null;
        }
    }
}
