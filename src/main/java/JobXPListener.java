import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

public class JobXPListener implements Listener {

    private final XPManager xpManager;
    private final BossbarManager bossbarManager;
    private final RecipeLoader recipeLoader;
    private final FileConfiguration config;
    private final Plugin plugin;

    private final Set<Integer> milestones = Set.of(15, 35, 50, 80, 100);

    public JobXPListener(XPManager xpManager, BossbarManager bossbarManager, RecipeLoader recipeLoader, Plugin plugin) {
        this.xpManager = xpManager;
        this.bossbarManager = bossbarManager;
        this.recipeLoader = recipeLoader;
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        JobType job = xpManager.getActiveJob(uuid);
        if (job == null) return;

        int level = xpManager.getLevel(uuid);

        // 🔒 Nur XP, wenn Spieler ein gültiges Tool hat
        if (!hasValidTool(player, job, level)) return;

        Block block = event.getBlock();
        Material type = block.getType();

        String path = "xp-values." + job.name() + "." + type.name();
        if (!config.contains(path)) return;

        double gainedXp = config.getDouble(path);
        double beforeXp = xpManager.getXp(uuid);
        boolean leveledUp = xpManager.addXp(uuid, gainedXp);

        double currentXp = xpManager.getXp(uuid);
        int currentLevel = xpManager.getLevel(uuid);
        double currentLevelXp = xpManager.getXpForLevel(currentLevel);
        double nextLevelXp = xpManager.getXpForLevel(currentLevel + 1);

        // XP-Leiste im HUD
        player.setLevel(currentLevel);
        player.setExp((float) ((currentXp - currentLevelXp) / (nextLevelXp - currentLevelXp)));

        // Bossbar
        bossbarManager.update(player, job, currentXp, currentLevelXp, nextLevelXp, currentLevel);

        // Feedback bei Level-Up
        if (leveledUp) {
            player.sendMessage("§6⬆ Du bist jetzt Level " + currentLevel + "!");
            player.playSound(player.getLocation(), "entity.player.levelup", 1.0f, 1.0f);

            if (milestones.contains(currentLevel)) {
                Bukkit.broadcastMessage("§d» §e" + player.getName() + " §7hat Level §b" + currentLevel +
                        " §7in seinem Beruf §f" + job.name() + " §7erreicht!");
            }
        }
    }

    private boolean hasValidTool(Player player, JobType job, int level) {
        ItemStack held = player.getInventory().getItemInMainHand();
        if (held == null || !held.hasItemMeta()) return false;

        ItemMeta meta = held.getItemMeta();
        if (meta == null) return false;

        NamespacedKey key = new NamespacedKey(plugin, "custom_item");
        String toolId = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (toolId == null) return false;

        CustomItemRecipe tool = recipeLoader.loadedRecipes.get(toolId);
        if (tool == null) return false;

        return tool.requiredJob == job && level >= tool.requiredLevel;
    }
}
