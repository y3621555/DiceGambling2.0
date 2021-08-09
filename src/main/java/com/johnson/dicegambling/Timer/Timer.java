package com.johnson.dicegambling.Timer;

import com.johnson.dicegambling.Config.ConfigManager;
import com.johnson.dicegambling.Dicegambling;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {
    private int originalCountdown;
    public int countdown;
    ConfigManager configManager;


    public Timer(Dicegambling i){
        configManager = new ConfigManager(i);
        originalCountdown = this.configManager.getConfig().getInt("DiceCooldown");
        countdown = originalCountdown;
    }



    @Override
    public void run() {
        //Bukkit.broadcastMessage(getTime());

        if (countdown == 60){
            Bukkit.broadcastMessage(
                    this.configManager.getConfig().getString("prefix").replace("&","§") +
                    " " +
                    this.configManager.getConfig().getString("message.dicecountdown.60s")
            );
        }
        else if (countdown == 30){
            Bukkit.broadcastMessage(
                    this.configManager.getConfig().getString("prefix").replace("&","§") +
                    " " +
                    this.configManager.getConfig().getString("message.dicecountdown.30s"));
        }
        else if (countdown == 10){
            Bukkit.broadcastMessage(
                    this.configManager.getConfig().getString("prefix").replace("&","§") +
                    " " +
                    this.configManager.getConfig().getString("message.dicecountdown.10s")
            );
        }
        if (countdown == 0 ){
            //開骰
            Bukkit.broadcastMessage(
                    this.configManager.getConfig().getString("prefix").replace("&","§") +
                    " " +
                    this.configManager.getConfig().getString("message.dicecountdown.opendice")
            );
            countdown = originalCountdown;
        }
        countdown--;
    }


    public String getCountDown(){
        long cooldown = (long) countdown;

        int h = (int) cooldown / 3600;
        cooldown = cooldown - (h*3600);
        int m = (int) cooldown / 60;
        cooldown = cooldown - (m*60);
        int s = (int) cooldown;

        return h + " h " + m + " m " + s + " s ";
    }
}
