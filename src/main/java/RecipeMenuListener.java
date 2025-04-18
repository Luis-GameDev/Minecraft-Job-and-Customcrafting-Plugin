import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class RecipeMenuListener implements Listener {

    private final RecipeLoader loader;
    private final XPManager xpManager;
    private final Plugin plugin;

    public RecipeMenuListener(RecipeLoader loader, XPManager xpManager, Plugin plugin) {
        this.loader = loader;
        this.xpManager = xpManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Inventory inv = event.getInventory();
        String title = event.getView().getTitle();

        if (title.startsWith("§8Rezepte für")) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            ItemMeta meta = clicked.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) return;

            String displayName = meta.getDisplayName();

            // Navigation
            if (displayName.equals("§7« Zurück")) {
                int currentPage = RecipeOverviewGUI.getCurrentPage(player);
                RecipeOverviewGUI.open(player, loader, xpManager, currentPage - 1);
                return;
            }

            if (displayName.equals("§7Weiter »")) {
                int currentPage = RecipeOverviewGUI.getCurrentPage(player);
                RecipeOverviewGUI.open(player, loader, xpManager, currentPage + 1);
                return;
            }

            // Detail-GUI öffnen
            NamespacedKey key = new NamespacedKey(plugin, "custom_item");
            if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;

            String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
            if (id == null) return;

            CustomItemRecipe recipe = loader.loadedRecipes.get(id);
            if (recipe != null) {
                RecipeViewerGUI.open(player, recipe, loader, xpManager);
            }

        } else if (title.equals("§8Rezeptvorschau")) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() != Material.ARROW) return;

            // Zurück zur vorherigen Übersicht
            RecipeOverviewGUI.open(player, loader, xpManager);
        }
    }
}