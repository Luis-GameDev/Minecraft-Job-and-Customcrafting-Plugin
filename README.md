# 🧱 Minecraft Job & Crafting System Plugin

A complete Minecraft plugin for a **job-based and level-based crafting system**, featuring custom-defined and easily self-scriptable custom recipes, XP progression, and intuitive GUIs for players.

---

## ✨ Features

- 🎓 Three playable jobs: **Farmer**, **Miner**, **Lumberjack**
- 📈 Leveling system with XP from gathering and crafting
- 🏆 Milestones at levels 15, 35, 50, 80, 100
- 🔓 Unlock custom recipes based on job and level
- 🛠 Fully configurable and expandable recipes via `recipes.yml`
- 🧪 XP only when using proper job tools
- 🧠 Bossbar XP/Level display
- 📚 `/jobsrecipes` GUI menu: shows unlocked & locked recipes
- 📦 Clickable detail-view: shows crafting recipe

---

## 📷 Screenshots

![b42071cb-6a2b-4e32-8c8c-21adaf3dbcab](https://github.com/user-attachments/assets/b5b815f3-5613-4590-8db3-5d5d4a592c50)
![2336e2f4-8958-4c8b-b424-6c66f45febed](https://github.com/user-attachments/assets/c48a9c50-03c7-4b92-bcef-a7099b6bcd84)
![82fd0a6d-587b-4ae0-bfbf-ae4444bbde72](https://github.com/user-attachments/assets/32f8e345-f925-44c3-907e-aec691427fca)

---

## 🧰 Installation

1. Download the `MinecraftPlugin-1.0-SNAPSHOT.jar` from this repo
2. Place the plugin `.jar` into your `plugins/` folder
3. Start the server to generate `recipes.yml`
4. Configure your recipes inside `plugins/MinecraftPlugin/recipes.yml`

---

## ⚙️ Commands

| Command            | Description                              |
|--------------------|------------------------------------------|
| `/jobsrecipes`     | Opens the GUI with all job recipes       |
| `/jobs`            | Select one of the three jobs             |

---

## 📄 Recipe Example (`recipes.yml`)

```yaml
lumberjack_axe_tier1:
  result:
    material: IRON_AXE
    display_name: "§a Adepts Lumberjack Axe"
    nbt_id: "lumberjack_axe_tier1"
  shape:
    - " WI"
    - " SW"
    - " S "
  ingredients:
    W: { material: OAK_LOG }
    I: { material: IRON_INGOT }
    S: { material: STICK }
  requirements:
    job: LUMBERJACK
    level: 0
  reward:
    xp: 3.0
```

---

## 📚 Dependencies

- Spigot / Paper (tested with 1.21.4)
- Java 21+
- No external libraries required

---

## 🤝 Contributions

Pull requests and feedback are welcome!  
Planned features:
- [ ] More job types
- [ ] More recipies depending on eachother
- [ ] Random drops

---

## 📜 License

This project is licensed under the MIT License.
