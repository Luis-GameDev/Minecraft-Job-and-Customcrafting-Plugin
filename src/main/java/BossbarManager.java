import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossbarManager {

    private final Map<UUID, BossBar> bars = new HashMap<>();
    private final Map<UUID, BukkitRunnable> timers = new HashMap<>();
    private final Plugin plugin;

    public BossbarManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void update(Player player, JobType job, double currentXp, double currentLevelXp, double nextLevelXp, int level) {
        UUID uuid = player.getUniqueId();
        double progress = Math.min(1.0, (currentXp - currentLevelXp) / (nextLevelXp - currentLevelXp));

        final BossBar bossBar;

        if (bars.containsKey(uuid)) {
            bossBar = bars.get(uuid);
        } else {
            bossBar = Bukkit.createBossBar(job.name(), BarColor.GREEN, BarStyle.SEGMENTED_10);
            bossBar.addPlayer(player);
            bars.put(uuid, bossBar);
        }

        bossBar.setProgress(progress);
        bossBar.setTitle("§f" + job.name() + " §7(Level " + level + ") §8| §a" + String.format("%.1f", currentXp) + " XP");
        bossBar.setVisible(true);

        // Timer reset
        if (timers.containsKey(uuid)) {
            timers.get(uuid).cancel();
        }

        BukkitRunnable hideTask = new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.setVisible(false);
            }
        };

        hideTask.runTaskLater(plugin, 20 * 5); // 5 Sekunden
        timers.put(uuid, hideTask);
    }

    public void remove(Player player) {
        UUID uuid = player.getUniqueId();
        if (bars.containsKey(uuid)) {
            bars.get(uuid).removeAll();
            bars.remove(uuid);
        }
        if (timers.containsKey(uuid)) {
            timers.get(uuid).cancel();
            timers.remove(uuid);
        }
    }
}
