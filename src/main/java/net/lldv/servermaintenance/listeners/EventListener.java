package net.lldv.servermaintenance.listeners;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import net.lldv.servermaintenance.components.managers.MaintenanceManager;
import net.lldv.servermaintenance.components.tools.Language;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerPreLoginEvent(PlayerPreLoginEvent ev) {
        if (MaintenanceManager.isMaintenance() && !ev.getPlayer().hasPermission("maintenance.join")) {
            ev.setKickMessage(Language.getNP("maintenance-kick"));

            ev.setCancelled(true);
        }
    }
}