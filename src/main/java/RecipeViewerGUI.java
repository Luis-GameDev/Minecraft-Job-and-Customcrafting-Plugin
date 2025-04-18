// RecipeViewerGUI.java
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeViewerGUI {

    public static void open(Player player, CustomItemRecipe recipe, RecipeLoader loader, XPManager xpManager) {
        Inventory gui = Bukkit.createInventory(null, 54, "§8Rezeptvorschau");

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§7« Zurück");
        back.setItemMeta(backMeta);
        gui.setItem(0, back);

        int[] slots = {10, 11, 12, 19, 20, 21, 28, 29, 30};
        int i = 0;

        for (String row : recipe.shape) {
            for (char c : row.toCharArray()) {
                if (c == ' ') {
                    gui.setItem(slots[i], new ItemStack(Material.AIR));
                } else {
                    ItemIngredient ing = recipe.ingredients.get(c);
                    if (ing != null) {
                        gui.setItem(slots[i], new ItemStack(ing.material));
                    }
                }
                i++;
            }
        }

        gui.setItem(24, recipe.createDisplayItem());

        player.openInventory(gui);
    }
}