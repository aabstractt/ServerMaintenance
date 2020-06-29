package net.lldv.servermaintenance.components.managers;

import cn.nukkit.Server;
import cn.nukkit.scheduler.TaskHandler;
import lombok.Getter;
import lombok.Setter;
import net.lldv.servermaintenance.ServerMaintenance;
import net.lldv.servermaintenance.components.tools.Language;

import java.util.List;

public class MaintenanceManager {

    private static final ServerMaintenance instance = ServerMaintenance.getInstance();

    @Getter
    @Setter
    private static boolean maintenance;
    @Getter
    @Setter
    private static int scheduleTime;
    @Getter
    @Setter
    private static List<Integer> announcements;
    public static TaskHandler taskHandler;

    public static void startMaintenanceCountdown(int time) {
        setScheduleTime(time * 60);
        taskHandler = Server.getInstance().getScheduler().scheduleRepeatingTask(ServerMaintenance.getInstance(), () -> {
            if (scheduleTime == 0) {
                Server.getInstance().broadcastMessage(Language.get("maintenance-active"));
                setMaintenance(true);
                instance.getConfig().set("Maintenance", true);
                instance.getConfig().save();
                instance.getConfig().reload();
                taskHandler.cancel();
                taskHandler = null;
                Server.getInstance().getOnlinePlayers().values().forEach(player -> {
                    if (!player.hasPermission("maintenance.join")) {
                        player.kick(Language.getNP("maintenance-kick"), false);
                    }
                });
                return;
            }
            announcements.forEach(i -> {
                if (i == scheduleTime) {
                    if (i > 1) Server.getInstance().broadcastMessage(Language.get("countdown-pl", scheduleTime));
                    else Server.getInstance().broadcastMessage(Language.get("countdown-sg", scheduleTime));
                }
            });
            scheduleTime--;
        }, 20);
    }

}
