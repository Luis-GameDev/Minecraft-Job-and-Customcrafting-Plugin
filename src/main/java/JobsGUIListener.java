import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class JobsGUIListener implements Listener {

    private final XPManager xpManager;

    public JobsGUIListener(XPManager xpManager) {
        this.xpManager = xpManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (!event.getView().getTitle().equals("Wähle deinen Job")) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;

        Material clicked = event.getCurrentItem() != null ? event.getCurrentItem().getType() : null;
        if (clicked == null) return;

        switch (clicked) {
            case WHEAT -> {
                xpManager.setJob(player.getUniqueId(), JobType.FARMER);
                player.sendMessage("§eDu bist jetzt ein Farmer.");
            }
            case IRON_PICKAXE -> {
                xpManager.setJob(player.getUniqueId(), JobType.MINER);
                player.sendMessage("§7Du bist jetzt ein Miner.");
            }
            case IRON_AXE -> {
                xpManager.setJob(player.getUniqueId(), JobType.LUMBERJACK);
                player.sendMessage("§aDu bist jetzt ein Lumberjack.");
            }
            default -> {
                return;
            }
        }

        player.closeInventory();
    }
}
