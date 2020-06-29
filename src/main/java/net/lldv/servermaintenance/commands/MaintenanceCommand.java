package net.lldv.servermaintenance.commands;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import net.lldv.servermaintenance.ServerMaintenance;
import net.lldv.servermaintenance.components.managers.MaintenanceManager;
import net.lldv.servermaintenance.components.tools.Language;

public class MaintenanceCommand extends Command {

    private final ServerMaintenance instance = ServerMaintenance.getInstance();

    public MaintenanceCommand(String name) {
        super(name);
        setDescription(instance.getConfig().getString("Commands.MaintenanceDescription"));
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender.hasPermission("maintenance.command")) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    boolean set = Boolean.parseBoolean(args[1]);
                    if (set == MaintenanceManager.isMaintenance()) {
                        sender.sendMessage(Language.get("maintenance-already-set", String.valueOf(set)));
                        return true;
                    }
                    MaintenanceManager.setMaintenance(set);
                    instance.getConfig().set("Maintenance", set);
                    instance.getConfig().save();
                    instance.getConfig().reload();
                    sender.sendMessage(Language.get("maintenance-set", String.valueOf(set)));
                    if (set) Server.getInstance().broadcastMessage(Language.get("maintenance-active"));
                    Server.getInstance().getOnlinePlayers().values().forEach(player -> {
                        if (!player.hasPermission("maintenance.join")) {
                            player.kick(Language.getNP("maintenance-kick"), false);
                        }
                    });
                } else if (args[0].equalsIgnoreCase("timer")) {
                    try {
                        int i = Integer.parseInt(args[1]);
                        if (i <= 0) {
                            sender.sendMessage(Language.get("invalid-time"));
                            return true;
                        }
                        if (MaintenanceManager.isMaintenance()) {
                            sender.sendMessage(Language.get("maintenance-already-set", String.valueOf(false)));
                            return true;
                        }
                        if (MaintenanceManager.taskHandler != null) {
                            sender.sendMessage(Language.get("already-timer"));
                            return true;
                        }
                        MaintenanceManager.startMaintenanceCountdown(i);
                        sender.sendMessage(Language.get("timer-started", i));
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Language.get("invalid-time"));
                    }
                } else sendHelp(sender);
            } else sendHelp(sender);
        } else sender.sendMessage(Language.get("no-permission"));
        return false;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Language.get("help-set", getName()));
        sender.sendMessage(Language.get("help-timer", getName()));
    }
}
