import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class JobsCommand implements CommandExecutor {

    private final XPManager xpManager;

    public JobsCommand(XPManager xpManager) {
        this.xpManager = xpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen.");
            return true;
        }

        openJobSelectionGui(player);
        return true;
    }

    private void openJobSelectionGui(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "Wähle deinen Job");

        gui.setItem(11, createJobItem(Material.WHEAT, "§eFarmer"));
        gui.setItem(13, createJobItem(Material.IRON_PICKAXE, "§7Miner"));
        gui.setItem(15, createJobItem(Material.IRON_AXE, "§aLumberjack"));

        player.openInventory(gui);
    }

    private ItemStack createJobItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}
