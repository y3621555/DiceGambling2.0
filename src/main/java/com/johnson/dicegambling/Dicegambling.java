package com.johnson.dicegambling;

import com.johnson.dicegambling.Commands.DiceCommands;
import com.johnson.dicegambling.Config.ConfigManager;
import com.johnson.dicegambling.Timer.OpenDice;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;



public final class Dicegambling extends JavaPlugin {
    public ConfigManager configManager;
    public OpenDice openDice;
    public JavaPlugin PLUGIN;



    @Override
    public void onEnable() {
        // Plugin startup logic
        this.configManager = new ConfigManager(this);

        PLUGIN = this;

        System.out.println("hello world Start");
        System.out.println (this.configManager.getConfig().getString("use"));

        getCommand("dice").setExecutor(new DiceCommands(this));

        openDice = new OpenDice(this);
        openDice.runTaskTimer(this, 0L, 20L);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getS(){
       return openDice.getCountDown();
    }
}
