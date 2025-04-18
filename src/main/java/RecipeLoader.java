import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeLoader {

    private final Plugin plugin;
    public final Map<String, CustomItemRecipe> loadedRecipes = new HashMap<>();

    public RecipeLoader(Plugin plugin) {
        this.plugin = plugin;
    }

    public void loadAllRecipes() {
        File file = new File(plugin.getDataFolder(), "recipes.yml");
        if (!file.exists()) {
            plugin.saveResource("recipes.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String id : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(id);
            if (section == null) continue;

            CustomItemRecipe recipe = new CustomItemRecipe();
            recipe.id = id;

            // Result
            ConfigurationSection result = section.getConfigurationSection("result");
            recipe.resultMaterial = Material.valueOf(result.getString("material"));
            recipe.displayName = result.getString("display_name");
            recipe.nbtKey = result.getString("nbt_id");

            // Enchantments
            recipe.enchantments = new HashMap<>();
            if (result.contains("enchantments")) {
                ConfigurationSection enchSec = result.getConfigurationSection("enchantments");
                for (String enchKey : enchSec.getKeys(false)) {
                    Enchantment ench = Enchantment.getByName(enchKey.toUpperCase());
                    if (ench != null) {
                        recipe.enchantments.put(ench, enchSec.getInt(enchKey));
                    }
                }
            }

            // Shape
            recipe.shape = section.getStringList("shape");

            // Ingredients
            recipe.ingredients = new HashMap<>();
            ConfigurationSection ing = section.getConfigurationSection("ingredients");
            for (String key : ing.getKeys(false)) {
                char symbol = key.charAt(0);
                ConfigurationSection i = ing.getConfigurationSection(key);
                Material mat = Material.valueOf(i.getString("material"));
                String nbt = i.contains("nbt_id") ? i.getString("nbt_id") : null;

                ItemIngredient ingredient = new ItemIngredient();
                ingredient.material = mat;
                ingredient.nbtKey = nbt;

                recipe.ingredients.put(symbol, ingredient);
            }

            // Requirements
            ConfigurationSection req = section.getConfigurationSection("requirements");
            recipe.requiredJob = JobType.valueOf(req.getString("job"));
            recipe.requiredLevel = req.getInt("level");

            // Reward
            if (section.contains("reward")) {
                ConfigurationSection reward = section.getConfigurationSection("reward");
                if (reward != null && reward.contains("xp")) {
                    recipe.rewardXp = reward.getDouble("xp");
                }
            }

            // Rezept speichern und registrieren
            loadedRecipes.put(recipe.nbtKey, recipe);
            registerRecipe(recipe);
        }
    }

    private void registerRecipe(CustomItemRecipe recipe) {
        ItemStack result = new ItemStack(recipe.resultMaterial);
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(recipe.displayName);

            NamespacedKey key = new NamespacedKey(plugin, "custom_item");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, recipe.nbtKey);

            for (Map.Entry<Enchantment, Integer> entry : recipe.enchantments.entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }

            result.setItemMeta(meta);
        }

        NamespacedKey namespacedKey = new NamespacedKey(plugin, recipe.nbtKey);
        ShapedRecipe shaped = new ShapedRecipe(namespacedKey, result);
        shaped.shape(recipe.shape.toArray(new String[0]));

        for (Map.Entry<Character, ItemIngredient> entry : recipe.ingredients.entrySet()) {
            shaped.setIngredient(entry.getKey(), entry.getValue().material);
        }

        Bukkit.addRecipe(shaped);
    }
}
