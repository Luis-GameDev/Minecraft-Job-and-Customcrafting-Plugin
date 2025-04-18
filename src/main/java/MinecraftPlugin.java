import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Plugin wurde aktiviert!");
        XPManager xpManager = new XPManager(this);
        getCommand("jobs").setExecutor(new JobsCommand(xpManager));
                
        Bukkit.getPluginManager().registerEvents(new JobsGUIListener(xpManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin wurde deaktiviert!");
    }

    // /heal Befehl
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("heal")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Dieser Befehl kann nur von Spielern verwendet werden.");
                return true;
            }

            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.sendMessage("§aDu wurdest geheilt und gefüttert!");
            return true;
        }
        return false;
    }
}
