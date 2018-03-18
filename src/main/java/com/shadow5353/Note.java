package com.shadow5353;

import com.shadow5353.Managers.SettingsManager;

import java.util.UUID;

/**
 * Created by Jacob on 18-03-2018.
 */
public class Note {
    private int id;
    private String note, date;
    private UUID playerUUID, adminUUID;

    public Note(int id) {
        this.id = id;

        this.note = SettingsManager.getNotes().get("notes." + id + ".note");
        this.date = SettingsManager.getNotes().get("notes." + id + ".date");
        this.playerUUID = SettingsManager.getNotes().get("notes." + id + ".playerUUID");
        this.adminUUID = SettingsManager.getNotes().get("notes." + id + ".adminUUID");
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public UUID getAdminUUID() {
        return adminUUID;
    }
}
