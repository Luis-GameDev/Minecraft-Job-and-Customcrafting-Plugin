import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class CraftItemListener implements Listener {

    private final XPManager xpManager;
    private final RecipeLoader loader;
    private final BossbarManager bossbarManager;
    private final Plugin plugin;

    public CraftItemListener(XPManager xpManager, RecipeLoader loader, BossbarManager bossbarManager, Plugin plugin) {
        this.xpManager = xpManager;
        this.loader = loader;
        this.bossbarManager = bossbarManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack result = event.getRecipe().getResult();
        if (result == null || !result.hasItemMeta()) return;

        ItemMeta meta = result.getItemMeta();
        if (meta == null) return;

        NamespacedKey key = new NamespacedKey(plugin, "custom_item");
        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (id == null) return;

        CustomItemRecipe recipe = loader.loadedRecipes.get(id);
        if (recipe == null || recipe.rewardXp <= 0) return;

        JobType job = xpManager.getActiveJob(player.getUniqueId());
        if (job != recipe.requiredJob) return;

        // XP hinzufügen
        xpManager.addXp(player.getUniqueId(), recipe.rewardXp);

        // XP-Leiste & Bossbar aktualisieren
        double currentXp = xpManager.getXp(player.getUniqueId());
        int currentLevel = xpManager.getLevel(player.getUniqueId());
        double currentLevelXp = xpManager.getXpForLevel(currentLevel);
        double nextLevelXp = xpManager.getXpForLevel(currentLevel + 1);

        bossbarManager.update(player, job, currentXp, currentLevelXp, nextLevelXp, currentLevel);
    }
}
