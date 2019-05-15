package com.shadow5353.Managers;

import java.io.File;
import java.io.IOException;

import com.shadow5353.StaffNotes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsManager {
    private File file, translationFile;
    private FileConfiguration config, translation;

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

        if(StaffNotes.getPlugin().getDataFolder().exists()) {
            translationFile = new File(StaffNotes.getPlugin().getDataFolder(), "messages.yml");

            if (!translationFile.exists()) {
                translationFile.getParentFile().mkdirs();
                StaffNotes.getPlugin().saveResource("messages.yml", false);
            }

            translation = new YamlConfiguration();

            try {
                translation.load(translationFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
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