package com.johnson.dicegambling.Config;

import com.johnson.dicegambling.Dicegambling;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigManager {
    private Dicegambling plugin;
    private FileConfiguration Config = null;
    private File configFile = null;

    public ConfigManager(Dicegambling plugin){
        this.plugin = plugin;

        saveDefaultConfig();
    }

    public void reloadConfig(){
        if (this.configFile == null){
            this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        }
        this.Config = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("config.yml");
        if (defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.Config.setDefaults(defaultConfig);
        }
    }

    public  FileConfiguration getConfig(){
        if (this.Config == null){
            reloadConfig();
        }
        return  this.Config;
    }

    public void saveConfig(){
        if (this.Config == null || this.configFile == null)
            return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Cant not save config to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig(){
        if (this.configFile == null){
            this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        }

        if (!this.configFile.exists()){
            this.plugin.saveResource("config.yml", false);
        }
    }
}
