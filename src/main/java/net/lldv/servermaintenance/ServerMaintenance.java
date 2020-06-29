package net.lldv.servermaintenance;

import cn.nukkit.plugin.PluginBase;
import net.lldv.servermaintenance.commands.MaintenanceCommand;
import net.lldv.servermaintenance.components.managers.MaintenanceManager;
import net.lldv.servermaintenance.components.tools.Language;
import net.lldv.servermaintenance.listeners.EventListener;

public class ServerMaintenance extends PluginBase {

    private static ServerMaintenance instance;

    @Override
    public void onEnable() {
        instance = this;
        try {
            saveDefaultConfig();
            Language.init();
            MaintenanceManager.setMaintenance(getConfig().getBoolean("Maintenance"));
            MaintenanceManager.setAnnouncements(getConfig().getIntegerList("Announcements"));
            getServer().getPluginManager().registerEvents(new EventListener(), this);
            getServer().getCommandMap().register(getConfig().getString("Commands.Maintenance"), new MaintenanceCommand(getConfig().getString("Commands.Maintenance")));
            getLogger().info("§aServerMaintenance successfully started.");
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().error("§4Failed to load ServerMaintenance.");
        }
    }

    public static ServerMaintenance getInstance() {
        return instance;
    }
}
