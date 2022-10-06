package com.github.tcn.plexi.utils.database.template;

public class User {

    private final int permissions;
    private final int overseerrID;
    private final String email;
    private final String plexUsername;
    private final String overseerrUsername;
    private final int userType;
    private final String avatarURL;
    private final String displayName;
    private final String discordID;
    private final boolean discordVerified;


    public User(int permissions, int overseerrID, String email, String plexUsername, String overseerrUsername, int userType, String avatarURL, String displayName, String discordID, boolean discordVerified) {
        this.permissions = permissions;
        this.overseerrID = overseerrID;
        this.email = email;
        this.plexUsername = plexUsername;
        this.overseerrUsername = overseerrUsername;
        this.userType = userType;
        this.avatarURL = avatarURL;
        this.displayName = displayName;
        this.discordID = discordID;
        this.discordVerified = discordVerified;
    }

    public int getPermissions() {
        return permissions;
    }

    public int getOverseerrID() {
        return overseerrID;
    }

    public String getEmail() {
        return email;
    }

    public String getPlexUsername() {
        return plexUsername;
    }

    public String getOverseerrUsername() {
        return overseerrUsername;
    }

    public int getUserType() {
        return userType;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDiscordID() {
        return discordID;
    }

    public boolean isDiscordVerified() {
        return discordVerified;
    }

}
