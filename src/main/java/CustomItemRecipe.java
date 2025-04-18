import java.util.Map;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public class CustomItemRecipe {
    public String id;
    public Material resultMaterial;
    public String displayName;
    public String nbtKey;
    private static Plugin plugin;
    public Map<Enchantment, Integer> enchantments;
    public List<String> shape;
    public Map<Character, ItemIngredient> ingredients;
    public JobType requiredJob;
    public int requiredLevel;
    public double rewardXp = 0.0;

    public ItemStack createDisplayItem() {
        ItemStack result = new ItemStack(resultMaterial);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.getPersistentDataContainer().set(
                    new NamespacedKey(plugin, "custom_item"),
                    PersistentDataType.STRING,
                    nbtKey
            );
            for (Map.Entry<Enchantment, Integer> e : enchantments.entrySet()) {
                meta.addEnchant(e.getKey(), e.getValue(), true);
            }
            result.setItemMeta(meta);
        }
        return result;
    }

    public static void setPlugin(Plugin p) {
        plugin = p;
    }
}


