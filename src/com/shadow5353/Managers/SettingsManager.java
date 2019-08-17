package com.shadow5353.Managers;

import com.shadow5353.StaffNotes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class SettingsManager {
    private File file;
    private FileConfiguration config;

    private static SettingsManager notes = new SettingsManager();

    public static SettingsManager getNotes() {
        return notes;
    }

    private String filename = "notes";

    public void setup() {
        System.out.println(StaffNotes.getPlugin());

        if (!StaffNotes.getPlugin().getDataFolder().exists()) StaffNotes.getPlugin().getDataFolder().mkdir();

        file = new File(StaffNotes.getPlugin().getDataFolder(),  filename + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

    }

    private SettingsManager() {

    }

    public void set(String path, Object value) {
        config.set(path, value);
        try { config.save(file); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public ConfigurationSection createConfigurationSection(String path) {
        ConfigurationSection cs = config.createSection(path);
        try { config.save(file); }
        catch (Exception e) { e.printStackTrace(); }
        return cs;
    }

    public boolean removePath(String path) {
        config.set(path, null);

        try {
            config.save(file);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) config.get(path);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }
}