import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class RecipeOverviewGUI {

    private static final int ITEMS_PER_PAGE = 45;
    private static final Map<UUID, Integer> playerPageMap = new HashMap<>();

    public static void open(Player player, RecipeLoader loader, XPManager xpManager, int page) {
        JobType job = xpManager.getActiveJob(player.getUniqueId());
        if (job == null) {
            player.sendMessage("§cDu hast keinen Job gewählt.");
            return;
        }

        List<CustomItemRecipe> jobRecipes = new ArrayList<>(loader.loadedRecipes.values().stream()
                .filter(r -> r.requiredJob == job)
                .sorted(Comparator.comparingInt(r -> r.requiredLevel))
                .toList());

        int totalPages = (int) Math.ceil(jobRecipes.size() / (double) ITEMS_PER_PAGE);
        page = Math.max(0, Math.min(page, totalPages - 1));
        playerPageMap.put(player.getUniqueId(), page);

        Inventory gui = Bukkit.createInventory(null, 54, "§8Rezepte für " + job.name() + " §7Seite " + (page + 1));

        int level = xpManager.getLevel(player.getUniqueId());
        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, jobRecipes.size());

        for (int i = start; i < end; i++) {
            CustomItemRecipe recipe = jobRecipes.get(i);
            ItemStack displayItem;
            if (level >= recipe.requiredLevel) {
                displayItem = recipe.createDisplayItem();
            } else {
                displayItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta meta = displayItem.getItemMeta();
                meta.setDisplayName("§7§oErfordert Level " + recipe.requiredLevel);
                displayItem.setItemMeta(meta);
            }
            gui.setItem(i - start, displayItem);
        }

        // Navigation
        if (page > 0) {
            ItemStack back = new ItemStack(Material.ARROW);
            ItemMeta meta = back.getItemMeta();
            meta.setDisplayName("§7« Zurück");
            back.setItemMeta(meta);
            gui.setItem(45, back);
        }

        if (page < totalPages - 1) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta meta = next.getItemMeta();
            meta.setDisplayName("§7Weiter »");
            next.setItemMeta(meta);
            gui.setItem(53, next);
        }

        player.openInventory(gui);
    }

    public static void open(Player player, RecipeLoader loader, XPManager xpManager) {
        open(player, loader, xpManager, 0);
    }

    public static int getCurrentPage(Player player) {
        return playerPageMap.getOrDefault(player.getUniqueId(), 0);
    }
}