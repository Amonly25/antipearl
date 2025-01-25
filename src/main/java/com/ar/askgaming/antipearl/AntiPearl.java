package com.ar.askgaming.antipearl;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiPearl extends JavaPlugin implements Listener {
    
    public void onEnable() {
        
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginCommand("antipearl").setExecutor(this);

    }

    private String msg = "§cTeleport cancelled, prevent pearl exploit.";

    @EventHandler
    public void onEntityTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("antipearl.bypass")) return;

            if (event.getCause() == TeleportCause.ENDER_PEARL){
                Location loc = event.getTo();

            for (String worldName : getConfig().getStringList("disabled_worlds")) {
                if (loc.getWorld().getName().equalsIgnoreCase(worldName)) {
                    return;
                }
            }

            if (loc.getBlock().getType().isSolid() || loc.getBlock().getRelative(0, 1, 0).getType().isSolid()) {
                player.sendMessage(getConfig().getString("msg", msg).replace("&", "§"));
                event.setCancelled(true);

            }
        }
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