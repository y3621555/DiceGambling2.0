package com.johnson.dicegambling;


import com.johnson.dicegambling.Config.ConfigManager;
import com.johnson.dicegambling.Timer.Timer;
import com.johnson.dicegambling.data.dataManager;
import com.johnson.dicegambling.message.MessageHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public final class Dicegambling extends JavaPlugin implements CommandExecutor {
    public ConfigManager configManager;
    public Timer timer;
    public MessageHandler messageHandler;
    public dataManager dataManager;
    private Economy econ = null;



    @Override
    public void onEnable() {
        // Plugin startup logic
        this.configManager = new ConfigManager(this);
        this.messageHandler = new MessageHandler(this);
        this.dataManager = new dataManager(this);

        if (!setupEconomy() ) {
            System.out.println(this.configManager.getConfig().getString("message.NoInstallEconomy"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        getCommand("dice").setExecutor(this);

        //Get use = true run taskTimer
        timer = new Timer(this);
        if (this.configManager.getConfig().getBoolean("use")){
            timer.runTaskTimer(this, 0L, 20L);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){

            Player p = (Player) sender;
            Economy economy = getEconomy();
            double PlayerBank = economy.getBalance(p);

            if ( args.length == 0 ) {
                return true;
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("help")){
                for ( String line : messageHandler.getRule()){
                    p.sendMessage(line);
                }
                return true;
            }
            else if ( args.length == 3 && args[0].equalsIgnoreCase("bet")){
                List<String> type = dataManager.getType();
                String playerType = args[1];
                boolean isNum = args[2].matches("[0-9]+");
                if ( type.contains(playerType)){
                    //下注類型正確
                    if ( isNum ){
                        //輸入的金額是數字
                        double BetMoney = Double.parseDouble(args[2]);
                        double Tack_Money = PlayerBank - BetMoney;
                        if (BetMoney >= this.configManager.getConfig().getDouble("Min") && BetMoney <= this.configManager.getConfig().getDouble("Max")){
                            //金額正確
                            if ( Tack_Money >= 0){
                                //庫存的錢夠
                                dataManager.EnterBet(p,playerType,BetMoney);
                            }
                            else {
                                //餘額不足
                                p.sendMessage(
                                        this.configManager.getConfig().getString("prefix").replace("&","§") +
                                                " " +
                                                this.configManager.getConfig().getString("message.InsufficientBalance").replace("&","§")
                                );
                            }
                        }
                        else{
                            //金額錯誤
                            p.sendMessage(
                                    this.configManager.getConfig().getString("prefix").replace("&","§") +
                                            " " +
                                            this.configManager.getConfig().getString("message.BetMoneyError").replace("&","§")
                            );
                        }
                    }
                    else{
                        //輸入的金額不是數字
                        p.sendMessage(
                                this.configManager.getConfig().getString("prefix").replace("&","§") +
                                " " +
                                this.configManager.getConfig().getString("message.BetError").replace("&","§")
                        );
                    }
                }
                else {
                    //下注類型錯誤
                    p.sendMessage(
                            this.configManager.getConfig().getString("prefix").replace("&","§") +
                            " " +
                            this.configManager.getConfig().getString("message.BetError").replace("&","§")
                    );
                }

                return true;
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("cooldown")){
                p.sendMessage(
                        this.configManager.getConfig().getString("prefix").replace("&","§") +
                         " " +
                        timer.getCountDown()
                        );
            }
            else if (args.length ==1 && args[0].equalsIgnoreCase("his")){
                dataManager.getHis(p);
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("reload") && p.isOp()){
                this.configManager.reloadConfig();
                p.sendMessage(ChatColor.RED + "Reload Finish");
                return true;
            }
        }

        return true;
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

}
