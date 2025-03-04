package com.ar.askgaming.antipearl;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class PlayerTeleportListener implements Listener{

    private AntiPearl plugin;
    public PlayerTeleportListener(AntiPearl plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
    
        if (player.hasPermission("antipearl.bypass")) return;
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
    
        Location from = event.getFrom();
        Location to = event.getTo();
    
        // Verificar si el mundo está en la lista de mundos deshabilitados
        List<String> disabledWorlds = plugin.getConfig().getStringList("disabled_worlds");
        if (disabledWorlds.contains(to.getWorld().getName())) return;
    
        Block firstBlock = to.getBlock();
        Block upperBlock = firstBlock.getRelative(0, 1, 0);
    
        // Verificar si el material del bloque está en la lista negra
        List<String> blacklistMaterials = plugin.getConfig().getStringList("blacklist_materials");
        for (String mat : blacklistMaterials) {
            if (firstBlock.getType().toString().replace("_", "").contains(mat)) return;
        }
    
        // Calcular la dirección del TP y el siguiente bloque en esa dirección
        Vector direction = to.toVector().subtract(from.toVector()).normalize();
        Location nextLocation = to.clone().add(direction);
        Block nextBlock = nextLocation.getBlock();
        Block nextAboveBlock = nextBlock.getRelative(0, 1, 0);
    
        // Verificar si el jugador se teletransporta dentro o sobre bloques de vidrio
        if (nextBlock.getType().toString().contains("GLASS") || nextAboveBlock.getType().toString().contains("GLASS")) {
            player.sendMessage(plugin.getConfig().getString("msg").replace("&", "§"));
            event.setCancelled(true);
            return;
        }
    
        // Bloquear el teletransporte si el bloque de destino o el superior son sólidos
        if (firstBlock.getType().isSolid() || upperBlock.getType().isSolid()) {
            player.sendMessage(plugin.getConfig().getString("msg").replace("&", "§"));
            event.setCancelled(true);
        }
    }
}
    