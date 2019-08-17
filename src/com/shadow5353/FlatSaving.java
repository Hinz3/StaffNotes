package com.shadow5353;

import com.shadow5353.Managers.SettingsManager;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jacob on 18-03-2018.
 */
public class FlatSaving {

    private FlatSaving() {}
    private static FlatSaving instance = new FlatSaving();

    public static FlatSaving getInstance() {
        return instance;
    }

    private ArrayList<Note> notes = new ArrayList<Note>();

    public void setupNotes() {
        SettingsManager.getNotes().setup();

        if (SettingsManager.getNotes().<ConfigurationSection>get("notes") == null) SettingsManager.getNotes().createConfigurationSection("notes");

        notes.clear();

        for (String key : SettingsManager.getNotes().<ConfigurationSection>get("notes").getKeys(false)) {
            notes.add(new Note(Integer.parseInt(key)));
        }
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void saveNote(String note, UUID playerUUID, UUID adminUUID, String date){
        int id = getNotes().size() + 1;

        SettingsManager.getNotes().createConfigurationSection("notes." + id);
        SettingsManager.getNotes().set("notes." + id + ".note", note);
        SettingsManager.getNotes().set("notes." + id + ".date", date);
        SettingsManager.getNotes().set("notes." + id + ".playerUUID", playerUUID.toString());
        SettingsManager.getNotes().set("notes." + id + ".adminUUID", adminUUID.toString());

        setupNotes();
    }

    public Note getNote(int id) {
         return new Note(id);
    }

    public boolean removeNote(UUID playerUUID, int id) {
        boolean found = false;

        if (getNote(id).getPlayerUUID().equals(playerUUID)) {
            found = true;

            SettingsManager.getNotes().removePath("notes." + id);

            setupNotes();
        }

        return found;

    }

    public boolean removeAllNotes(UUID playerUUID) {
        boolean found = false;

        for (Note note : getNotes()) {
            if (note.getPlayerUUID().equals(playerUUID)) {
                found = true;
                SettingsManager.getNotes().removePath("notes." + note.getId());
            }
        }

        setupNotes();

        return found;
    }

    public void reset() {
        SettingsManager.getNotes().removePath("notes");
        setupNotes();
    }

    public boolean hasNote(UUID playerUUID) {

        for (Note note : getNotes()) {
            if (playerUUID.equals(note.getPlayerUUID())) {
                return true;
            }
        }
        return false;
    }
}
