package net.bobthehuge.tardiscraft.playerinfos;

public enum PlayerRole {
    STANDARD,
    ADMIN,
    OP;

    public static PlayerRole getPlayerRole(String role) {
        switch (role) {
            case "standard": return PlayerRole.STANDARD;
            case "admin": return PlayerRole.ADMIN;
            case "op": return PlayerRole.OP;
            default: return null;
        }
    }
}
