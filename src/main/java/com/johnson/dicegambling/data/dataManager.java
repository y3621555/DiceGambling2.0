package com.johnson.dicegambling.data;

import com.johnson.dicegambling.Config.ConfigManager;
import com.johnson.dicegambling.Dicegambling;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class dataManager {
    ConfigManager configManager;
    Dicegambling dicegambling;
    public List<Entry> entries = new ArrayList<Entry>();

    public dataManager(Dicegambling i){
        this.configManager = new ConfigManager(i);
        this.dicegambling = i;
    }
    public class Entry{
        String name;
        String type;
        UUID uuid;
        double value;
    }

    public void getHis(Player p){
        UUID uid = p.getUniqueId();
        Economy economy = dicegambling.getEconomy();
        int i=0;
        p.sendMessage(this.configManager.getConfig().getString("message.His.Title").replace("&", "§"));
        for ( Entry e : entries){
            if ( uid == e.uuid){
                p.sendMessage(
                        this.configManager.getConfig().getString("message.His.BetHis")
                        .replace("{PLAYER}", e.name)
                        .replace("{TYPE}", e.type)
                        .replace("{MONEY}", economy.format(e.value))
                        .replace("&", "§")
                );
                ++i;
            }
        }
        if (i <= 0){
            p.sendMessage(
                    this.configManager.getConfig().getString("prefix").replace("&","§") +
                    " " +
                    this.configManager.getConfig().getString("message.His.NotBet").replace("&", "§"));
        }
        else {
            i=0;
        }
    }


    public List<String> getType(){
        List<String> BIG = this.configManager.getConfig().getStringList("Type.1/3Rate.big");
        List<String> MID = this.configManager.getConfig().getStringList("Type.1/3Rate.mid");
        List<String> SMALL = this.configManager.getConfig().getStringList("Type.1/3Rate.small");
        List<String> SINGLE = this.configManager.getConfig().getStringList("Type.1/2Rate.single");
        List<String> EVEN = this.configManager.getConfig().getStringList("Type.1/2Rate.even");

        List<String> total = new ArrayList<>();
        for (String l : BIG){
            total.add(l);
        }
        for (String l : MID){
            total.add(l);
        }
        for (String l : SMALL){
            total.add(l);
        }
        for (String l : SINGLE){
            total.add(l);
        }
        for (String l : EVEN){
            total.add(l);
        }
        return total;
    }

    public void EnterBet(Player player, String type, double amt){
        Economy economy = dicegambling.getEconomy();
        economy.withdrawPlayer(player, amt);
        String name = player.getDisplayName();
        UUID PlayerUUID = player.getUniqueId();

        AddData(PlayerUUID, name ,type,amt);
        player.sendMessage(
                this.configManager.getConfig().getString("prefix").replace("&","§") +
                        " " +
                this.configManager.getConfig().getString("message.BetTrue").replace("&","§")
        );
    }



    public void AddData(UUID player_uuid,String name ,String type, double value){
        Entry e = new Entry();
        e.name = name;
        e.uuid = player_uuid;
        e.type = type;
        e.value = value;
        entries.add(e);
    }

    public void OpenDice(){
        if (!(entries.isEmpty())){

        }
    }
}
