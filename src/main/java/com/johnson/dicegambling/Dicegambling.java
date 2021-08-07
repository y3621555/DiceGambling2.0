package com.johnson.dicegambling;


import com.johnson.dicegambling.Config.ConfigManager;
import com.johnson.dicegambling.Timer.OpenDice;
import com.johnson.dicegambling.message.MessageHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public final class Dicegambling extends JavaPlugin implements CommandExecutor {
    public ConfigManager configManager;
    public OpenDice openDice;
    public MessageHandler messageHandler;
    private Economy econ = null;


    @Override
    public void onEnable() {
        // Plugin startup logic
        this.configManager = new ConfigManager(this);
        this.messageHandler = new MessageHandler(this);

        if (!setupEconomy() ) {
            System.out.println(this.configManager.getConfig().getString("message.NoInstallEconomy"));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        getCommand("dice").setExecutor(this);

        //Get use = true run taskTimer
        openDice = new OpenDice(this);
        if (this.configManager.getConfig().getBoolean("use")){
            openDice.runTaskTimer(this, 0L, 20L);
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
            if ( args.length == 0 ) {

                for ( String line : messageHandler.getRule()){
                    p.sendMessage(line);
                }
                return true;
            }
            else if ( args.length == 1 && args[0].equalsIgnoreCase("test")){
                p.sendMessage("冷卻 " + openDice.getCountDown() );
                return true;
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
