import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Plugin wurde aktiviert!");
        saveDefaultConfig();
        XPManager xpManager = new XPManager(this);
        BossbarManager bossbarManager = new BossbarManager(this);
        RecipeLoader recipeLoader = new RecipeLoader(this);
        JobXPListener xpListener = new JobXPListener(xpManager, bossbarManager, recipeLoader, this);

        recipeLoader.loadAllRecipes();
        getCommand("jobs").setExecutor(new JobsCommand(xpManager));

        Bukkit.getPluginManager().registerEvents(new CraftItemListener(xpManager, recipeLoader, bossbarManager, this), this);
        Bukkit.getPluginManager().registerEvents(new JobsGUIListener(xpManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new JobXPListener(xpManager, bossbarManager, recipeLoader, this), this);
        Bukkit.getPluginManager().registerEvents(new CustomCraftingManager(recipeLoader, xpManager, this), this);
        Bukkit.getPluginManager().registerEvents(xpListener, this);
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
