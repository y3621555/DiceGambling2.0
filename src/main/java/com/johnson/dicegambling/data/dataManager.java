package com.johnson.dicegambling.data;

import com.johnson.dicegambling.Config.ConfigManager;
import com.johnson.dicegambling.Dicegambling;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class dataManager {
    ConfigManager configManager;
    Dicegambling dicegambling;
    public List<Entry> entries = new ArrayList<Entry>();

    public List<Entry> BetBig = new ArrayList<>();
    public List<Entry> BetMid = new ArrayList<>();
    public List<Entry> BetSmall = new ArrayList<>();
    public List<Entry> BetSingle = new ArrayList<>();
    public List<Entry> BetEven = new ArrayList<>();

    public dataManager(Dicegambling i){
        this.configManager = new ConfigManager(i);
        this.dicegambling = i;
    }

    public dataManager(dataManager dataManager, Dicegambling i){
        this.configManager = new ConfigManager(i);
        this.dicegambling = i;

        this.entries = dataManager.entries;
        this.BetBig = dataManager.BetBig;
        this.BetMid = dataManager.BetMid;
        this.BetSmall = dataManager.BetSmall;
        this.BetSingle = dataManager.BetSingle;
        this.BetEven = dataManager.BetEven;
    }

    public class Entry{
        String name;
        String type;
        UUID uuid;
        double value;
    }

    public void ClearList(){
        entries.clear();
        BetSmall.clear();
        BetMid.clear();
        BetBig.clear();
        BetSingle.clear();
        BetEven.clear();
    }

    public void SortBetPlayer(){
        List<String> BIG = this.configManager.getConfig().getStringList("Type.1/3Rate.big");
        List<String> MID = this.configManager.getConfig().getStringList("Type.1/3Rate.mid");
        List<String> SMALL = this.configManager.getConfig().getStringList("Type.1/3Rate.small");
        List<String> SINGLE = this.configManager.getConfig().getStringList("Type.1/2Rate.single");
        List<String> EVEN = this.configManager.getConfig().getStringList("Type.1/2Rate.even");
        Entry entry = new Entry();

        for (Entry e : entries){
            if (BIG.contains(e.type)){
                entry.name = e.name;
                entry.uuid = e.uuid;
                entry.type = e.type;
                entry.value = e.value;
                BetBig.add(entry);
            }
            else if (MID.contains(e.type)){
                entry.name = e.name;
                entry.uuid = e.uuid;
                entry.type = e.type;
                entry.value = e.value;
                BetMid.add(entry);
            }
            else if (SMALL.contains(e.type)){
                entry.name = e.name;
                entry.uuid = e.uuid;
                entry.type = e.type;
                entry.value = e.value;
                BetSmall.add(entry);
            }
            else if (SINGLE.contains(e.type)){
                entry.name = e.name;
                entry.uuid = e.uuid;
                entry.type = e.type;
                entry.value = e.value;
                BetSingle.add(entry);
            }
            else if (EVEN.contains(e.type)){
                entry.name = e.name;
                entry.uuid = e.uuid;
                entry.type = e.type;
                entry.value = e.value;
                BetEven.add(entry);
            }
        }

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
            SortBetPlayer();
            Economy economy = dicegambling.getEconomy();
            int DiceRandom=0;
            DiceRandom = (int)(Math.random()*6)+1;

            Bukkit.broadcastMessage(
                    this.configManager.getConfig().getString("prefix").replace("&","§") +
                    " " +
                    this.configManager.getConfig().getString("message.DiceNumber")
                    .replace("&","§")
                    .replace("{number}", String.valueOf(DiceRandom))
            );
            if (DiceRandom % 2 ==0){
                //雙數贏
                for (Entry entry : BetEven){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(entry.uuid);
                    double Bet_money = entry.value;
                    double Win_money = Bet_money * this.configManager.getConfig().getDouble("1/2Rate");;
                    Player p = player.getPlayer();
                    economy.depositPlayer(player, Win_money);
                    if ( player.isOnline()){
                        p.sendMessage(
                                this.configManager.getConfig().getString("prefix").replace("&","§") +
                                " " +
                                this.configManager.getConfig().getString("message.Winner").replace("&","§") +
                                " " +
                                economy.format(Win_money)
                        );
                    }
                }
            }
            else{
                //單數贏
                for (Entry entry : BetSingle){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(entry.uuid);
                    double Bet_money = entry.value;
                    double Win_money = Bet_money * this.configManager.getConfig().getDouble("1/2Rate");;
                    Player p = player.getPlayer();
                    economy.depositPlayer(player, Win_money);
                    if ( player.isOnline()){
                        p.sendMessage(
                                this.configManager.getConfig().getString("prefix").replace("&","§") +
                                        " " +
                                        this.configManager.getConfig().getString("message.Winner").replace("&","§") +
                                        " " +
                                        economy.format(Win_money)
                        );
                    }
                }
            }

            if ( DiceRandom <= 2){
                //DiceRandom == 1 or 2
                for (Entry entry : BetSmall){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(entry.uuid);
                    double Bet_money = entry.value;
                    double Win_money = Bet_money * this.configManager.getConfig().getDouble("1/3Rate");
                    Player p = player.getPlayer();
                    economy.depositPlayer(player, Win_money);
                    if ( player.isOnline()){
                        p.sendMessage(
                                this.configManager.getConfig().getString("prefix").replace("&","§") +
                                        " " +
                                        this.configManager.getConfig().getString("message.Winner").replace("&","§") +
                                        " " +
                                        economy.format(Win_money)
                        );
                    }
                }
            }
            else if (DiceRandom == 3 || DiceRandom == 4 ){
                //DiceRandom == 3 or 4
                for (Entry entry : BetMid){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(entry.uuid);
                    double Bet_money = entry.value;
                    double Win_money = Bet_money * this.configManager.getConfig().getDouble("1/3Rate");
                    Player p = player.getPlayer();
                    economy.depositPlayer(player, Win_money);
                    if ( player.isOnline()){
                        p.sendMessage(
                                this.configManager.getConfig().getString("prefix").replace("&","§") +
                                        " " +
                                        this.configManager.getConfig().getString("message.Winner").replace("&","§") +
                                        " " +
                                        economy.format(Win_money)
                        );
                    }
                }
            }
            else {
                //DiceRandom == 5 or 6
                for (Entry entry : BetBig){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(entry.uuid);
                    double Bet_money = entry.value;
                    double Win_money = Bet_money * this.configManager.getConfig().getDouble("1/3Rate");
                    Player p = player.getPlayer();
                    economy.depositPlayer(player, Win_money);
                    if ( player.isOnline()){
                        p.sendMessage(
                                this.configManager.getConfig().getString("prefix").replace("&","§") +
                                        " " +
                                        this.configManager.getConfig().getString("message.Winner").replace("&","§") +
                                        " " +
                                        economy.format(Win_money)
                        );
                    }
                }
            }

        }
        else{
            Bukkit.broadcastMessage(
                    this.configManager.getConfig().getString("prefix").replace("&","§") +
                    " " +
                    this.configManager.getConfig().getString("message.NoBet").replace("&","§")
            );
        }
        ClearList();
    }
}
