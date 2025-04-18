import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.UUID;

public class JobXPListener implements Listener {

    private final XPManager xpManager;
    private final BossbarManager bossbarManager;
    private final FileConfiguration config;

    private final Set<Integer> milestones = Set.of(15, 35, 50, 80, 100);

    public JobXPListener(XPManager xpManager, BossbarManager bossbarManager, Plugin plugin) {
        this.xpManager = xpManager;
        this.bossbarManager = bossbarManager;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        JobType job = xpManager.getActiveJob(uuid);
        if (job == null) return;

        Block block = event.getBlock();
        Material type = block.getType();

        String jobName = job.name();
        String blockName = type.name();
        String path = "xp-values." + jobName + "." + blockName;

        if (!config.contains(path)) return;

        double gainedXp = config.getDouble(path);
        double beforeXp = xpManager.getXp(uuid);

        boolean leveledUp = xpManager.addXp(uuid, gainedXp);

        double currentXp = xpManager.getXp(uuid);
        int currentLevel = xpManager.getLevel(uuid);
        double currentLevelXp = xpManager.getXpForLevel(currentLevel);
        double nextLevelXp = xpManager.getXpForLevel(currentLevel + 1);

        // Setze Level und Exp-Leiste im Minecraft-UI
        player.setLevel(currentLevel);
        player.setExp((float) ((currentXp - currentLevelXp) / (nextLevelXp - currentLevelXp)));

        // Bossbar aktualisieren (optional)
        bossbarManager.update(player, job, currentXp, currentLevelXp, nextLevelXp);

        // Level-Up Feedback
        if (leveledUp) {
            player.sendMessage("§6⬆ You are now Level " + currentLevel + "!");
            player.playSound(player.getLocation(), "entity.player.levelup", 1.0f, 1.0f);

            if (milestones.contains(currentLevel)) {
                Bukkit.broadcastMessage("§d» §e" + player.getName() + " §7has reached Level §b" + currentLevel + " §7as §f" + job.name());
            }
        }
    }
}
