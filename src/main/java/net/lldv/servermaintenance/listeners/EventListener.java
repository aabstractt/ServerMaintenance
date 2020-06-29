package net.lldv.servermaintenance.listeners;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import net.lldv.servermaintenance.components.managers.MaintenanceManager;
import net.lldv.servermaintenance.components.tools.Language;

public class EventListener implements Listener {

    @EventHandler
    public void on(PlayerLoginEvent event) {
        if (MaintenanceManager.isMaintenance() && !event.getPlayer().hasPermission("maintenance.join")) {
            event.setKickMessage(Language.getNP("maintenance-kick"));
            event.setCancelled(true);
        }
    }

}
