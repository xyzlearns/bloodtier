# 🩸 BloodTier

**Kill. Progress. Dominate.**

BloodTier is a PvP progression plugin where players earn **Blood Points (BP)** by killing others and unlock powerful tiers as they progress.

Designed for Lifesteal, SMP, and competitive PvP servers, this plugin makes every fight meaningful.

---

## ⚔ Features

- 🩸 Blood Points system on player kills
- ⚡ Tier-based progression system
- 💀 Blood loss on death (risk vs reward)
- 🚫 Anti-farming protection (prevents abuse)
- 📊 Progress tracking with `/bloodtier`
- 🔄 Configurable rewards (commands per tier)
- 🧠 Lightweight and optimized

---

## 📜 Commands

| Command | Description |
|--------|------------|
| `/bloodtier` | View your Blood Points and Tier |
| `/bloodtier reload` | Reload config (admin) |

---

## 🔐 Permissions

| Permission | Description |
|-----------|------------|
| `bloodtier.use` | Use `/bloodtier` |
| `bloodtier.admin` | Reload config |

---

## ⚙ Config Example

```yaml
blood-system:
  kill-points: 10
  death-loss: 5

anti-farm:
  enabled: true
  cooldown-seconds: 300

tiers:
  1:
    required-bp: 50
    commands:
      - "say %player% reached Tier 1!"
  2:
    required-bp: 150
    commands:
      - "say %player% reached Tier 2!"

```
## 📦 Installation

1. Download the latest `.jar` from releases
2. Place it inside your server’s `/plugins` folder
3. Start or restart the server
4. Edit `config.yml` to customize the system
5. Use `/bloodtier` in-game

---

## 🧪 Compatibility

- Minecraft Versions: **1.20 – 1.21**
- Server Software: **Spigot & Paper**
- Java Version: **17 or higher**  