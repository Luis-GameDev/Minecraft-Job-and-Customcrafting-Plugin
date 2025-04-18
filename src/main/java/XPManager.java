import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class XPManager {

    private final File dataFile;
    private final Map<UUID, PlayerJobData> playerDataMap = new HashMap<>();

    public XPManager(Plugin plugin) {
        File folder = new File(plugin.getDataFolder(), "data");
        if (!folder.exists()) folder.mkdirs();

        this.dataFile = new File(folder, "jobs.json");
        load();
    }

    public void addXp(UUID playerId, int amount) {
        PlayerJobData data = playerDataMap.get(playerId);
        if (data != null) {
            data.addXp(amount);
            save();
        }
    }

    public int getXp(UUID playerId) {
        PlayerJobData data = playerDataMap.get(playerId);
        return (data != null) ? data.getXp() : 0;
    }

    public int getLevel(UUID playerId) {
        int xp = getXp(playerId);
        return calculateLevel(xp);
    }

    private int calculateLevel(int xp) {
        // Beispiel: XP = 100 * level^1.5 (ungefähr)
        int level = 0;
        while (xp >= getXpForLevel(level + 1)) {
            level++;
        }
        return level;
    }

    private int getXpForLevel(int level) {
        return (int) (100 * Math.pow(level, 1.5));
    }

    public JobType getActiveJob(UUID playerId) {
        PlayerJobData data = playerDataMap.get(playerId);
        return (data != null) ? data.getActiveJob() : null;
    }

    public void setJob(UUID playerId, JobType newJob) {
        PlayerJobData data = playerDataMap.get(playerId);
        if (data == null) {
            playerDataMap.put(playerId, new PlayerJobData(newJob, 0));
        } else {
            int reducedXp = (int) (data.getXp() * 0.7);
            playerDataMap.put(playerId, new PlayerJobData(newJob, reducedXp));
        }
        save();
    }

    public void load() {
        if (!dataFile.exists()) return;

        try (FileReader reader = new FileReader(dataFile)) {
            JSONObject json = (JSONObject) new JSONParser().parse(reader);

            for (Object key : json.keySet()) {
                UUID uuid = UUID.fromString((String) key);
                JSONObject obj = (JSONObject) json.get(key);

                JobType job = JobType.valueOf((String) obj.get("activeJob"));
                int xp = ((Long) obj.get("xp")).intValue();

                playerDataMap.put(uuid, new PlayerJobData(job, xp));
            }

        } catch (Exception e) {
            Bukkit.getLogger().warning("Fehler beim Laden der XP-Daten: " + e.getMessage());
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            JSONObject json = new JSONObject();

            for (Map.Entry<UUID, PlayerJobData> entry : playerDataMap.entrySet()) {
                JSONObject obj = new JSONObject();
                obj.put("activeJob", entry.getValue().getActiveJob().name());
                obj.put("xp", entry.getValue().getXp());

                json.put(entry.getKey().toString(), obj);
            }

            writer.write(json.toJSONString());

        } catch (Exception e) {
            Bukkit.getLogger().warning("Fehler beim Speichern der XP-Daten: " + e.getMessage());
        }
    }
}
