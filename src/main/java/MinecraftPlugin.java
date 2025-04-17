import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Plugin wurde aktiviert!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin wurde deaktiviert!");
    }
}
