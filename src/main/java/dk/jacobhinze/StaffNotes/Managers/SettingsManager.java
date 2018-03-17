package dk.jacobhinze.StaffNotes.Managers;

import java.io.File;

import dk.jacobhinze.StaffNotes.StaffNotes;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SettingsManager {
    private File file;
    private FileConfiguration config;

    private static SettingsManager notes = new SettingsManager(StaffNotes.getPlugin().getConfig().get("filename").toString());

    public static SettingsManager getNotes() {
        return notes;
    }

    private SettingsManager(String fileName) {
        System.out.println(StaffNotes.getPlugin());

        if (!StaffNotes.getPlugin().getDataFolder().exists()) StaffNotes.getPlugin().getDataFolder().mkdir();

        file = new File(StaffNotes.getPlugin().getDataFolder(), fileName + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
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

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) config.get(path);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }
}