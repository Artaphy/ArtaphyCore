package org.artaphy.artaphyCore.config;

import org.artaphy.artaphyCore.ArtaphyCore;
import org.artaphy.artaphyCore.utils.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LanguageManager {
    private static final Map<String, String> messages = new HashMap<>();
    private final ArtaphyCore plugin;
    private static FileConfiguration languageConfig;

    public LanguageManager(ArtaphyCore plugin) {
        this.plugin = plugin;
    }

    public void loadLanguage() {
        String language = plugin.getConfig().getString("language", "en");
        File langFolder = new File(plugin.getDataFolder(), "langs");
        File langFile = new File(langFolder, "locale-" + language + ".yml");

        if (!langFolder.exists()) {
            langFolder.mkdirs();
        }

        if (!langFile.exists()) {
            createLanguageFile(language);
        }

        languageConfig = YamlConfiguration.loadConfiguration(langFile);
        loadMessages();
    }

    private void createLanguageFile(String language) {
        File outFile = new File(plugin.getDataFolder() + "/langs", "locale-" + language + ".yml");
        InputStream inputStream = plugin.getResource("langs/locale-" + language + ".yml");

        if (inputStream == null) {
            plugin.getLogger().warning("Language file not found in resources: locale-" + language + ".yml");
            plugin.getLogger().info("Creating default English language file.");
            inputStream = plugin.getResource("langs/locale-en.yml");
        }

        if (inputStream == null) {
            plugin.getLogger().severe("Default language file not found in resources! Creating an empty file.");
            try (FileWriter writer = new FileWriter(outFile)) {
                writer.write("# Default language file for " + language);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create empty language file: " + e.getMessage());
            }
            return;
        }

        try (InputStream is = inputStream;
             FileOutputStream outputStream = new FileOutputStream(outFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Could not create language file: " + e.getMessage());
        }
    }

    private void loadMessages() {
        messages.clear();
        for (String key : languageConfig.getKeys(true)) {
            if (languageConfig.isString(key)) {
                messages.put(key, languageConfig.getString(key));
            }
        }
    }

    public static String get(String key) {
        return ColorUtil.colorize(messages.getOrDefault(key, ChatColor.RED + "Missing translation: " + key));
    }

    public static List<String> getList(String key) {
        List<String> list = languageConfig.getStringList(key);
        return list.stream().map(ColorUtil::colorize).collect(Collectors.toList());
    }
}
