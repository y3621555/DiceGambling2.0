package com.johnson.dicegambling.Commands;

import com.johnson.dicegambling.Config.ConfigManager;
import com.johnson.dicegambling.Dicegambling;
import com.johnson.dicegambling.Timer.OpenDice;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DiceCommands implements CommandExecutor {
    OpenDice openDice;
    ConfigManager configManager;

    public DiceCommands(Dicegambling i){
        configManager = new ConfigManager(i);
        openDice = new OpenDice(i);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){

            Player p = (Player) sender;

            if ( args.length == 0 ) {
                p.sendMessage(this.configManager.getConfig().getString("use"));
                return true;
            }
            else if ( args.length == 1 && args[0].equalsIgnoreCase("test")){

                p.sendMessage("測試有效");
                p.sendMessage("冷卻 " + openDice.getCountDown());
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
}
