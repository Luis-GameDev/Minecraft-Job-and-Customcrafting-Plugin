import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JobsRecipesCommand implements CommandExecutor {

    private final RecipeLoader recipeLoader;
    private final XPManager xpManager;

    public JobsRecipesCommand(RecipeLoader recipeLoader, XPManager xpManager) {
        this.recipeLoader = recipeLoader;
        this.xpManager = xpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly Players can use this command.");
            return true;
        }

        RecipeOverviewGUI.open(player, recipeLoader, xpManager);
        return true;
    }
}
