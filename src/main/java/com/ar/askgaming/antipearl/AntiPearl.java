package com.ar.askgaming.antipearl;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiPearl extends JavaPlugin{
    
    public void onEnable() {
        
        saveDefaultConfig();
        getServer().getPluginCommand("antipearl").setExecutor(this);

        new PlayerTeleportListener(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) return false;

        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
        }
        return true;
        
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("reload");
        }
        return null;
    }
    
}