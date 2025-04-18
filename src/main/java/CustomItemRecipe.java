import java.util.Map;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class CustomItemRecipe {
    public String id;
    public Material resultMaterial;
    public String displayName;
    public String nbtKey;
    public Map<Enchantment, Integer> enchantments;
    public List<String> shape;
    public Map<Character, ItemIngredient> ingredients;
    public JobType requiredJob;
    public int requiredLevel;
}
