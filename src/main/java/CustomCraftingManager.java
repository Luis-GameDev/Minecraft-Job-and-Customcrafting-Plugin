import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import java.util.Map;
import org.bukkit.plugin.Plugin;

public class CustomCraftingManager implements Listener {

    private final RecipeLoader loader;
    private final XPManager xpManager;
    private final Plugin plugin;

    public CustomCraftingManager(RecipeLoader loader, XPManager xpManager, Plugin plugin) {
        this.loader = loader;
        this.xpManager = xpManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getViewers().isEmpty() || !(event.getViewers().get(0) instanceof Player player)) return;

        ItemStack result = event.getInventory().getResult();
        if (result == null || !result.hasItemMeta()) return;

        ItemMeta meta = result.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "custom_item");
        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (id == null) return;

        CustomItemRecipe recipe = loader.loadedRecipes.get(id);
        if (recipe == null) return;

        JobType job = xpManager.getActiveJob(player.getUniqueId());
        int level = xpManager.getLevel(player.getUniqueId());

        if (job != recipe.requiredJob || level < recipe.requiredLevel) {
            event.getInventory().setResult(null);
        }
    }
}
