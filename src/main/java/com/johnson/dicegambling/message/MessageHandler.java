package com.johnson.dicegambling.message;

import com.johnson.dicegambling.Config.ConfigManager;
import com.johnson.dicegambling.Dicegambling;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    ConfigManager configManager;

    public MessageHandler(Dicegambling i){
        this.configManager = new ConfigManager(i);
    }

    public List<String> getRule(){
        List<String> rule = this.configManager.getConfig().getStringList("message.rule");
        List<String> Msg = new ArrayList<>();
        for ( String line:rule){
            line.replace("&", "§");
            Msg.add(line);
        }
        return Msg;
    }


}
