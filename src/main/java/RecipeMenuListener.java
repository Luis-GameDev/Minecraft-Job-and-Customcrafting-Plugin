import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.NamespacedKey;

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

        if (title.startsWith("§8Recipes for")) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            String displayName = clicked.getItemMeta().getDisplayName();

            if (displayName.equals("§7« Back")) {
                int currentPage = RecipeOverviewGUI.getCurrentPage(player);
                RecipeOverviewGUI.open(player, loader, xpManager, currentPage - 1);
                return;
            }

            if (displayName.equals("§7Next »")) {
                int currentPage = RecipeOverviewGUI.getCurrentPage(player);
                RecipeOverviewGUI.open(player, loader, xpManager, currentPage + 1);
                return;
            }

            ItemMeta meta = clicked.getItemMeta();
            if (meta.getPersistentDataContainer().has(
                    new NamespacedKey(plugin, "custom_item"), PersistentDataType.STRING)) {

                String id = meta.getPersistentDataContainer().get(
                        new NamespacedKey(plugin, "custom_item"), PersistentDataType.STRING);

                CustomItemRecipe recipe = loader.loadedRecipes.get(id);
                if (recipe != null) {
                    RecipeViewerGUI.open(player, recipe, loader, xpManager);
                }
            }

        } else if (title.equals("§8Recipe Preview")) {
            event.setCancelled(true);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() != Material.ARROW) return;

            RecipeOverviewGUI.open(player, loader, xpManager);
        }
    }
}