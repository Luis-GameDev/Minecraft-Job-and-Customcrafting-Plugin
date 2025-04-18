import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XPManager {

    private final File dataFile;
    private final Map<UUID, PlayerJobData> playerDataMap = new HashMap<>();

    public XPManager(Plugin plugin) {
        File folder = new File(plugin.getDataFolder(), "data");
        if (!folder.exists()) folder.mkdirs();

        this.dataFile = new File(folder, "jobs.json");
        load();
    }

    public boolean addXp(UUID playerId, double amount) {
        PlayerJobData data = playerDataMap.get(playerId);
        if (data != null) {
            int oldLevel = calculateLevel(data.getXp());
            data.addXp(amount);
            int newLevel = calculateLevel(data.getXp());
            save();
            return newLevel > oldLevel;
        }
        return false;
    }

    public double getXp(UUID playerId) {
        PlayerJobData data = playerDataMap.get(playerId);
        return (data != null) ? data.getXp() : 0.0;
    }

    public int getLevel(UUID playerId) {
        double xp = getXp(playerId);
        return calculateLevel(xp);
    }

    public double getXpForLevel(int level) {
        return 100 * Math.pow(level, 1.5);
    }

    private int calculateLevel(double xp) {
        int level = 0;
        while (xp >= getXpForLevel(level + 1)) {
            level++;
        }
        return level;
    }

    public JobType getActiveJob(UUID playerId) {
        PlayerJobData data = playerDataMap.get(playerId);
        return (data != null) ? data.getActiveJob() : null;
    }

    public void setJob(UUID playerId, JobType newJob) {
        PlayerJobData data = playerDataMap.get(playerId);
        if (data == null) {
            playerDataMap.put(playerId, new PlayerJobData(newJob, 0.0));
        } else {
            double reducedXp = data.getXp() * 0.7;
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
                double xp = ((Number) obj.get("xp")).doubleValue();

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
