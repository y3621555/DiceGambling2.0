package com.johnson.dicegambling.Timer;

import com.johnson.dicegambling.Config.ConfigManager;
import com.johnson.dicegambling.Dicegambling;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class OpenDice extends BukkitRunnable {

    private final int originalCountdown;
    private int countdown;

    boolean aBoolean ;
    ConfigManager configManager;

    public OpenDice(Dicegambling i){
        configManager = new ConfigManager(i);
        originalCountdown = this.configManager.getConfig().getInt("DiceCooldown");
        countdown = originalCountdown;
    }

    @Override
    public void run() {
        Bukkit.broadcastMessage(getCountDown());

        if (countdown == 60){
            Bukkit.broadcastMessage("60s");
            Bukkit.broadcastMessage(String.valueOf(countdown));
        }
        else if (countdown == 30){
            Bukkit.broadcastMessage("30s");
        }
        else if (countdown == 10){
            Bukkit.broadcastMessage("10s");
        }
        if (countdown == 0 ){
            //開骰
            Bukkit.broadcastMessage("開骰");
            countdown = originalCountdown;
        }
        countdown--;
    }

    public String getCountDown(){
        int cooldown = countdown;

        int h = cooldown / 3600;
        cooldown = cooldown - (h*3600);
        int m = cooldown / 60;
        cooldown = cooldown - (m*60);
        int s = cooldown;

        return h + " h " + m + " m " + s + " s ";
    }
}
